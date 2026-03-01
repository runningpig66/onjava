package ch26_concurrent;

import java.util.Arrays;

/**
 * @author runningpig66
 * @date 2月28日 周六
 * @time 23:41
 * P.222 §5.7 并行流 §5.7.1 parallel() 并非灵丹妙药
 * {ExcludeFromTravisCI}
 * <p>
 * 核心设计意图与并发基准测试结论：
 * 本类作为 Summing2 的对照实验，通过仅仅将基本类型数组 (long[]) 替换为包装类对象数组 (Long[])，
 * 极其直观地揭示了 Java 并发性能优化中最容易踩中的致命陷阱：对象开销、缓存击穿与 GC 风暴。
 * 1. 内存占用的极速膨胀 (为什么 SZ 被迫减半？)
 * 一个基本类型 long 只占绝对的 8 字节。而一个 Long 对象包含：对象头 (通常 12-16 字节) + 内部数据 (8 字节) + 数组指针 (4-8 字节)。
 * 相同数据量下，Long[] 消耗的物理内存是 long[] 的好几倍。为了防止 JVM 再次爆出 OutOfMemoryError，本例被迫将测试规模砍半。
 * 2. 致命的 CPU 缓存击穿 (Cache Miss)
 * long[] 在物理内存中是绝对连续的，完美契合 CPU 的空间局部性原理，L1/L2 缓存可以极速预读。
 * Long[] 数组中存储的仅仅是“内存地址”，真正的 Long 对象如满天星般散落在堆内存中。多线程在遍历时必须顺着指针到处寻址，
 * 导致 CPU 高速缓存频频失效，绝大部分时间都被浪费在主存读取的等待上。
 * 3. 拆箱装箱引发的 GC 风暴 (为什么 parallelPrefix 慢得如此夸张？)
 * Long 对象是不可变的。当执行 Arrays.parallelPrefix(aL, Long::sum) 时，底层必须经过：
 * 拆箱获取 long 值 -> 执行加法 -> 【致命步骤】在堆内存中 new 一个全新的 Long 对象 -> 将新对象地址写回数组。
 * 一千万次并发运算，瞬间在堆内存中制造了一千万个朝生夕灭的废弃对象。这直接触发了极其沉重的垃圾回收 (Stop-The-World)，
 * 线程被操作系统强行挂起，彻底抹杀了多核并发带来的一切收益。
 * 4. 为什么 basicSum 依然较快？
 * 仔细观察 basicSum 的源码，它的累加器是基本类型：long sum = 0;。
 * 虽然它也遍历了 Long[]，但它只涉及“自动拆箱”来读取数值，并没有“重新装箱”并将对象写回数组的动作，因此成功避开了海量对象创建的 GC 灾难。
 * 最终法则：在追求极致性能的并发计算场景下，必须远离包装类 (Object) 及其装箱/拆箱机制，尽可能使用基本数据类型 (Primitive Types)。
 */
public class Summing3 {
    static long basicSum(Long[] ia) {
        long sum = 0;
        /*int size = ia.length;
        for (int i = 0; i < size; i++) {
            sum += ia[i];
        }*/
        for (Long aLong : ia) {
            sum += aLong; // 自动拆箱
        }
        return sum;
    }

    // Approximate largest value of SZ before running out of memory on my machine:
    public static final int SZ = 100_000_000;
    public static final long CHECK = (long) SZ * ((long) SZ + 1) / 2;

    public static void main(String[] args) {
        System.out.println(CHECK);
        Long[] aL = new Long[SZ + 1];
        Arrays.parallelSetAll(aL, i -> (long) i);
        // 终端归约操作（纯粹的折叠计算）：T reduce(T identity, BinaryOperator<T> accumulator)
        // 将流中的所有元素按照指定的规则一层层合并（折叠），最终归约（递归约减）输出一个单一的结果。
        // 与 parallelPrefix 不同，reduce 是纯函数式的非破坏性操作：它只计算并返回最终值，绝对不会修改底层的数据源。
        // - 入参 identity (T): 初始值（本例中为 0L）。它是归约操作的起点；如果流是空的，它就是默认返回值。
        //   *极度重要：在并行流中，Fork/Join 框架切分出多个子任务时，这个 identity 会作为每一个子任务局部累加的初始基数。
        //   因此它必须是该运算的“恒等值”（例如求和操作必须传 0，求积操作必须传 1），否则多线程计算结果必错。
        // - 入参 accumulator (BinaryOperator): 累加器函数（如 Long::sum）。必须满足“结合律”和“无状态”。
        //   - 内部参数 1 (partial result): 迄今为止累积的中间结果（在第一步计算时，它的值就是 identity）。
        //   - 内部参数 2 (element): 当前从流中遍历到的下一个元素。
        //   - 返回值：将当前元素折叠进累积值后的新结果。
        // 性能陷阱提示（针对本类环境）：由于本例使用的是对象流 Stream<Long>，每次 accumulator 执行加法运算时，
        // 都会强行经历拆箱 (Long -> long) 和再次装箱 (new Long) 的过程。在一千万次遍历中，这会产生海量的废弃对象。
        Summing.timeTest("Long Array Stream Reduce", CHECK, () ->
                Arrays.stream(aL).reduce(0L, Long::sum));
        Summing.timeTest("Long Basic Sum", CHECK, () -> basicSum(aL));
        // Destructive summation:
        Summing.timeTest("Long parallelPrefix", CHECK, () -> {
            Arrays.parallelPrefix(aL, Long::sum);
            return aL[aL.length - 1];
        });
    }
}
/* Output: （这里的输出是经过不同SZ汇总整理的）
// SZ = 10_000_000;
50000005000000
Long Array Stream Reduce: 96ms
Long Basic Sum: 18ms
Long parallelPrefix: 181ms

// SZ = 20_000_000;
200000010000000
Long Array Stream Reduce: 145ms
Long Basic Sum: 27ms
Long parallelPrefix: 228ms

// SZ = 100_000_000;
5000000050000000
Long Array Stream Reduce: 1140ms
Long Basic Sum: 142ms
Long parallelPrefix: 1113ms
 */
