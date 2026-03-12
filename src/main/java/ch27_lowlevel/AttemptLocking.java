package ch27_lowlevel;

import onjava.Nap;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author runningpig66
 * @date 3月12日 周四
 * @time 17:55
 * P.309 §6.6 临界区 §6.6.2 使用显式 Lock 对象
 * Locks in the concurrent library allow you to give up on trying to acquire a lock
 * <p>
 * 总体来说，在使用 synchronized 的时候，要写的代码会更少，同时用户错误发生的概率也会大大降低，
 * 因此通常只需要在解决特殊问题的时候才使用显式 Lock 对象。例如，使用 synchronized 关键字时，你无法在开始尝试获取锁后，
 * （如果没有获取成功）就立刻主动放弃，也无法等待一段时间（仍然没有获取成功）后再放弃——要想这样做，就必须使用 concurrent 库：
 */
public class AttemptLocking {
    private ReentrantLock lock = new ReentrantLock();

    public void untimed() {
        /* boolean tryLock(): 显式锁非阻塞探测机制（Non-blocking Lock Acquisition）。
         * 调用 tryLock() 时，当前线程会瞬间尝试获取锁。若锁处于空闲状态，则成功获取并返回 true；
         * 若锁已被其他线程占用，则立刻放弃并返回 false。整个探测过程严格无阻塞，当前线程绝对不会因获取锁失败而被操作系统挂起排队。
         * 这种设计彻底打破了传统 synchronized 关键字或 Lock.lock() 导致的强制阻塞僵局。借助该“快速失败（Fail-Fast）”特性，
         * 线程在遭遇高频锁竞争时，能够立即转而执行降级逻辑或其他独立计算任务，从而极大保障了业务执行流的连续性与系统整体的响应吞吐量。
         */
        boolean captured = lock.tryLock();
        try {
            System.out.println("tryLock(): " + captured);
        } finally {
            if (captured) {
                /* void unlock(): 显式锁的释放必须以成功获取锁为绝对前提。
                 * ReentrantLock 在底层严格维护了当前持有锁的线程标识（Thread ID）。当调用 unlock() 时，
                 * 系统会主动核对当前执行解锁的线程是否为该锁的合法持有者。如果当前线程并未持有该锁（即 captured 为 false）
                 * 却强行发起释放指令，底层校验失败并会直接抛出 IllegalMonitorStateException 运行期异常。
                 * 这是一种底层的强制安全保护机制。它从根本上杜绝了由于代码逻辑漏洞或恶意调用，导致其他线程正在使用的锁被意外释放，
                 * 从而引发临界区数据状态瞬间崩溃的灾难性后果。因此，在 finally 块中必须通过获取锁时记录的局部状态变量，进行严格的解锁准入判断。
                 */
                lock.unlock();
            }
        }
    }

    public void timed() {
        boolean captured;
        try {
            /* boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException: 限时等待锁探测机制。
             * 该机制的底层执行流表现为条件阻塞。当发起调用时，若目标锁处于空闲状态，系统会瞬间完成加锁并立即返回 true，产生零阻塞开销。
             * 仅当锁已被其他线程强占时，当前工作线程才会被操作系统主动挂起，从而进入由传入参数严格限定时长的休眠排队期。
             * 在指定的超时等待窗口内，底层线程调度器会持续监听锁状态。如果占用锁的线程在此期间执行了释放操作，当前被挂起的线程将被即时唤醒、
             * 成功抢占该锁并提前返回 true。反之，若参数设定的时间耗尽依然未能获取到锁，线程将触发超时退出机制，主动结束排队并返回 false。
             * 此外，在阻塞等待期间若收到外部的中断信号，该方法会立即响应并抛出 InterruptedException。这种允许限时退出的设计，
             * 为高并发分布式架构设定了极佳的容错边界，能够有效防止因单点资源死锁或极端竞争导致的大面积线程挂起与系统资源耗尽。
             */
            captured = lock.tryLock(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            System.out.println("tryLock(3, TimeUnit.SECONDS): " + captured);
        } finally {
            if (captured) {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        final AttemptLocking al = new AttemptLocking();
        al.untimed(); // True -- lock is available
        al.timed(); // True -- lock is available
        // Now create a second task to grab the lock:
        CompletableFuture.runAsync(() -> {
            al.lock.lock();
            System.out.println("acquired");
        });
        // 强制主线程休眠以抹平操作系统的线程调度延迟，确保异步任务有充足时间完成初始化并成功抢占锁。
        new Nap(0.1); // Give the second task a chance
        al.untimed(); // False -- lock grabbed by task
        al.timed(); // False -- lock grabbed by task
    }
}
/* Output:
tryLock(): true
tryLock(3, TimeUnit.SECONDS): true
acquired
tryLock(): false
tryLock(3, TimeUnit.SECONDS): false
 */
