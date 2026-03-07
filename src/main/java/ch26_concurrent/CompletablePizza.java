package ch26_concurrent;

import onjava.Timer;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author runningpig66
 * @date 3月7日 周六
 * @time 23:22
 * P.272 §5.13 工作量、复杂性、成本
 * <p>
 * 我们可以用 CompletableFuture 来重写该示例：
 * 并行流和 CompletableFuture 是 Java 并发工具中最发达的技术。不论何时你都应该优先选择其中之一。
 * 并行流方案最适合解决无脑并行类型问题，即那种很容易将数据拆分成无差别、易处理的片段来处理的问题
 * （要自己实现这部分的话，你得先撸起袖子好好钻研 Spliterator 的文档）。
 * CompletableFuture 处理的工作片段最好是各不相同的，这样效果最好。CompletableFuture 看起来更像是面向任务的，而不是面向数据的。
 * <p>
 * 异步框架执行同步阻塞任务的性能陷阱与并发架构选型总结：本测试类作为第五章的最终示例，
 * 通过引入 CompletableFuture (CF) 重构线性阻塞任务，揭示了异步非阻塞框架在不匹配的业务场景下所暴露的物理瓶颈与过度设计成本。
 * 1. 线程池调度差异与物理并发上限 (Main 线程闲置)
 * 在 16 核逻辑线程的硬件环境下，Stream.parallel() 基于 Work Stealing 机制会让主线程 (main) 参与计算，从而达到 16 个并发容量。
 * 而在本例的 CF 链式调用中，主线程在执行由 map 组成的派发逻辑后，仅负责等待结果，不参与底层 ForkJoinPool 的实际运算。
 * 这导致 CF 的实际工作线程数为物理核心数减 1（即 15 个 worker 线程）。
 * 因此，当 QUANTITY 达到 16 时，任务便会因无空闲线程而进入排队状态，耗时出现阶跃式翻倍。
 * 2. 伪异步陷阱：异步派发与同步物理阻塞的冲突
 * CompletableFuture 的核心优势在于基于事件回调的“异步非阻塞”模型。然而，本例中的 Pizza 制作步骤内部使用了 Nap (底层为 Thread.sleep)，
 * 这是一种典型的操作系统级别的同步物理阻塞。
 * 当 worker 线程执行异步任务时遇到该物理阻塞，该线程的系统资源会被死死占用。这意味着 CF 虽然在 API 层面实现了异步派发，
 * 但在物理执行层面依然退化为同步阻塞模型，完全无法释放休眠线程去处理其他数据实体，因此未能打破物理核心数的并发瓶颈。
 * - 异步非阻塞架构重构方案：若需彻底突破上述物理线程瓶颈，必须将底层耗时操作替换为基于事件驱动的非阻塞 API（如 Java 9 引入的 CompletableFuture.delayedExecutor）。
 * 在该执行模型下，工作线程在发起延迟任务后不会触发系统级挂起，而是仅注册回调函数并随即归还至线程池，以便处理其他就绪任务。
 * 待定时器或 I/O 事件触发时，底层调度器会唤醒可用线程接续执行后续流水线。这种将“逻辑控制流”与“底层物理线程”深度解耦的机制，是提升系统并发吞吐量与资源利用率的工程标准。
 * 3. 滥用 thenApplyAsync 的上下文切换开销
 * 对于具有严格状态依赖的线性流程（揉面 -> 抹酱 -> 烘烤等），使用 thenApplyAsync 会导致流水线的每一个极小步骤都被作为独立任务重新打回线程池队列。
 * 这不仅破坏了单线程执行的局部性原理（如 Stream 的操作融合 Loop Fusion），还引发了海量的跨线程流转与上下文切换开销。
 * 这种强行的异步切分非但不能压缩单一对象的关键路径耗时，反而增加了系统的运行成本与调度复杂度。
 * 总结：工作量、复杂性与成本——并发技术选型必须严格匹配业务特征，避免为了使用高级 API 而导致过度设计：
 * 并行流 (Parallel Streams)：最适合解决“无脑并行”类型的问题，即数据极易拆分且处理逻辑高度同质化、易处理的场景。在表现形式上，并行流的解决方案通常看起来更清晰，应作为首选尝试。
 * CompletableFuture：是面向任务的 (Task-oriented) 而非面向数据的，其处理的工作片段最好是各不相同的、具备真正异步非阻塞 I/O 特征的复杂编排场景。
 * 成本警示：当面对简单的线性或强阻塞型问题时，强行引入 CF 试图进一步优化，其耗费的开发成本和系统维护精力可能很快就会远远超过优化的实际好处。
 */
public class CompletablePizza {
    static final int QUANTITY = 5;

    public static CompletableFuture<Pizza> makeCF(Pizza za) {
        return CompletableFuture.completedFuture(za)
                .thenApplyAsync(Pizza::roll)
                .thenApplyAsync(Pizza::sauce)
                .thenApplyAsync(Pizza::cheese)
                .thenApplyAsync(Pizza::toppings)
                .thenApplyAsync(Pizza::bake)
                .thenApplyAsync(Pizza::slice)
                .thenApplyAsync(Pizza::box, CompletableFuture.delayedExecutor(0, TimeUnit.MILLISECONDS));
    }

    public static void show(CompletableFuture<Pizza> cf) {
        try {
            System.out.println(cf.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Timer timer = new Timer();
        List<CompletableFuture<Pizza>> pizzas = IntStream.range(0, QUANTITY)
                .mapToObj(Pizza::new)
                .map(CompletablePizza::makeCF)
                // .collect(Collectors.toList());
                .toList();
        System.out.println(timer.duration());
        pizzas.forEach(CompletablePizza::show);
        System.out.println(timer.duration());
    }
}
/* Output:
13
currentThread: [ForkJoinPool.commonPool-worker-4], Pizza 3: ROLLED
currentThread: [ForkJoinPool.commonPool-worker-5], Pizza 4: ROLLED
currentThread: [ForkJoinPool.commonPool-worker-2], Pizza 1: ROLLED
currentThread: [ForkJoinPool.commonPool-worker-3], Pizza 2: ROLLED
currentThread: [ForkJoinPool.commonPool-worker-1], Pizza 0: ROLLED
currentThread: [ForkJoinPool.commonPool-worker-2], Pizza 1: SAUCED
currentThread: [ForkJoinPool.commonPool-worker-5], Pizza 2: SAUCED
currentThread: [ForkJoinPool.commonPool-worker-4], Pizza 3: SAUCED
currentThread: [ForkJoinPool.commonPool-worker-6], Pizza 4: SAUCED
currentThread: [ForkJoinPool.commonPool-worker-7], Pizza 0: SAUCED
currentThread: [ForkJoinPool.commonPool-worker-4], Pizza 3: CHEESED
currentThread: [ForkJoinPool.commonPool-worker-5], Pizza 2: CHEESED
currentThread: [ForkJoinPool.commonPool-worker-6], Pizza 4: CHEESED
currentThread: [ForkJoinPool.commonPool-worker-7], Pizza 0: CHEESED
currentThread: [ForkJoinPool.commonPool-worker-2], Pizza 1: CHEESED
currentThread: [ForkJoinPool.commonPool-worker-4], Pizza 3: TOPPED
currentThread: [ForkJoinPool.commonPool-worker-6], Pizza 4: TOPPED
currentThread: [ForkJoinPool.commonPool-worker-2], Pizza 1: TOPPED
currentThread: [ForkJoinPool.commonPool-worker-7], Pizza 0: TOPPED
currentThread: [ForkJoinPool.commonPool-worker-5], Pizza 2: TOPPED
currentThread: [ForkJoinPool.commonPool-worker-2], Pizza 1: BAKED
currentThread: [ForkJoinPool.commonPool-worker-7], Pizza 0: BAKED
currentThread: [ForkJoinPool.commonPool-worker-5], Pizza 2: BAKED
currentThread: [ForkJoinPool.commonPool-worker-4], Pizza 3: BAKED
currentThread: [ForkJoinPool.commonPool-worker-6], Pizza 4: BAKED
currentThread: [ForkJoinPool.commonPool-worker-1], Pizza 0: SLICED
currentThread: [ForkJoinPool.commonPool-worker-5], Pizza 2: SLICED
currentThread: [ForkJoinPool.commonPool-worker-4], Pizza 3: SLICED
currentThread: [ForkJoinPool.commonPool-worker-6], Pizza 4: SLICED
currentThread: [ForkJoinPool.commonPool-worker-2], Pizza 1: SLICED
currentThread: [ForkJoinPool.commonPool-worker-2], Pizza 1: BOXED
currentThread: [ForkJoinPool.commonPool-worker-5], Pizza 4: BOXED
currentThread: [ForkJoinPool.commonPool-worker-1], Pizza 3: BOXED
currentThread: [ForkJoinPool.commonPool-worker-6], Pizza 0: BOXED
currentThread: [ForkJoinPool.commonPool-worker-4], Pizza 2: BOXED
Pizza0: complete
Pizza1: complete
Pizza2: complete
Pizza3: complete
Pizza4: complete
1685
 */
