package ch16_validating.jmh;

import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author runningpig66
 * @date 2025/11/30 周日
 * @time 17:47
 * P.518 §16.6 基准测试 §16.6.2 介绍 JMH
 * <p>
 * Java 微基准测试工具（Java Microbenchmarking Harness, JMH）
 * <p>
 * 基于 C/P/N/Q, 通过使用下面的方法 f(), 我们让任务 N*Q 更加复杂．从而提高了并行的可能性：
 */
// 每个基准测试实例的状态，按“线程”隔离
@State(Scope.Thread)
// 基准模式：统计每次操作的平均耗时
@BenchmarkMode(Mode.AverageTime)
// 输出时间单位：微秒
@OutputTimeUnit(TimeUnit.MICROSECONDS)
// Increase these three for more accuracy:
// 预热：先跑 5 次，让 JIT “热身”，这些数据不计入结果
@Warmup(iterations = 5)
// 正式测量：再跑 5 次，这些数据会参与统计
@Measurement(iterations = 5)
// fork 1 个独立的 JVM 进程来跑测试（避免被 IDE 之类干扰）
@Fork(1)
public class JMH3 {
    private long[] la;
    // @Param 自动将它的每个值插人到它注解的变量中。值必须是字符串类型，然后会被转换为适当的类型，本例中是 int.
    @Param({
            "1",
            "10",
            "100",
            "1000",
            "10000",
            "100000",
            "1000000",
            "10000000",
            "100000000",
            "250000000"
    })
    int size;

    // 在每次基准测试前执行一次，用来准备测试数据
    @Setup
    public void setup() {
        la = new long[size];
    }

    public static long f(long x) {
        long quadratic = 42 * x * x + 19 * x + 47;
        return Long.divideUnsigned(quadratic, x + 1);
    }

    // 一个基准方法：串行 setAll
    @Benchmark
    public void setAll() {
        // 方法引用允许做“形参的自动扩大转换（widening）”
        Arrays.setAll(la, JMH3::f);
    }

    // 另一个基准方法：并行 setAll
    @Benchmark
    public void parallelSetAll() {
        Arrays.parallelSetAll(la, n -> f(n));
    }
}
/* Output:
......以下是编辑后的结果：看来 parallelSetAll() 的结果在很大程度上取决于计算的复杂性和数组的大小。
Benchmark               (size)  Mode  Cnt        Score      Error  Units
JMH3.setAll                  1  avgt    5        0.008 ±    0.001  us/op
JMH3.parallelSetAll          1  avgt    5        0.034 ±    0.001  us/op
JMH3.setAll                 10  avgt    5        0.055 ±    0.001  us/op
JMH3.parallelSetAll         10  avgt    5       11.167 ±    0.512  us/op
JMH3.setAll                100  avgt    5        0.580 ±    0.041  us/op
JMH3.parallelSetAll        100  avgt    5       12.403 ±    1.087  us/op
JMH3.setAll               1000  avgt    5        5.705 ±    0.066  us/op
JMH3.parallelSetAll       1000  avgt    5       12.356 ±    0.811  us/op
JMH3.setAll              10000  avgt    5       53.897 ±    0.835  us/op
JMH3.parallelSetAll      10000  avgt    5       13.595 ±    2.115  us/op
JMH3.setAll             100000  avgt    5      539.838 ±    8.339  us/op
JMH3.parallelSetAll     100000  avgt    5       94.052 ±    5.486  us/op
JMH3.setAll            1000000  avgt    5     5524.339 ±   25.931  us/op
JMH3.parallelSetAll    1000000  avgt    5      574.538 ±   16.633  us/op
JMH3.setAll           10000000  avgt    5    54591.210 ± 3480.700  us/op
JMH3.parallelSetAll   10000000  avgt    5     5620.858 ±  838.027  us/op
JMH3.setAll          100000000  avgt    5   542287.368 ± 8347.913  us/op
JMH3.parallelSetAll  100000000  avgt    5    55785.431 ± 3959.735  us/op
JMH3.setAll          250000000  avgt    5  1341932.980 ± 4889.760  us/op
JMH3.parallelSetAll  250000000  avgt    5   135193.139 ± 3011.585  us/op
 */
