package ch27_lowlevel;

import onjava.Nap;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 3月12日 周四
 * @time 13:13
 * P.305 §6.6 临界区
 * Synchronizing blocks instead of entire methods speeds up access.
 * <p>
 * 下面的示例演示了对代码块（而不是整个方法）进行同步可以极大地提升方法对于其他任务的可访问性（即并发处理能力）。
 * 该示例会统计成功访问 method() 方法的次数，并启动相互竞争以试图调用 method() 的任务：
 * <p>
 * 记住，使用 synchronized 控制块也有风险：它要求你必须能够确定控制块外部的非 synchronized 代码的确是安全的。
 */
abstract class Guarded {
    AtomicLong callCount = new AtomicLong();

    public abstract void method();

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + callCount.get();
    }
}

/* 本类演示了粗粒度锁（Coarse-Grained Lock）的实现及其带来的性能瓶颈。
 * 通过在方法级别使用 synchronized 关键字，整个方法体被划定为单一临界区。
 * 这导致线程在执行如 Nap 休眠等完全无需并发保护的耗时操作时，依然全程霸占对象锁。
 * 这种无差别的排他性阻塞使得多核 CPU 的并发计算能力被强制退化为纯串行执行，不仅大幅拉低了系统吞吐量，
 * 还会因极其漫长的锁持有时间，引发严重的线程饥饿与操作系统调度失衡（体现为各线程执行次数差异巨大）。
 */
class SynchronizedMethod extends Guarded {
    @Override
    public synchronized void method() {
        new Nap(0.01);
        callCount.incrementAndGet();
    }
}

/* 本类演示了通过缩小锁粒度（Fine-Grained Lock）来优化临界区（Critical Section）的标准实践。
 * 其核心在于仅将真正发生共享状态突变的代码置于 synchronized 同步块中，
 * 而将耗时且不存在并发逃逸的安全操作（如 Nap 休眠）强行剥离至锁外部。
 * 这种设计允许多个线程在无锁状态下并发处理耗时逻辑，仅在状态写入的微秒级瞬间进行排队。
 * 锁持有时间的极度压缩显著降低了线程挂起与唤醒的上下文切换开销，使得系统调度更加公平，整体吞吐量呈倍数级增长。
 * 应用此模式的绝对前提是：必须在架构逻辑上 100% 确认被移出同步块的代码不存在线程安全问题。
 */
class CriticalSection extends Guarded {
    @Override
    public void method() {
        new Nap(0.01);
        synchronized (this) {
            callCount.incrementAndGet();
        }
    }
}

class Caller implements Runnable {
    private Guarded g;

    Caller(Guarded g) {
        this.g = g;
    }

    /* 记录当前 Caller 实例成功调用 method() 的次数变量 successfulCalls 调优：
     * 1. 使用 long 而不是 AtomicLong: 线程封闭（消除 CAS 开销），此变量不存在跨线程共享。
     * 在 CompletableFuture.runAsync() 的调度模型中，每个 Caller 实例都在自己独立的单个工作线程中完整执行 run() 方法。
     * 同时，后台的 Timer 线程也不会触碰该变量。根据“线程封闭（Thread Confinement）”原则，单线程内部的变量天然是线程安全的。
     * 使用 AtomicLong 不仅毫无必要，反而会引入无意义的底层 CAS（硬件级比较并交换）检查开销，拖慢高频循环的执行速度。
     * 此外由于底层框架提供的 Happens-Before 内存可见性保证，即便结合 thenRunAsync() 等链式 API 进行任务交接，单个任务实例的生命周期内也严格维持串行语义。
     * 2. 使用基本类型 long 而不是包装类 Long: 避免装箱（消除 GC 压力）
     * 在 while(!stop) 这个极高频的毫秒级循环体内，执行累加操作。如果声明为包装类 Long，每次 successfulCalls++ 都会在底层触发自动拆箱与装箱
     * （等价于 successfulCalls = Long.valueOf(successfulCalls.longValue() + 1)）。这不仅会产生巨量的堆内存垃圾（短生命周期对象），
     * 还会给垃圾回收器（GC）带来极大压力。降级为基本数据类型 long 后，累加操作将直接在 CPU 寄存器或线程私有栈中极速完成。
     */
    // private AtomicLong successfulCalls = new AtomicLong();
    // private Long successfulCalls = 0L;
    private long successfulCalls = 0L;

    /* 跨线程状态标志位 stop 调优：
     * 1. 必须保留 volatile 关键字：保障跨线程的内存可见性。在该执行逻辑中存在隐式的跨线程交互：
     * Timer 内部的后台调度线程负责异步写入（stop = true），而 CompletableFuture 分配的 Caller 工作线程负责高频读取（while (!stop)）。
     * 若无 volatile 修饰，JIT 编译器出于性能优化，极易将 stop 变量缓存至 Caller 线程的局部寄存器中（即循环提升优化）。
     * 这将导致主存中的修改对 Caller 线程不可见，使其永远无法跳出 while 循环。因此，stop 必须具备可见性。
     * 作者使用了 AtomicBoolean，其实这里用 private volatile boolean stop = false; 是完全等价且更轻量的。
     * 2. 使用 volatile boolean 替代 AtomicBoolean：消除不必要的同步开销。
     * AtomicBoolean 的核心价值在于为复合操作（如 compareAndSet）提供硬件级原子性与无锁并发安全。
     * 但在此场景下，stop 的状态流转仅涉及“单线程纯写”与“单线程纯读”，没有任何“读取-修改-写入”的并发竞争。
     * 在 Java 内存模型（JMM）中，针对基础 boolean 类型变量的单一读写天然具备物理原子性。
     * 因此，仅依赖 volatile 提供的内存读写屏障（Happens-Before 语义）即可形成完整的线程安全闭环，彻底移除了 Atomic 类底层的过度优化机制。
     */
    // private AtomicBoolean stop = new AtomicBoolean(false);
    private volatile boolean stop = false;

    @Override
    public void run() {
        new Timer(true).schedule(new TimerTask() {
            @Override
            public void run() {
                // stop.set(true);
                stop = true;
            }
        }, 2500);
        // while (!stop.get()) {
        while (!stop) {
            g.method();
            successfulCalls++;
        }
        System.out.println("-> " + successfulCalls);
    }
}

public class SynchronizedComparison {
    static void test(Guarded g) {
        List<CompletableFuture<Void>> callers =
                Stream.of(new Caller(g), new Caller(g), new Caller(g), new Caller(g))
                        .map(CompletableFuture::runAsync)
                        // .collect(Collectors.toList());
                        .toList();
        callers.forEach(CompletableFuture::join);
        System.out.println(g);
    }

    public static void main(String[] args) {
        test(new CriticalSection());
        test(new SynchronizedMethod());
    }
}
/* Output:
-> 161
-> 161
-> 161
-> 161
CriticalSection: 644
-> 112
-> 21
-> 86
-> 21
SynchronizedMethod: 240
 */
