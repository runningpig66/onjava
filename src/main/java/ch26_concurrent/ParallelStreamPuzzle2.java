package ch26_concurrent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 3月1日 周日
 * @time 20:31
 * P.225 §5.7 并行流 §5.7.2 parallel() 和 limit() 的作用
 * <p>
 * 本类演示了在使用并行流 (parallel) 配合无界生成器 (Stream.generate) 和截断操作 (limit) 时，底层框架的调度特征与并发陷阱。
 * 一、 原书核心结论：随机输出与过度调用
 * 1. 贪婪预取导致过度调用：当让无限流以并行方式生成时，实际是在让所有的线程都尽可能调用 get() 方法。
 * 底层分块大小由内部实现决定，这导致实际调用次数远超 limit() 设定的需求（原书测试甚至超额 1200 多次）。
 * 2. 强求并行与截断的代价：基本上，同时使用 parallel() 和 limit() 就等于在请求随机的输出。
 * 这是一个不可控的组合操作，仅适用于对结果顺序完全无要求的特殊场景。
 * 二、 延伸技术发现：复合操作的竞态条件 (Compound Actions)
 * 原书作者本意是使用线程安全的 ConcurrentLinkedDeque 和 AtomicInteger 来规避竞态条件。
 * 但通过实际测试输出（日志记录中出现大量相同旧值），揭示了另一个隐蔽的并发陷阱：
 * 连续调用两个独立的线程安全方法（先读取并记录日志，再执行原子递增），在方法整体级别依然不具备原子性。
 * 这导致了数据获取与日志记录之间产生了状态割裂。
 */
public class ParallelStreamPuzzle2 {
    public static final Deque<String> trace = new ConcurrentLinkedDeque<>();

    static class IntGenerator implements Supplier<Integer> {
        private AtomicInteger current = new AtomicInteger();

        @Override
        public Integer get() {
            // 场景一：原书默认实现（非原子的复合操作）
            // 典型输出示例 (List): [5, 0, 6, 8, 2, 1, 7, 3, 9, 4] (结果唯一，但处于无序状态)
            // 典型输出示例 (Trace日志): 出现大量重复的初始值记录（如多行 "0: ForkJoinPool..."）
            // 现象分析与底层原理：
            // 1. 复合操作导致状态记录不一致
            //    虽然 ConcurrentLinkedDeque 和 AtomicInteger 自身的方法调用是线程安全的，
            //    但在本方法中，“读取并记录日志”（第一行）和“原子递增并返回”（第二行）是两个独立的执行步骤。
            //    由于这两个步骤在方法级别缺乏同步锁保护，它们在整体上不具备原子性，从而暴露了竞态条件的时间窗口。
            // 2. 日志中出现大量重复值的成因 (并发读取)
            //    当并行流底层的 Fork/Join 框架瞬间唤醒多个工作线程时，它们极易在同一微观时间切片内并发执行到
            //    第一行代码 `current.get()`。在没有任何线程向下推进执行递增操作之前，这些并发线程均从主内存中
            //    读取到了相同的当前状态（如初始值 0），并分别将该重复值写入 trace 队列。
            // 3. 最终收集元素唯一的成因 (硬件级 CAS 机制)
            //    进入第二行代码 `current.getAndIncrement()` 时，AtomicInteger 底层依赖硬件级别的
            //    CAS (Compare-And-Swap) 机制保障原子性。该机制强制所有并发线程在此操作上串行排队。
            //    因此，尽管各线程在第一步记录了相同的旧值，它们在第二步向流的局部缓冲区提交时，
            //    必定能获取并返回严格递增、不重复的唯一数值。
            //    （注：最终收集到 List 中的乱序与跳号，源于无序流的底层分块提交与限流截断机制，而非计算重复。）
            trace.add(current.get() + ": " + Thread.currentThread().getName());
            return current.getAndIncrement();

            // 场景二：改进实现（消除复合操作的竞态条件）
            // 典型输出示例 (List): [2, 18, 21, 4, 17, 5, 16, 19, 20, 22] (结果唯一，处于无序状态)
            // 典型输出示例 (Trace日志): 记录的序号严格唯一，与底层实际发生的原子递增次数完全对应，无重复值。
            // 现象分析与底层原理：
            // 1. 提取局部变量以保证状态一致性
            //    通过将 `current.getAndIncrement()` 的单次原子操作结果赋值给局部变量 `strictlyUniqueValue`，
            //    程序确保了向并发队列 `trace` 写入的日志数值，与最终向流框架提交的返回值是完全相同的副本。
            //    这种设计消除了原代码中两次独立访问共享状态之间的时间差，避免了复合操作带来的竞态条件。
            // 2. 局部变量的线程隔离特性
            //    局部变量 `strictlyUniqueValue` 分配在当前执行线程的私有栈帧 (Stack Frame) 中。
            //    一旦原子递增完成并完成赋值，该变量的值便与其他并发线程彻底物理隔离。即使当前线程在执行
            //    `trace.add()` 之前被操作系统挂起（上下文切换），其持有的数值也不会被外部篡改或覆盖。
            // 3. 乱序日志的本质 (线程调度的时间差)
            //    在此改进版本中，`trace` 文件中记录的序号不再出现重复，但排列顺序依然呈现乱序特征。
            //    这准确反映了多线程环境下操作系统调度的随机性：例如线程 A 可能先完成了原子自增获得了较小序号（如 2），
            //    但在执行 `trace.add()` 入队操作前失去了 CPU 时间片；而线程 B 获得了较大序号（如 18）
            //    却率先完成了日志写入。这证明了数据获取的原子性可以保障数值准确唯一，但无法保障并发执行动作的全局顺序
            /*// 只调用一次原子操作，拿到唯一的值
            int strictlyUniqueValue = current.getAndIncrement();
            // 将这个唯一的值写入日志
            trace.add(strictlyUniqueValue + ": " + Thread.currentThread().getName());
            // 返回同一个唯一的值
            return strictlyUniqueValue;*/
        }
    }

    public static void main(String[] args) throws IOException {
        List<Integer> x = Stream.generate(new IntGenerator())
                //.peek(e -> System.out.println(e + ": " + Thread.currentThread().getName()))
                .limit(10)
                .parallel()
                .collect(Collectors.toList());
        System.out.println(x);
        Files.write(Paths.get("src/main/java/ch26_concurrent/PSP2.txt"), trace);
    }
}
/* Output:
[2, 9, 6, 7, 0, 8, 5, 3, 1, 4]
 */
