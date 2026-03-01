package ch26_concurrent;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author runningpig66
 * @date 3月2日 周一
 * @time 1:06
 * P.227 §5.7 并行流 §5.7.2 parallel() 和 limit() 的作用
 * <p>
 * 本类演示了有界数据源在并行流中的执行表现，作为对无界生成器并行陷阱的替代与改进方案。运行机制与技术要点总结：
 * 1. 有界流的预取控制
 * 程序使用 IntStream.range(0, 30) 替代了 Stream.generate()，提供了明确的数据边界。
 * 在这种有界状态下，底层 Fork/Join 框架的任务分配具有确定性，避免了无界流配合 limit()
 * 时产生的大量盲目预取行为。生成的数据量和线程调用次数均被限制在合理范围内。
 * 2. 状态观察与 peek() 的使用
 * 通过引入 peek() 方法，可以在不改变流中元素传递的情况下，对每个元素执行中间操作。
 * 此处用于打印处理该元素的线程名称，以验证并行流的实际工作分配。
 * （注：System.out.println 涉及 I/O 阻塞，在实际测试中会干扰底层线程的自然调度策略）。
 * 3. 基本类型的装箱操作
 * 管道中加入了 boxed() 方法，其作用是将原始类型 int 流装箱转换为对象类型 Integer 流，
 * 以便满足末端收集器 Collectors.toList() 对引用类型的要求。
 * 4. 并行流的性能适用场景
 * 对于小批量或计算步骤简单的数据序列，启用并行流引发的线程上下文切换开销，往往大于
 * 并行计算带来的收益。通常仅在单次计算开销较高、或数据量极大时，才考虑引入并行化设计。
 */
public class ParallelStreamPuzzle3 {
    public static void main(String[] args) {
        List<Integer> x = IntStream.range(0, 30)
                .peek(e -> System.out.println(e + ": " + Thread.currentThread().getName()))
                .limit(10)
                .parallel()
                .boxed()
                .collect(Collectors.toList());
        System.out.println(x);
    }
}
/* Output:
3: ForkJoinPool.commonPool-worker-9
1: ForkJoinPool.commonPool-worker-2
0: ForkJoinPool.commonPool-worker-5
2: ForkJoinPool.commonPool-worker-8
7: ForkJoinPool.commonPool-worker-7
9: ForkJoinPool.commonPool-worker-4
5: ForkJoinPool.commonPool-worker-6
4: ForkJoinPool.commonPool-worker-1
8: main
6: ForkJoinPool.commonPool-worker-3
[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
 */
