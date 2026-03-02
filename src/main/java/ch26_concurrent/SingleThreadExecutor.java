package ch26_concurrent;

import onjava.Nap;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * @author runningpig66
 * @date 3月2日 周一
 * @time 2:23
 * P.230 §5.8 创建和运行任务 §5.8.1 Task 和 Executor
 * <p>
 * 本类演示了 SingleThreadExecutor 的基本运行机制。
 * 核心逻辑体现为任务提交与任务执行的分离：主线程负责非阻塞式提交，后台单线程负责串行执行。
 * 工作线程的唤醒与执行是由 execute() 方法即时触发的，并非依赖 shutdown() 方法来启动执行。
 */
public class SingleThreadExecutor {
    public static void main(String[] args) {
        // static ExecutorService newSingleThreadExecutor() 创建一个单线程的 ExecutorService。
        // 其底层实现为 ThreadPoolExecutor，核心线程数与最大线程数均锁定为 1，并使用无界的 LinkedBlockingQueue 作为任务队列。
        // 该配置确保了所有提交的任务会严格按照提交顺序（FIFO）串行处理。如果任务堆积过快，队列会无限膨胀，导致 OOM（内存溢出）。
        ExecutorService exec = Executors.newSingleThreadExecutor();
        IntStream.range(0, 10)
                .mapToObj(NapTask::new)
                // void execute(Runnable command); 非阻塞式提交任务。
                // 调用此方法时，若工作线程空闲，会立即唤醒线程接手执行该任务；
                // 若工作线程正在忙碌，任务则被推入底层的 LinkedBlockingQueue 等待。
                // 主线程完成入队动作后立即返回并继续执行下一行代码，不会阻塞等待任务的实际运算或睡眠结束。
                .forEach(exec::execute);
        System.out.println("All tasks submitted");
        // void shutdown(); 停止接收新任务，平滑关闭线程池接收通道。线程池状态更改为 SHUTDOWN。
        // 此后若再向 exec 提交新任务，将抛出 RejectedExecutionException 异常。注意：该方法仅用于变更生命周期状态。
        // 调用后，后台的单工作线程不会立即被销毁，它会继续存活并消耗队列中已排队的剩余存量任务。
        exec.shutdown();
        // boolean isTerminated(); 检查线程池的清理与销毁流程是否彻底完成。
        // 该方法通常位于 shutdown() 之后，配合 while 循环使用。
        // 只有当接收通道已关闭，且队列中所有存量任务执行完毕、后台工作单线程安全退出时，才会返回 true。
        // 此处的 while 循环起到轮询监控的作用，查询并等待后台任务收尾。
        while (!exec.isTerminated()) {
            System.out.println(Thread.currentThread().getName() + " awaiting termination");
            new Nap(0.1);
        }
    }
}
/* Output:
All tasks submitted
main awaiting termination
main awaiting termination
NapTask[0] pool-1-thread-1
main awaiting termination
NapTask[1] pool-1-thread-1
main awaiting termination
NapTask[2] pool-1-thread-1
main awaiting termination
NapTask[3] pool-1-thread-1
main awaiting termination
NapTask[4] pool-1-thread-1
main awaiting termination
NapTask[5] pool-1-thread-1
main awaiting termination
NapTask[6] pool-1-thread-1
main awaiting termination
NapTask[7] pool-1-thread-1
main awaiting termination
NapTask[8] pool-1-thread-1
main awaiting termination
NapTask[9] pool-1-thread-1
 */
