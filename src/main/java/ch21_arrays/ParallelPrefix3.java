package ch21_arrays;

import java.util.Arrays;

/**
 * @author runningpig66
 * @date 2026-03-28
 * @time 19:35
 * P.805 §21.19 用 parallelPrefix() 进行累积计算
 * {ExcludeFromTravisCI}
 * <p>
 * 正如之前提到的，用 Stream 来初始化非常优雅，但是对于非常大的数组，该方法会导致堆内存耗尽。使用 setAll() 方法来初始化能够更高效地利用内存：
 * <p>
 * 由于很难保证正确使用，因此除非遇到内存或者性能（或两者同时出现）问题时，可以考虑使用 parallelPrefix()，否则都应该默认使用 Stream.reduce()。
 */
public class ParallelPrefix3 {
    static final int SIZE = 10_000_000;

    public static void main(String[] args) {
        long[] nums = new long[SIZE];
        Arrays.setAll(nums, n -> n);
        Arrays.parallelPrefix(nums, Long::sum);
        System.out.println("First 20: " + nums[19]);
        System.out.println("First 200: " + nums[199]);
        System.out.println("All: " + nums[nums.length - 1]);
    }
}
/* Output:
First 20: 190
First 200: 19900
All: 49999995000000
 */
