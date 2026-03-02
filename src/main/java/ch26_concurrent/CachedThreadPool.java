package ch26_concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * @author runningpig66
 * @date 3月2日 周一
 * @time 4:39
 * P.232 §5.8 创建和运行任务 §5.8.2 使用更多的线程
 * <p>
 * 本类演示了 CachedThreadPool 的运行机制。该线程池的调度策略是不进行任务排队，转而通过创建新线程来处理即时涌入的任务。
 * 适用场景为处理大量且执行时间极短的异步任务。如果瞬间涌入大量耗时任务，会无限创建新物理线程，耗尽 CPU 和内存，导致 OOM 或宕机。
 */
public class CachedThreadPool {
    public static void main(String[] args) {
        // 创建一个可缓存的线程池。其底层实现为 ThreadPoolExecutor，参数配置为：
        // 常驻（核心）线程数为 0，最大线程数为 Integer.MAX_VALUE，空闲存活时间为 60 秒。
        // 其主要特征是使用 SynchronousQueue（容量为 0 的同步移交队列）作为任务传递容器。
        // SynchronousQueue 容量为 0，不具备存储排队功能。当提交新任务时，如果有 60 秒内存活的闲置线程，
        // 则交由其复用执行；若所有存活线程都在忙碌，线程池会直接创建一个新的物理线程接手任务。
        ExecutorService exec = Executors.newCachedThreadPool();
        IntStream.range(0, 10)
                .mapToObj(NapTask::new)
                // 非阻塞式提交任务。主线程在极短的时间内连续提交了 10 个任务。
                // 由于每个 NapTask 需要休眠 0.1 秒，执行耗时远大于主线程的提交速度。
                // 导致在前一个线程释放之前，新任务已经到达。由于 SynchronousQueue 无法缓存排队，
                // 线程池被迫连续创建 10 个不同的物理线程来并行处理这些任务。
                // 控制台输出中出现 pool-1-thread-1 到 thread-10，客观反映了这一线程递增现象。
                .forEach(exec::execute);
        // 停止接收新任务，变更线程池状态为 SHUTDOWN。
        // 现有线程在执行完当前任务后，会在闲置 60 秒后触发超时销毁机制，并从工作线程集合中移除。
        exec.shutdown();
    }
}
/* Output:
NapTask[8] pool-1-thread-9
NapTask[7] pool-1-thread-8
NapTask[3] pool-1-thread-4
NapTask[5] pool-1-thread-6
NapTask[1] pool-1-thread-2
NapTask[6] pool-1-thread-7
NapTask[0] pool-1-thread-1
NapTask[9] pool-1-thread-10
NapTask[4] pool-1-thread-5
NapTask[2] pool-1-thread-3
 */
