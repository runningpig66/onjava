package ch26_concurrent;

import onjava.Nap;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author runningpig66
 * @date 3月3日 周二
 * @time 4:32
 * P.238 §5.9 终止长时间运行的任务
 * <p>
 * Java 5 引入了 Atomic 类，它提供了一组类型，让你可以无须担心并发问题，并放心使用。下面我们引入 AtomicBoolean 标识来告诉任务自行清理并退出：
 * 虽然多个任务可以成功地在同一个实例中调用 quit()，但 AtomicBoolean 阻止了多个任务同时修改 running，由此保证 quit() 方法是线程安全的。
 * <p>
 * 本类演示了并发编程中推荐的协作式任务终止模式，并揭示了跨线程通信时的内存可见性陷阱。
 * 1. 协作式任务终止模式的引入
 * 相比于使用底层的 Thread.interrupt() 强行中断任务（可能导致状态混乱与数据丢失），
 * 设置一个任务可见的标识符让任务自行定期检查并退出，是更为安全且代码结构更清晰的做法。
 * 2. 隐藏的并发陷阱：读写分离带来的内存可见性问题
 * 尽管每个 QuittableTask 实例在堆内存中彼此独立，且不存在多线程竞争写入的冲突，但依然构成了跨线程访问：
 * - 读操作：由线程池中的后台工作线程持续执行（位于 run() 方法的 while 循环中）。
 * - 写操作：由主线程 (main) 执行（QuittingTasks.java 中调用 quit() 方法修改标识）。
 * 3. 为什么不能使用普通 boolean？
 * 若将 running 声明为普通的 boolean，在现代多核 CPU 架构与 JIT (即时编译器) 的运行机制下，会导致严重的不可见问题：
 * - 硬件缓存隔离：后台线程为了极致性能，可能会将 running=true 长期缓存在其所在 CPU 核心的 L1 缓存或寄存器中。
 * 当主线程将值修改为 false 时，该修改可能长时间滞留在另一个 CPU 核心的缓存里，导致后台线程看不到状态变更。
 * - JIT 激进优化：由于 run() 的循环体内没有任何修改 running 的代码，JIT 编译器极有可能将 while(running)
 * 直接优化为死循环 if (running) { while(true) }，彻底切断了外部信号的接入。
 * 在这种情况下，即使测试时碰巧能够正常结束，代码在底层依然是极不安全的。
 * 4. AtomicBoolean 与内存屏障的底层保障机制
 * 为了避免普通 boolean 带来的不可控性，此处引入了 AtomicBoolean（底层依赖 volatile 语义）。
 * 当变量被声明为 volatile 时，底层硬件会插入内存屏障，并通过缓存一致性协议（如 MESI）强制执行以下机制：
 * - 写操作同步：当核心 A（主线程）写入 false 时，不仅会更新其私有缓存，还会通过总线（Bus）发出信号，并强制将新值刷入物理主内存（或多核共享的 L3 缓存）。
 * - 缓存失效：核心 B（后台线程）通过总线监听到变量修改信号后，会立刻将自身 L1 高速缓存中存留的 running=true 副本标记为失效状态。
 * - 读操作回源：下一次核心 B 执行 while(running) 时，发现本地缓存已失效，它被迫越过私有缓存，直接去主内存（或共享 L3 缓存）重新拉取最新值，从而成功读取到 false 并安全退出循环。
 * - 禁止编译器优化：内存屏障在字节码层面明确界定了变量的易变性，严禁 JIT 编译器执行诸如指令折叠或外提的激进优化，强制每次循环均需进行实际的变量查验。
 */
public class QuittableTask implements Runnable {
    final int id;

    public QuittableTask(int id) {
        this.id = id;
    }

    private AtomicBoolean running = new AtomicBoolean(true);

    public void quit() {
        running.set(false);
    }

    @Override
    public void run() {
        while (running.get()) { // [1] 只要 running 标识还是 true，该任务的 run() 方法就会持续执行。
            new Nap(0.1);
        }
        System.out.print(id + " "); // [2] 在任务退出后才会执行本行输出。
    }
}
