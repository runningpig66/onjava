package ch27_lowlevel;

import onjava.Nap;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author runningpig66
 * @date 3月12日 周四
 * @time 16:35
 * P.307 §6.6 临界区 §6.6.1 在其他对象上进行同步
 * Synchronizing on another object
 * <p>
 * 有时必须在另一个对象上进行同步，但如果要这么做，就必须确保所有的相关任务都要在同一个对象上进行同步。
 * 下面这个示例演示了一个对象中的方法在不同的锁上进行同步时，两个任务都可以进入该对象。
 */
/* 多锁隔离（Lock Isolation）与互斥独立性设计。架构原理：
 * 1. 锁对象的物理边界：synchronized 的互斥排他性严格绑定于具体的锁对象实例（Monitor），而非代码块所在的类。
 * 2. 消除伪竞争：本类中 f() 方法隐式获取当前实例对象（this）的锁，而 g() 方法显式获取内部私有对象（syncObject）的锁。这两把锁在内存地址上完全独立。
 * 3. 提升并发吞吐量：当多线程分别调用 f() 和 g() 时，因竞争不同的 Monitor，系统不会触发任何互斥阻塞。
 * 即便 f() 在临界区内发生长时间挂起（如 I/O 阻塞或 Nap 休眠），访问 g() 的线程依然可以
 * 获取到完全不受影响的 syncObject 锁并全速执行，从而实现同一实例内部不同业务逻辑的真正物理并行。
 */
class DualSynch {
    ConcurrentLinkedQueue<String> trace = new ConcurrentLinkedQueue<>();

    public synchronized void f(boolean nap) {
        for (int i = 0; i < 5; i++) {
            trace.add(String.format("f() " + i));
            if (nap) {
                new Nap(0.01);
            }
        }
    }

    private Object syncObject = new Object();

    public void g(boolean nap) {
        synchronized (syncObject) {
            for (int i = 0; i < 5; i++) {
                trace.add(String.format("g() " + i));
                if (nap) {
                    new Nap(0.01);
                }
            }
        }
    }
}

/* 独立锁并发调度特性测试与时间片分析。调度现象解析：
 * 1. 无休眠状态的伪串行（test(false, false)）：在极短逻辑流中，操作系统级别的原生线程创建与唤醒耗时（通常为毫秒级）远大于 5 次
 * 内存写入操作的执行耗时（纳秒级）。这导致先获取系统调度的线程在后续线程完成初始化前即已执行完毕，在控制台呈现出物理层面的串行错觉。
 * 2. 挂起状态的均匀并行（test(true, true)）：通过注入 Nap 强制休眠，人为拉长了单次循环的物理执行时间，抹平了多线程启动阶段的调度时间差。
 * 各活跃线程得以在同一宏观时间窗口内均匀争夺 CPU 时间片，从而清晰展现出基于双锁隔离机制的纯粹并发执行轨迹。
 */
public class SyncOnObject {
    static void test(boolean fNap, boolean gNap) {
        DualSynch ds = new DualSynch();
        List<CompletableFuture<Void>> cfs = Arrays.stream(new Runnable[]{
                        () -> ds.f(fNap), () -> ds.g(gNap)})
                .map(CompletableFuture::runAsync)
                // .collect(Collectors.toList());
                .toList();
        cfs.forEach(CompletableFuture::join);
        ds.trace.forEach(System.out::println);
    }

    public static void main(String[] args) {
        test(true, false);
        System.out.println("****");
        test(false, true);
        System.out.println("****");
        test(true, true);
        System.out.println("****");
        test(false, false);
    }
}
/* Output:
f() 0
g() 0
g() 1
g() 2
g() 3
g() 4
f() 1
f() 2
f() 3
f() 4
****
f() 0
f() 1
g() 0
f() 2
f() 3
f() 4
g() 1
g() 2
g() 3
g() 4
****
f() 0
g() 0
g() 1
f() 1
f() 2
g() 2
g() 3
f() 3
f() 4
g() 4
****
f() 0
f() 1
f() 2
f() 3
f() 4
g() 0
g() 1
g() 2
g() 3
g() 4
 */
