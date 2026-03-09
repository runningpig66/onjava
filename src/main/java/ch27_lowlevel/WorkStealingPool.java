package ch27_lowlevel;

import onjava.Nap;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author runningpig66
 * @date 3月8日 周日
 * @time 22:57
 * P.280 §6.1 什么是线程？ §6.1.2 我可以创建多少线程  §工作窃取线程池 (WorkStealingPool)
 * <p>
 * 这是一种能基于所有可用处理器 ( 如 JVM 可检测出的 ) 自动创建线程池的 ExecutorService。
 * 工作窃取 ( Work Stealing ) 算法使得已完成自身输入队列中所有工作项的线程可以“窃取”其他队列中的工作项。
 * 该算法的目的是在执行计算密集型任务时，能够跨处理器分发工作项，由此最大化所有可用处理器的利用率，Java 的 fork/join 框架中同样也用到了该算法。
 * <p>
 * 本类旨在验证 ExecutorService 的 newWorkStealingPool() 方法及其底层的工作窃取 (Work Stealing) 算法。
 * 该线程池默认基于 JVM 可检测到的所有可用处理器来自动创建工作线程。算法核心在于：当某个工作线程完成了自身输入队列中的所有工作项后，
 * 它可以“窃取”其他队列中的工作项来执行。此设计的根本目的是在执行计算密集型任务时，跨处理器分发工作项，由此最大化所有可用处理器的利用率。
 * 基于多组 Output 的运行机制剖析：
 * 1. 任务轻量无阻塞场景（参考 Output: range(0, 10) 与 range(0, 20) 无 Nap 休眠）：
 * - 现象：无论提交 10 个还是 20 个任务，绝大多数任务均由少数线程（如 worker-1 和 worker-2）连续执行，并未触发 16 核全量并发。
 * - 物理机制：由于单行打印操作耗时极短，唤醒其他休眠物理核心并进行操作系统上下文切换的开销，
 * 大于单线程顺序处理本地队列的开销。这表明该线程池在任务极轻时，会倾向于复用活跃线程以避免底层调度损耗。
 * 2. 模拟计算/阻塞场景（参考 Output: range(0, 20) 附加 Nap(1) 休眠）：
 * - 现象 A（物理并发上限控制）：前 16 个任务被精准分配给 1 到 16 号 worker 线程。这证明当任务具备物理耗时后，
 * 线程池会按需拉起新的工作线程，但严格将其总数上限钳制在 CPU 的逻辑核心数（本机为 16）以内，以消除超量物理线程带来的上下文切换成本。
 * - 现象 B（工作窃取算法触发）：剩余的 4 个任务由优先结束休眠的 worker（如 13, 9, 12, 11）执行。这些线程在自身恢复空闲后，
 * 会立刻接手剩余任务。这些任务可能原本就分配在其独立的本地双端队列中，也可能是它们在检查到自身队列为空时，触发了工作窃取机制，
 * 从其他仍在阻塞状态的线程队列尾部获取而来。该结果直观展示了分离式本地队列在动态负载均衡中的实际作用。
 */
class ShowThread implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
        new Nap(1);
    }
}

public class WorkStealingPool {
    public static void main(String[] args) throws InterruptedException {
        System.out.println(Runtime.getRuntime().availableProcessors());

        // static ExecutorService newWorkStealingPool()
        // 创建一个维护足够线程以支持目标并行级别的线程池，默认并行级别等于系统可用处理器的数量。
        // 该线程池基于工作窃取 (Work Stealing) 算法，内部为每个工作线程分配独立的本地双端队列。
        // 当某个线程提前完成自身队列中的任务时，会主动尝试从其他繁忙线程的队列尾部窃取任务来执行。
        // 这种调度策略通过匹配物理核心数来减少上下文切换开销，并通过分散任务队列来降低全局锁竞争，从而最大化多核处理器的利用率。
        // 在执行计算密集型 (CPU-bound) 任务时，通常比传统的共享单队列线程池具备更高的并发吞吐量。
        ExecutorService exec = Executors.newWorkStealingPool();
        IntStream.range(0, 20)
                .mapToObj(n -> new ShowThread())
                .forEach(exec::execute);

        // 1. void shutdown(); 向线程池发送平滑关闭的异步信号，改变其生命周期状态。底层行为：
        // - 状态流转：调用瞬间，线程池状态立刻从 RUNNING（运行中）切换为 SHUTDOWN（关闭中）。
        // - 任务处理：立即拒绝接收任何新提交的任务（若继续提交将抛出 RejectedExecutionException），
        //   但后台工作线程会继续存活，直到将队列中已积压的存量任务全部执行完毕。
        // - 非阻塞特性：此方法仅仅是修改状态标志位，瞬间执行完毕，绝对不会阻塞调用它的主线程。
        // - 架构意义：对于使用“用户线程”的传统线程池（如 CachedThreadPool），
        //   若不调用此方法，线程池将永久处于 RUNNING 状态，导致 JVM 永远无法退出。
        //   对于 WorkStealingPool（默认使用守护线程），虽然主线程结束后 JVM 会强制退出并杀死守护线程，
        //   但为确保状态机正确流转及任务安全收尾，仍需作为标准范式调用。
        // exec.shutdown();

        // 2. boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException
        // 作用：阻塞当前调用线程（如主线程），直到线程池状态最终流转为 TERMINATED（彻底终止），或达到设定的超时时间。
        // 注意：该方法仅作为状态观察者执行物理阻塞等待，本身不具备向线程池发送关闭指令的能力，必须与 shutdown() 配合使用。
        //
        // 解除阻塞的三种触发条件及底层状态机逻辑：
        // 1. 彻底（正常）终止（返回 true）：
        //    必须在已调用 shutdown() 的前提下发生。此时线程池切换为 SHUTDOWN 状态，当所有子任务在设定的超时时间前执行完毕后，
        //    内部状态自动推演为 TERMINATED。该方法监测到 TERMINATED 状态后，会立即提前解除调用线程的阻塞。
        // 2. 到达超时时间（返回 false）：
        //    当前线程等待了传入的指定时长，但线程池仍未彻底关闭。
        //    [重要状态陷阱]：若在调用此方法前未调用 shutdown()，线程池将一直维持在 RUNNING（运行中）状态。
        //    此时，即使所有子任务在极短时间内全部执行完毕，该方法也无法监测到 TERMINATED 状态，
        //    从而会强制调用线程阻塞至设定的超时时间耗尽，并最终返回 false。
        // 3. 线程被中断（抛出 InterruptedException）：
        //    调用线程在阻塞等待期间接收到来自外部的中断信号。
        //
        // 返回值说明：
        // - true：解除阻塞时，线程池已完全关闭（处于 TERMINATED 状态）。
        // - false：因到达规定时间而停止等待，此时线程池本身可能仍在运行。
        //
        // 工程实践与标准范式：
        // 若业务需求为“主线程无限期等待所有子任务彻底执行完毕”，通常的规范写法是先调用 shutdown()，
        // 随后传入极大的时间值（如 exec.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);）。
        //
        // 针对 WorkStealingPool 测试用例的特殊说明：WorkStealingPool 内部默认创建的均为守护线程（Daemon Thread）。
        // 如果 awaitTermination 设置了较短的有限时长（如 1 秒）且返回了 false，主线程将解除阻塞并继续向下执行直至结束。
        // 主线程死亡后，JVM 若监测到系统中仅剩余守护线程，会立刻强制退出进程，底层的守护线程也会随之被直接终止。
        // 此处的有限阻塞，仅是为了给守护线程预留出完成打印任务的物理时间。
        System.out.println(exec.awaitTermination(5, TimeUnit.SECONDS));
        // System.out.println(exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS));
        System.out.println("hello");
    }
}
/* Output: range(0, 10) & no Nap(1);
16
ForkJoinPool-1-worker-1
ForkJoinPool-1-worker-1
ForkJoinPool-1-worker-1
ForkJoinPool-1-worker-1
ForkJoinPool-1-worker-1
ForkJoinPool-1-worker-1
ForkJoinPool-1-worker-1
ForkJoinPool-1-worker-1
ForkJoinPool-1-worker-1
ForkJoinPool-1-worker-2
false
hello
 */
/* Output: range(0, 20) & no Nap(1);
16
ForkJoinPool-1-worker-1
ForkJoinPool-1-worker-1
ForkJoinPool-1-worker-1
ForkJoinPool-1-worker-1
ForkJoinPool-1-worker-1
ForkJoinPool-1-worker-1
ForkJoinPool-1-worker-1
ForkJoinPool-1-worker-1
ForkJoinPool-1-worker-1
ForkJoinPool-1-worker-1
ForkJoinPool-1-worker-1
ForkJoinPool-1-worker-1
ForkJoinPool-1-worker-1
ForkJoinPool-1-worker-1
ForkJoinPool-1-worker-1
ForkJoinPool-1-worker-2
ForkJoinPool-1-worker-2
ForkJoinPool-1-worker-2
ForkJoinPool-1-worker-3
ForkJoinPool-1-worker-1
false
hello
 */
/* Output: range(0, 20) & Nap(1);
16
ForkJoinPool-1-worker-1
ForkJoinPool-1-worker-2
ForkJoinPool-1-worker-3
ForkJoinPool-1-worker-4
ForkJoinPool-1-worker-5
ForkJoinPool-1-worker-6
ForkJoinPool-1-worker-7
ForkJoinPool-1-worker-8
ForkJoinPool-1-worker-9
ForkJoinPool-1-worker-10
ForkJoinPool-1-worker-11
ForkJoinPool-1-worker-12
ForkJoinPool-1-worker-13
ForkJoinPool-1-worker-14
ForkJoinPool-1-worker-15
ForkJoinPool-1-worker-16
ForkJoinPool-1-worker-13
ForkJoinPool-1-worker-9
ForkJoinPool-1-worker-12
ForkJoinPool-1-worker-11
false
hello
 */
