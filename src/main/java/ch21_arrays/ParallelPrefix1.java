package ch21_arrays;

import onjava.ArrayShow;
import onjava.Count;

import java.util.Arrays;

/**
 * @author runningpig66
 * @date 2026-03-28
 * @time 17:13
 * P.804 §21.19 用 parallelPrefix() 进行累积计算
 * <p>
 * 并不存在 prefix() 方法；只有一个 parallelPrefix() 方法。这很像 Stream 类中的 reduce() 方法：
 * 它会对前一个元素和当前元素进行操作，将结果放入当前元素的位置：
 * <p>
 * 这里我们对数组使用了 Integer::sum。在位置 0，该方法将前置位算好的值（没有前置位则为 0）和当前值合并，并更新到位置 0；
 * 在位置 1，该方法继续将前置位算好的值（即刚才更新到位置 0 中的值）和当前值合并，并更新到位置 1；以此类推。
 * <p>
 * 使用 Stream.reduce() 方法只能得到最终的结果，而 Arrays.parallelPrefix() 方法则还能得到所有的中间结果值（如果需要的话）。
 * 注意第二个 Stream.reduce() 的计算结果是如何已经存在于 parallelPrefix() 计算出的数组中的。
 */
public class ParallelPrefix1 {
    public static void main(String[] args) {
        int[] nums = new Count.Pint().array(10);
        ArrayShow.show(nums);
        System.out.println(Arrays.stream(nums)
                // 终端归约操作：OptionalInt reduce(IntBinaryOperator op); [IntStream.java]
                // 接收一个流并将其元素反复结合，最终折叠（归约）为一个单一的全局计算结果。
                // op (IntBinaryOperator): 一个必须满足“结合律”的二元操作函数（如 Integer::sum 求和）。
                //   - 入参 left (a): 截至目前的累积计算结果（在第一次执行时通常为流的第一个元素或初始种子值）。
                //   - 入参 right (b): 流中正在遍历的下一个元素。
                //   - 返回值 (return): a 和 b 合并计算后的最新结果，该值将作为下一轮合并的累积基数 (left) 继续传递。
                // 核心原理：该方法隐式地遍历整个流，利用二元操作符将所有元素依次折叠。由于其设计目标仅为获取最终的计算总值，
                // 因此在运行结束后，不会在内存中保留或暴露计算过程中的任何中间状态，直接返回封装了最终结果的 OptionalInt 对象（以防范空流）。
                .reduce(Integer::sum)
                .getAsInt());
        // 并行前缀累积（破坏性操作）：static void parallelPrefix(int[] array, IntBinaryOperator op) [Arrays.java]
        // 对目标数组执行就地 (in-place) 的并行前缀累积计算。它会将前置索引位置算出的结果，累积到当前元素上。
        // 注意：该方法（包括不带初始值的 Stream.reduce）没有隐式的“初始零值”，数组的第 0 个元素将直接作为初始基数，实际的计算从索引 1 开始。
        // op (IntBinaryOperator): 一个必须满足“结合律”的二元操作函数（如 Integer::sum 求和）。
        //   - 入参 left (a): 前置索引位置已经算出的累积结果（或者在底层树状归约时，代表左侧区块的累积值）。
        //   - 入参 right (b): 当前索引位置的原始元素值（或者在底层树状归约时，代表右侧区块局部的累积值）。
        //   - 返回值 (return): left 和 right 合并计算后的最新结果（底层并发框架会自动将这个返回值覆盖写回到原数组的对应位置）。
        // 核心原理：底层利用 Fork/Join 框架的并行计算能力。它不是简单的从左到右单步累加，而是多线程跨区块合并。执行完毕后，
        // 原数组会被直接转化为一个状态序列记录表，不仅实现了整体归约，还在每一个具体索引位置上完整保留了从起始元素至当前位置的所有中间累积结果。
        Arrays.parallelPrefix(nums, Integer::sum);
        ArrayShow.show(nums);
        System.out.println(Arrays.stream(new Count.Pint().array(6))
                .reduce(Integer::sum)
                .getAsInt());
    }
}
/* Output:
[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
45
[0, 1, 3, 6, 10, 15, 21, 28, 36, 45]
15
 */
