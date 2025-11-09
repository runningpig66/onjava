package ch14_streams;

import static ch14_streams.RandInts.rands;

/**
 * @author runningpig66
 * @date 2025/11/10 周一
 * @time 5:31
 * 代码清单 P.41 终结操作：获得流相关的信息
 * <p>
 * 获得数值化流相关的信息
 * <p>
 * 就是通常的意义，获得平均值。
 * OptionalDouble average();
 * <p>
 * 这些操作不需要一个 Comparator, 因为它们处理的是数值化流。
 * OptionalInt max();
 * OptionalInt min();
 * <p>
 * 将流中的数值累加起来。
 * int sum();
 * <p>
 * 返回可能有用的摘要数据。
 * IntSummaryStatistics summaryStatistics(
 */
public class NumericStreamInfo {
    public static void main(String[] args) {
        System.out.println(rands().average().getAsDouble());
        System.out.println(rands().max().getAsInt());
        System.out.println(rands().min().getAsInt());
        System.out.println(rands().sum());
        System.out.println(rands().summaryStatistics());
    }
}
/* Output:
507.94
998
8
50794
IntSummaryStatistics{count=100, sum=50794, min=8, average=507.940000, max=998}
 */
