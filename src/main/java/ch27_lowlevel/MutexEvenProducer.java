package ch27_lowlevel;

import onjava.Nap;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author runningpig66
 * @date 3月12日 周四
 * @time 17:38
 * P.308 §6.6 临界区 §6.6.2 使用显式 Lock 对象
 * Preventing thread collisions with mutexes
 * <p>
 * java.util.concurrent 库提供了显式的互斥机制，定义在 java.util.concurrent.locks 中。
 * Lock 对象必须显式地创建、加锁以及解锁，因此它的语法不如内建的 synchronized 关键字优雅。
 * 不过它在解决某些类型的问题时更灵活。下面用显式的 Lock 重写 SynchronizedEvenProducer.java：
 * <p>
 * MutexEvenProducer 中增加了一个名为 lock 的互斥锁，并在 next() 中通过 lock() 和 unlock() 方法创建了一个临界区。
 * 在使用 Lock 对象的时候，要注意用这种使用方式：在调用 lock() 后，必须放置一个 try-finally 语句，
 * 并在 finally 子句中调用 unlock()——这是唯一能保证锁一定会被释放的方法。
 * 注意，return 语句必须放在 try 子句中，以确保 unlock() 不会执行得太早，导致将数据暴露给下一个任务。
 * <p>
 * 显式锁 (ReentrantLock) 核心机制与高级特性总结：
 * 一、 基础语义与规范
 * 1. 内存等效性：提供与 synchronized 绝对等价的内存读写屏障（Happens-Before 语义），保障复合操作的物理原子性与共享变量状态的跨线程可见性。
 * 2. 可重入性：允许已获取锁的当前工作线程再次无阻塞地进入该锁保护的其他代码块。底层通过维护“持有者 Thread ID”与“重入计数器”，从根本上避免单线程自我死锁。
 * 3. 异常逃逸控制 (防死锁范式)：显式锁无 JVM 托管的自动回收机制。必须严格遵循 "lock() 紧接 try-finally" 的工程规范。
 * 若临界区异常崩溃且未可靠执行 unlock()，将导致锁引用永久泄漏及全局线程死锁。
 * 二、 高级调度特性 (相比 synchronized 的独有优势)
 * 1. 非阻塞探测 tryLock()：瞬间尝试获取锁，若锁已被占用则立刻返回 false 并转投其他任务，彻底消除无意义的线程死等。
 * 2. 限时等待 tryLock(time, unit)：设定有界的阻塞排队时间，超时未获取到资源则主动放弃，为高并发架构确立了天然的容错边界。
 * 3. 响应中断 lockInterruptibly()：允许处于阻塞排队状态的线程被外部信号强制打断（抛出 InterruptedException），支持任务的提前取消与优雅停机。
 * 4. 跨域加解锁 (Cross-Scope Locking)：打破了隐式锁必须在单一代码块内闭合的物理局限，允许跨方法甚至跨对象传递并释放锁引用，为复杂数据结构（如交替锁）提供了底层支持。
 * 5. 公平排队选项 (Fairness)：构造时可选配公平策略 (new ReentrantLock(true))，强制系统按线程申请锁的时间顺序进行排队，有效解决极端场景下的线程饥饿问题。
 */
public class MutexEvenProducer extends IntGenerator {
    private int currentEvenValue = 0;
    private Lock lock = new ReentrantLock();

    @Override
    public int next() {
        lock.lock();
        try {
            ++currentEvenValue;
            new Nap(0.01); // Cause failure faster
            ++currentEvenValue;
            return currentEvenValue;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        EvenChecker.test(new MutexEvenProducer());
    }
}
/* Output:
No odd numbers discovered
 */
