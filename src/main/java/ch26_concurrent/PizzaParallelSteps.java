package ch26_concurrent;

import onjava.Timer;

import java.util.stream.IntStream;

/**
 * @author runningpig66
 * @date 3月7日 周六
 * @time 21:52
 * P.271 §5.13 工作量、复杂性、成本
 * <p>
 * PizzaStreams.java 是在 forEach() 中完成所有的工作的。如果我们将独立的步骤 map 起来，又会发生什么变化呢？
 * 答案是 “没有变化”。想想也并不奇怪，因为每个比萨都需要所有的步骤按顺序执行，
 * 所以并没有机会让我们可以像在 PizzaParallelSteps.java 中那样，通过分离各个步骤来进一步提速。
 * <p>
 * 并行流流水线操作与数据并行模型分析：本测试类通过将 Pizza 的制作步骤拆分为多个连续的 map() 操作，
 * 揭示了 Java Stream API 的底层执行机制、性能边界，以及其并发模型的准确工程定义。
 * 1. 线程未切换的底层原理：流的操作融合
 * 在代码表象上，连续的 map() 调用似乎会将任务切割为多个独立阶段。但在物理执行层面，
 * 当 Stream 遇到终端操作 (forEach) 时，底层的执行引擎会将这一整串 map 逻辑“融合”为一个整体执行块。
 * - 执行流转：一旦 ForkJoinPool 的某个 worker 线程接管了某一个特定的 Pizza 对象数据，
 * 它就会“负责到底”，在当前线程上下文中连续执行从 roll() 到 box() 的所有操作。
 * - 架构优势：这种设计避免了元素在不同处理步骤之间流转时产生极其昂贵的线程上下文切换开销。
 * 2. 性能未提升的物理根源：状态依赖与关键路径 (Critical Path)
 * 尽管代码使用了 parallel() 并通过 map 进行了步骤细分，但总耗时并未减少。
 * - 垂直依赖限制：对于单个 Pizza 而言，其生命周期是一个严格的有限状态机，
 * 步骤之间存在绝对的先后因果依赖（必须先执行 roll 才能执行 sauce）。
 * - 关键路径法则：并行流解决的是“水平扩展”问题（同时处理多个 Pizza），但无法压缩单一对象的“垂直关键路径”。
 * 只要步骤中存在物理阻塞（如 Nap 休眠），完成单一数据实体的物理总耗时就是不可压缩的时间底线。
 * 3. 并发模型的严谨界定：同步数据并行
 * 对于 Stream.parallel()，其准确的并发工程术语为“基于同步调用的数据并行阻塞模型”。
 * - 为什么是“数据并行 (Data Parallelism)”而非“任务并发”：
 * 任务并发强调不同线程执行不同的业务逻辑。而此处的并行流，其本质是利用 IntStream.range() 在逻辑上生成一批数据池，
 * 随后将这些底层的“数据实体 (Pizza 对象)”分发给多个线程。所有参与的子线程，执行的都是完全相同的函数调用链。被切割和分配的是数据，而非行为。
 * - 为什么是“同步 (Synchronous)”：
 * 在 API 调用级别，主线程 (main) 在执行终端操作 forEach 时会被完全阻塞。它必须等待所有并发子任务处理完毕才能继续向下执行。
 * 同时，基于底层 Work Stealing（工作窃取）机制，处于等待状态的主线程也会直接参与到数据处理中。
 * 结论：优雅的函数式语法链（如连续的 map）无法改变底层物理阻塞的客观法则。对于包含强状态依赖及 I/O 阻塞特征的业务场景，
 * 基于同步数据并行的 Stream.parallel()并非合理的架构选择。要打破此类瓶颈，必须引入真正的“异步非阻塞任务模型”。
 */
public class PizzaParallelSteps {
    static final int QUANTITY = 5;

    public static void main(String[] args) {
        Timer timer = new Timer();
        IntStream.range(0, QUANTITY)
                .mapToObj(Pizza::new)
                .parallel()
                .map(Pizza::roll)
                .map(Pizza::sauce)
                .map(Pizza::cheese)
                .map(Pizza::toppings)
                .map(Pizza::bake)
                .map(Pizza::slice)
                .map(Pizza::box)
                .forEach(System.out::println);
        System.out.println(timer.duration());
    }
}
/* Output:
currentThread: [ForkJoinPool.commonPool-worker-1], Pizza 1: ROLLED
currentThread: [ForkJoinPool.commonPool-worker-2], Pizza 0: ROLLED
currentThread: [ForkJoinPool.commonPool-worker-3], Pizza 4: ROLLED
currentThread: [ForkJoinPool.commonPool-worker-4], Pizza 3: ROLLED
currentThread: [main], Pizza 2: ROLLED
currentThread: [ForkJoinPool.commonPool-worker-1], Pizza 1: SAUCED
currentThread: [ForkJoinPool.commonPool-worker-4], Pizza 3: SAUCED
currentThread: [ForkJoinPool.commonPool-worker-3], Pizza 4: SAUCED
currentThread: [ForkJoinPool.commonPool-worker-2], Pizza 0: SAUCED
currentThread: [main], Pizza 2: SAUCED
currentThread: [ForkJoinPool.commonPool-worker-3], Pizza 4: CHEESED
currentThread: [ForkJoinPool.commonPool-worker-2], Pizza 0: CHEESED
currentThread: [ForkJoinPool.commonPool-worker-1], Pizza 1: CHEESED
currentThread: [ForkJoinPool.commonPool-worker-4], Pizza 3: CHEESED
currentThread: [main], Pizza 2: CHEESED
currentThread: [ForkJoinPool.commonPool-worker-1], Pizza 1: TOPPED
currentThread: [ForkJoinPool.commonPool-worker-3], Pizza 4: TOPPED
currentThread: [ForkJoinPool.commonPool-worker-2], Pizza 0: TOPPED
currentThread: [ForkJoinPool.commonPool-worker-4], Pizza 3: TOPPED
currentThread: [main], Pizza 2: TOPPED
currentThread: [main], Pizza 2: BAKED
currentThread: [ForkJoinPool.commonPool-worker-3], Pizza 4: BAKED
currentThread: [ForkJoinPool.commonPool-worker-1], Pizza 1: BAKED
currentThread: [ForkJoinPool.commonPool-worker-2], Pizza 0: BAKED
currentThread: [ForkJoinPool.commonPool-worker-4], Pizza 3: BAKED
currentThread: [main], Pizza 2: SLICED
currentThread: [ForkJoinPool.commonPool-worker-3], Pizza 4: SLICED
currentThread: [ForkJoinPool.commonPool-worker-2], Pizza 0: SLICED
currentThread: [ForkJoinPool.commonPool-worker-4], Pizza 3: SLICED
currentThread: [ForkJoinPool.commonPool-worker-1], Pizza 1: SLICED
currentThread: [ForkJoinPool.commonPool-worker-3], Pizza 4: BOXED
currentThread: [ForkJoinPool.commonPool-worker-1], Pizza 1: BOXED
currentThread: [main], Pizza 2: BOXED
currentThread: [ForkJoinPool.commonPool-worker-4], Pizza 3: BOXED
currentThread: [ForkJoinPool.commonPool-worker-2], Pizza 0: BOXED
Pizza2: complete
Pizza4: complete
Pizza0: complete
Pizza1: complete
Pizza3: complete
1688
 */
