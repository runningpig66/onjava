package ch26_concurrent;

import java.util.Arrays;

/**
 * @author runningpig66
 * @date 2月28日 周六
 * @time 17:47
 * P.221 §5.7 并行流 §5.7.1 parallel() 并非灵丹妙药
 * {ExcludeFromTravisCI}
 * <p>
 * 核心设计意图与并发基准测试结论：本类的目的并非教授 Arrays 的生僻 API，而是通过控制变量法
 * （预分配单一大数组，彻底排除对象创建与 GC 垃圾回收的干扰），来揭示 JVM 并发优化底层必须面对的三个“残酷真相”：
 * 1. 物理内存的绝对壁垒 (SZ = 20亿 -> OOM)
 * 并发计算的前提是系统资源足以支撑数据规模。当数组达到 15GB 级别时（在我的机器上），JVM 堆内存直接崩溃。
 * 这证明了在面临物理资源极限时，单纯的算法级并发将失去意义，必须转向分布式架构或流式分批处理。
 * 2. 读写操作的巨大性能鸿沟 (parallelPrefix 永远最慢)
 * 尽管 parallelPrefix 底层使用了极其精妙的树状归约算法，但它是一个“破坏性操作”（需要将结果不断写回主存）。
 * 向主存写入数据的硬件耗时，成百上千倍地慢于 CPU 在高速缓存 (L1/L2 Cache) 中的纯粹读与算 (如 stream().sum())。
 * 这警示我们：并发优化必须时刻警惕频繁的内存写入。
 * 3. 并行开销的倒挂现象 (SZ = 2千万 -> 单线程 for 循环最快)
 * 并非数据量越大，并行就一定越好。在较小的数据量下，Fork/Join 框架切分数组、唤醒线程、上下文切换以及最终合并结果的调度开销，
 * 已经远远超出了单线程顺着内存地址一路 for 循环遍历的耗时。
 * 最终法则：并发编程是一门高度依赖基准测试 (Benchmark) 的“实验科学”。没有万能的性能公式，遇到复杂的计算场景，只能“试着来”。
 */
public class Summing2 {
    static long basicSum(long[] ia) {
        long sum = 0;
        /*int size = ia.length;
        for (int i = 0; i < size; i++) {
            sum += ia[i];
        }*/
        for (long l : ia) {
            sum += l;
        }
        return sum;
    }

    // Approximate largest value of SZ before running out of memory on my machine:
    public static final int SZ = 20_000_000;
    public static final long CHECK = (long) SZ * ((long) SZ + 1) / 2;

    public static void main(String[] args) {
        System.out.println(CHECK);
        long[] la = new long[SZ + 1];
        // 并发初始化数组：void parallelSetAll(long[] array, IntToLongFunction generator)
        // 利用 Fork/Join 框架的多线程能力，将数组划分为多个子段并行赋值。
        // generator (IntToLongFunction): 输入当前元素的下标 (index)，返回该下标对应的计算结果 (value)。
        // 适用场景：数组巨大，且每个元素的初始值可以通过其下标经过数学公式独立计算得出时，能极大缩短初始化时间。
        Arrays.parallelSetAll(la, i -> i);
        Summing.timeTest("Array Stream Sum", CHECK, () -> Arrays.stream(la).sum());
        Summing.timeTest("Parallel", CHECK, () -> Arrays.stream(la).parallel().sum());
        Summing.timeTest("Basic Sum", CHECK, () -> basicSum(la));
        // 并行前缀计算（破坏性操作）：void parallelPrefix(long[] array, LongBinaryOperator op)
        // 对数组进行就地 (in-place) 的并发累积计算。它会将前一个或前一组元素的计算结果，累积到当前元素上。
        // op (LongBinaryOperator): 一个必须满足“结合律”的二元函数（如 Long::sum 求和、Math::max 求最大值）。
        //   - 入参 left (a): 左侧区块已经累积的结果（或者说是前一个/组元素的值）。
        //   - 入参 right (b): 当前遍历到的原始元素值（或者说是右侧区块局部的累积值）。
        //   - 返回值 (return): left 和 right 合并计算后的最新结果（底层框架会自动将这个返回值覆盖写回到原数组的对应位置）。
        // 核心原理：底层采用树状归约算法（Tree Reduction）。它不是简单的从左到右单步累加，
        // 而是多线程先计算局部区块的累加值，再跨区块合并，最终使得 array[i] 的值等于从 array[0] 到 array[i] 的累积结果。
        // Destructive summation:
        Summing.timeTest("parallelPrefix", CHECK, () -> {
            Arrays.parallelPrefix(la, Long::sum);
            return la[la.length - 1];
        });
    }
}
/* Output: （这里的输出是经过不同SZ汇总整理的）
// SZ = 20_000_000;
200000010000000
Array Stream Sum: 16ms
Parallel: 18ms
Basic Sum: 11ms
parallelPrefix: 103ms

// SZ = 100_000_000;
5000000050000000
Array Stream Sum: 56ms
Parallel: 38ms
Basic Sum: 51ms
parallelPrefix: 130ms

// SZ = 1000_000_000;
500000000500000000
Array Stream Sum: 463ms
Parallel: 235ms
Basic Sum: 473ms
parallelPrefix: 734ms

// SZ = 2000_000_000;
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	at ch26_concurrent.Summing2.main(Summing2.java:31)
 */
