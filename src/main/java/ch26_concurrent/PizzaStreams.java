package ch26_concurrent;

import onjava.Timer;

import java.util.stream.IntStream;

/**
 * @author runningpig66
 * @date 3月7日 周六
 * @time 20:52
 * P.270 §5.13 工作量、复杂性、成本
 * <p>
 * 如果你像这样制作了 5 个比萨，那么你会预料到耗时会变成 5 倍。但是如果你想要更快呢？可以从并行流的方式开始：
 * 现在我们用差不多制作 1 个比萨的时间，制作了 5 个比萨。试着移除行 [1]，可以验证如果不用并行就又变成了 5 倍耗时。
 * 还可以试着将 QUANTITY 改为 4、8、10、16 和 17，再看看区别，猜猜背后的原因是什么。
 * <p>
 * 并行流性能测试与底层硬件瓶颈分析：本测试揭示了 Java 8 并行流 (Parallel Streams) 在处理阻塞型任务时的底层调度机制及其局限性。
 * 1. 硬件核心数与批处理效应 (Batch Processing)
 * Java 的 parallel() 底层默认使用全局共享的 ForkJoinPool.commonPool()，其工作线程数配置为：
 * 系统的可用逻辑核心数 - 1。加上参与调度的 main 线程，系统最大允许的并发执行线程数正好等于物理逻辑核心数。
 * （注：在 8 核 16 线程的硬件环境下，如 Intel i9-9900K，该并发上限为 16）。
 * - 当任务数量 (QUANTITY) 处于 [1, 16] 区间时，所有任务被分配到 16 个物理线程中严格并行执行，总耗时为单次任务时间。
 * - 当任务数量达到 17 时，前 16 个任务瞬间占满所有线程资源。第 17 个任务因无可用线程，被迫进入等待队列（发生排队）。
 * 直至前一批次中任意线程执行完毕并被释放，第 17 个任务才会被接手处理。这种机制导致任务处理耗时在 17、33、49 等
 * 硬件核心数的倍数节点上，出现阶跃式的成倍增长。
 * 2. 核心矛盾：I/O 阻塞与线程饥饿 (Thread Starvation)
 * 示例代码中的 Nap() 用于真实模拟生产环境中的 I/O 阻塞（如网络请求、数据库读写）。
 * 并行流 (Parallel Streams) 的底层设计初衷是为了加速“纯 CPU 密集型计算”。然而，当它被用于处理 I/O 阻塞型任务时，
 * 会暴露出致命缺陷：线程在 I/O 等待期间，CPU 实际上处于闲置状态，但该物理线程依然被死死占用、无法释放。
 * 这种资源占用的方式导致了极大的性能浪费，并直接引发了超出核心数之外任务的“线程饥饿”。
 * 3. 架构演进方向：真正的异步非阻塞模型
 * 面对 I/O 密集型的高并发场景，仅仅依靠增加硬件物理核心数是存在物理极限的。
 * 现代高并发架构的标准解决方案是引入异步非阻塞编程模型（如 Java 8 引入的 CompletableFuture）。
 * 异步模型的核心优势在于：当任务遇到 I/O 阻塞（休眠或等待响应）时，能够主动将底层的物理线程“让出”给其他就绪任务
 * （如本例中的第 17 个任务）使用。通过将“任务的执行流”与“底层的物理线程”解耦，彻底打破物理逻辑核心数对系统并发吞吐量的硬性限制。
 */
public class PizzaStreams {
    static final int QUANTITY = 5;

    public static void main(String[] args) {
        Timer timer = new Timer();
        IntStream.range(0, QUANTITY)
                .mapToObj(Pizza::new)
                .parallel() // [1]
                .forEach(za -> {
                    while (!za.complete()) {
                        za.next();
                    }
                });
        System.out.println(timer.duration());
    }
}
/* Output:
currentThread: [ForkJoinPool.commonPool-worker-3], Pizza 4: ROLLED
currentThread: [ForkJoinPool.commonPool-worker-1], Pizza 1: ROLLED
currentThread: [ForkJoinPool.commonPool-worker-2], Pizza 0: ROLLED
currentThread: [main], Pizza 2: ROLLED
currentThread: [ForkJoinPool.commonPool-worker-4], Pizza 3: ROLLED
currentThread: [ForkJoinPool.commonPool-worker-1], Pizza 1: SAUCED
currentThread: [ForkJoinPool.commonPool-worker-2], Pizza 0: SAUCED
currentThread: [main], Pizza 2: SAUCED
currentThread: [ForkJoinPool.commonPool-worker-4], Pizza 3: SAUCED
currentThread: [ForkJoinPool.commonPool-worker-3], Pizza 4: SAUCED
currentThread: [ForkJoinPool.commonPool-worker-4], Pizza 3: CHEESED
currentThread: [ForkJoinPool.commonPool-worker-3], Pizza 4: CHEESED
currentThread: [main], Pizza 2: CHEESED
currentThread: [ForkJoinPool.commonPool-worker-2], Pizza 0: CHEESED
currentThread: [ForkJoinPool.commonPool-worker-1], Pizza 1: CHEESED
currentThread: [ForkJoinPool.commonPool-worker-1], Pizza 1: TOPPED
currentThread: [ForkJoinPool.commonPool-worker-2], Pizza 0: TOPPED
currentThread: [ForkJoinPool.commonPool-worker-4], Pizza 3: TOPPED
currentThread: [ForkJoinPool.commonPool-worker-3], Pizza 4: TOPPED
currentThread: [main], Pizza 2: TOPPED
currentThread: [main], Pizza 2: BAKED
currentThread: [ForkJoinPool.commonPool-worker-4], Pizza 3: BAKED
currentThread: [ForkJoinPool.commonPool-worker-1], Pizza 1: BAKED
currentThread: [ForkJoinPool.commonPool-worker-2], Pizza 0: BAKED
currentThread: [ForkJoinPool.commonPool-worker-3], Pizza 4: BAKED
currentThread: [ForkJoinPool.commonPool-worker-1], Pizza 1: SLICED
currentThread: [ForkJoinPool.commonPool-worker-4], Pizza 3: SLICED
currentThread: [main], Pizza 2: SLICED
currentThread: [ForkJoinPool.commonPool-worker-2], Pizza 0: SLICED
currentThread: [ForkJoinPool.commonPool-worker-3], Pizza 4: SLICED
currentThread: [ForkJoinPool.commonPool-worker-1], Pizza 1: BOXED
currentThread: [main], Pizza 2: BOXED
currentThread: [ForkJoinPool.commonPool-worker-4], Pizza 3: BOXED
currentThread: [ForkJoinPool.commonPool-worker-2], Pizza 0: BOXED
currentThread: [ForkJoinPool.commonPool-worker-3], Pizza 4: BOXED
1695
 */
