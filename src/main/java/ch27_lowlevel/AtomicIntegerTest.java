package ch27_lowlevel;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author runningpig66
 * @date 3月12日 周四
 * @time 10:47
 * P.303 §6.5 原子性 §6.5.2 原子类
 * <p>
 * Java 5 引入了特殊的原子变量类，如 AtomicInteger、AtomicLong、AtomicReference 等。
 * 这些类提供了原子性的更新能力，充分利用了现代处理器的硬件级原子性，实现了快速、无锁的操作。
 * 可以用 AtomicInteger 重写 UnsafeReturn.java；示例中通过使用 AtomicInteger，去除了 synchronized 关键字。
 * <p>
 * 悲观锁（以 synchronized 为代表）的底层机制基于排他性互斥，其设计前提是假定并发访问时的冲突极易发生。在进入临界区前，它会强制获取锁资源，
 * 导致未获取到锁的线程进入阻塞状态。这种机制将并发控制交由操作系统调度，不可避免地带来线程挂起、唤醒及上下文切换的重度性能开销。
 * 因此，悲观锁最适用于执行耗时较长、包含复杂复合操作或处于极端高冲突环境下的业务逻辑。
 * 在这些场景中，将未竞争到资源的线程主动休眠，能够有效避免系统算力被无意义的重试消耗殆尽，从而保障整体架构的稳定性与资源利用率。
 * <p>
 * 乐观锁（以基于 CAS 指令的原子类为代表）采用无锁化并发策略，其设计前提是假定共享资源的并发冲突概率较低。
 * 它允许线程在不加锁的情况下，将共享数据读取为线程私有副本并在本地进行计算，仅在最终写回主存的瞬间，利用 CPU 硬件级别的原子指令校验数据一致性。
 * 若校验发现数据已被其他线程修改，当前线程将不会被挂起，而是立即重新拉取最新值并再次尝试计算和提交（即自旋机制）。
 * 由于彻底规避了线程上下文切换的昂贵开销，乐观锁在处理运算速度极快、逻辑简单的轻量级任务时具有极致的性能表现。
 * 但在高频冲突的极端并发下，密集的自旋重试会导致 CPU 资源被大量空转消耗，此时应当谨慎评估其适用性。
 */
public class AtomicIntegerTest extends IntTestable {
    private AtomicInteger i = new AtomicInteger(0);

    @Override
    public int getAsInt() {
        return i.get();
    }

    @Override
    public void evenIncrement() {
        i.addAndGet(2);
    }

    public static void main(String[] args) {
        Atomicity.test(new AtomicIntegerTest());
    }
}
/* Output:
No failures found
 */
