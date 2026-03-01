package ch26_concurrent;

import java.util.Arrays;

/**
 * @author runningpig66
 * @date 3月1日 周日
 * @time 1:13
 * P.223 §5.7 并行流 §5.7.1 parallel() 并非灵丹妙药
 * {ExcludeFromTravisCI}
 * <p>
 * 核心设计意图与并发基准测试结论：
 * 本类专门针对包装类对象数组 (Long[]) 进行并行归约 (parallel().reduce) 测试。
 * 将其从 Summing3 中单独抽离，是因为如果将其放在上面的程序中，则会导致漫长的垃圾收集过程。
 * 独立测试确保了运行环境的纯洁性，避免受到其他方法产生的废弃对象所引发的 GC 停顿干扰。
 * 1. CPU 高速缓存 (Cache) 与内存连续性的物理限制
 * 处理器的缓存机制是导致耗时增加的主要原因之一。在 Summing2 中，基本类型 long[] 是一段连续的内存，
 * 处理器会更容易预测到对这个数组的使用情况，从而将数组元素保存在缓存中以备后续所需。
 * 而在本例中，Long[] 并不是一段连续的数值数组，而是一段连续的 Long 型对象引用的数组。
 * 尽管该引用数组本身可能在缓存中，但其指向的那些对象几乎永远在缓存之外。
 * CPU 每次计算都必须去主存中寻址（发生 Cache Miss），因为访问缓存远远比跳出去访问主存要快，这彻底抹平了多核并行的速度优势。
 * 2. 并发收益的规模拐点 (规模效应博弈)
 * 在 1000 万至 2000 万的数据量下，该方式比未使用 parallel() 的版本要稍微快一点，但差距不大。
 * 此时 Fork/Join 框架的任务拆分、线程唤醒开销，加上对象寻址的缓存失效开销，基本抵消了多核并行的红利。
 * 然而，当数据量达到 1 亿时（串行 1140ms vs 并行 381ms），庞大的绝对计算量终于盖过了线程调度开销，多核并行的收益才产生实质性的跨越。
 * 3. 性能基准的最终对比
 * 尽管在一亿规模下，Long[] 的并行计算比串行快了约 3 倍，但其耗时 (381ms) 依然远远落后于相同规模下基本类型 long[] 的并行耗时 (约 38ms)。
 * 这确立了并发优化的底层铁律：多线程并不能弥补数据结构在物理内存布局（对象寻址与基本类型连续内存）上的先天劣势。
 */
public class Summing4 {
    public static void main(String[] args) {
        System.out.println(Summing3.CHECK);
        Long[] aL = new Long[Summing3.SZ + 1];
        Arrays.parallelSetAll(aL, i -> (long) i);
        Summing.timeTest("Long Parallel", Summing3.CHECK, () ->
                Arrays.stream(aL).parallel().reduce(0L, Long::sum));
    }
}
/* Output: （这里的输出是经过不同SZ汇总整理的）
// SZ = 10_000_000;
50000005000000
Long Parallel: 78ms

// SZ = 20_000_000;
200000010000000
Long Parallel: 106ms

// SZ = 100_000_000;
5000000050000000
Long Parallel: 381ms
 */
