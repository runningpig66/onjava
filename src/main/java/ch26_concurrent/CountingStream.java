package ch26_concurrent;

import onjava.Timer;

import java.util.stream.IntStream;

/**
 * @author runningpig66
 * @date 3月3日 周二
 * @time 2:00
 * P.236 §5.8 创建和运行任务 §5.8.3 生成结果
 * {VisuallyInspectOutput}
 * <p>
 * 我们在这里对比一下 CountingStream 和 CachedThreadPool3 在解决“批量计算并求和”这一相同需求时的差异。作者所谓的“优雅”，体现在彻底消除了底层的样板代码和 API 的割裂感。
 * 1. 自动的生命周期管理
 * 在使用 ExecutorService 时，您必须手动创建线程池，并且在代码末尾严格调用 exec.shutdown() 来关闭它。如果忘记调用，JVM 将永远无法退出。
 * 而在 CountingStream 中，调用 parallel() 时，底层自动复用了 JVM 全局共享的 ForkJoinPool.commonPool()。您不需要去管理这个池的创建和销毁，框架在底层自动接管了资源分配。
 * 2. 移除冗余的 Future 包装
 * 正如我们在 CachedThreadPool3 讨论的，invokeAll() 的设计缺陷在于：它已经把主线程阻塞到所有任务完成了，却依然返回一个毫无意义的 List<Future> 包装类，迫使您必须遍历这个列表去调用 get()。
 * 在流式编程中，.map(CountingTask::call) 直接提取了计算的真实结果（Integer），并将其直接流入下一个 .reduce() 环节。数据的流转是纯粹的计算结果，去掉了 Future 这个没必要的“外壳”。
 * 3. 规避繁琐的异常处理
 * CachedThreadPool3 中为了调用 f.get()，作者不得不写了一个丑陋的 extractResult 辅助方法，仅仅是为了捕获 InterruptedException 和 ExecutionException。
 * 而在 CountingStream 中，由于没有了 Future.get() 的调用，这层由于底层 API 设计导致的强制异常捕获被完全去除了。
 * 4. 主线程的调度与参与度
 * 如果仔细观察控制台的输出，会发现一个非常有趣的现象："6 main 100"。在使用 ExecutorService 时，主线程是被死死“挂起”的，它除了等待，什么都不干。
 * 但在并行流中，框架会识别到主线程的阻塞状态，并将其直接安排参与底层子任务的计算，从而最大化压榨了 CPU 的可用计算力。
 * - 适用场景：如果当前的业务场景就是“必须等待一批任务全部并行计算完毕，拿到汇总结果才能往下走”（即同步阻塞的分散-聚集 Scatter-Gather 模式），
 * 请直接使用 IntStream.range().parallel()。代码量最少，语义也最清晰，只需将 parallel() 插入顺序操作中，一切就会自动以并发方式运行。
 * 如果您需要的是真正的异步交错（主线程提交任务后立即去干别的事情，不被挂起），原生的并行流则无法满足需求，需要引入现代异步工具（如 CompletableFuture）来解决。
 */
public class CountingStream {
    public static void main(String[] args) {
        System.out.println("[main] start...");
        Timer timer = new Timer();

        System.out.println(IntStream.range(0, 10)
                .parallel()
                .mapToObj(CountingTask::new)
                .map(CountingTask::call)
                .reduce(0, Integer::sum));

        System.out.println("[main] end. duration: " + timer.duration() + " microseconds");
    }
}
/* Output:
[main] start...
2 ForkJoinPool.commonPool-worker-1 100
3 ForkJoinPool.commonPool-worker-6 100
9 ForkJoinPool.commonPool-worker-7 100
7 ForkJoinPool.commonPool-worker-5 100
6 main 100
5 ForkJoinPool.commonPool-worker-9 100
4 ForkJoinPool.commonPool-worker-3 100
0 ForkJoinPool.commonPool-worker-8 100
1 ForkJoinPool.commonPool-worker-2 100
8 ForkJoinPool.commonPool-worker-4 100
1000
[main] end. duration: 4964 microseconds
 */
