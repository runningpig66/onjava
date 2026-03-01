package ch26_concurrent;

import onjava.Timer;

import java.util.function.LongSupplier;
import java.util.stream.LongStream;

/**
 * @author runningpig66
 * @date 2月28日 周六
 * @time 9:07
 * P.219 §5.7 并行流 §5.7.1 parallel() 并非灵丹妙药
 * <p>
 * 并行流的底层拆分机制 (Spliterator) 与性能陷阱
 * 1. 多线程并行的核心前提：数据必须能被极低成本地“切分”。
 * 并行流并非魔法，它的提速依赖于底层 Spliterator (分流器) 的 trySplit() 方法能否高效执行。
 * 2. 为什么 rangeClosed().parallel() 极快？(无状态/纯数学拆分)
 * - 底层结构：RangeIntSpliterator。它在内存中不生成任何实际的数字序列，只存储起始值 (from) 和结束值 (to)。
 * - 拆分原理：当请求并行时，它仅执行极其廉价的数学运算：mid = (from + to) >>> 1。
 * - 结果：任务瞬间被对半切分给多个 CPU 核心。整个过程几乎零对象创建，零堆内存 (Heap) 消耗。
 * 3. 为什么 iterate().parallel() 极慢甚至导致 OOM？(状态依赖/强行并行的灾难)
 * - 底层结构：iterate 的下一个元素严格依赖上一个元素的计算结果，在逻辑上是绝对串行的，无法通过数学公式直接切分。
 * - 强行并行的代价：为了让串行数据并行处理，JDK 底层只能委派一个单线程，顺着 iterate 一批批地生成数据，
 * 并在堆内存中不断 new 出各种大小的数组（作为数据缓冲池），再将这些数组分发给其他线程去求和。
 * - 结果：多核 CPU 实际上在等待单线程生成数据；同时，当数据量高达上亿级别时，海量的数组对象分配会瞬间榨干 JVM 内存，
 * 引发疯狂的 GC（垃圾回收），最终上下文切换的开销远超计算本身，甚至导致 OutOfMemoryError。
 * 4. 结论：
 * - 无状态、可通过索引随机访问的数据结构（如数组、range） -> 适合并行。
 * - 状态依赖、必须顺序访问的数据结构（如 iterate、LinkedList、IO流） -> 严禁使用并行流。
 */
public class Summing {
    static void timeTest(String id, long checkValue, LongSupplier operation) {
        System.out.print(id + ": ");
        Timer timer = new Timer();
        long result = operation.getAsLong();
        if (result == checkValue) {
            System.out.println(timer.duration() + "ms");
        } else {
            System.out.format("result: %d%ncheckValue: %d%n", result, checkValue);
        }
    }

    public static final int SZ = 100_000_000;
    // This even works:
    // public static final int SZ = 1_000_000_000;
    public static final long CHECK = (long) SZ * ((long) SZ + 1) / 2; // Gauss's formula

    public static void main(String[] args) {
        System.out.println(CHECK);
        timeTest("Sum Stream", CHECK, () ->
                LongStream.rangeClosed(0, SZ).sum());
        timeTest("Sum Stream Parallel", CHECK, () ->
                LongStream.rangeClosed(0, SZ).parallel().sum());
        timeTest("Sum Iterated", CHECK, () ->
                LongStream.iterate(0, i -> i + 1).limit(SZ + 1).sum());
        // Slower & runs out of memory above 1_000_000:
        timeTest("Sum Iterated Parallel", CHECK, () ->
                LongStream.iterate(0, i -> i + 1).parallel().limit(SZ + 1).sum());
    }
}
/* Output:
5000000050000000
Sum Stream: 116ms
Sum Stream Parallel: 15ms
Sum Iterated: 136ms
Sum Iterated Parallel: 1024ms
 */
