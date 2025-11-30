package ch16_validating.jmh;

import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author runningpig66
 * @date 2025/11/30 周日
 * @time 17:13
 * P.517 §16.6 基准测试 §16.6.2 介绍 JMH
 * <p>
 * Java 微基准测试工具（Java Microbenchmarking Harness, JMH）
 * <p>
 * 基于 C/P/N/Q, 我们使用不同大小（即 N 的值）的数组重新运行测试：
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
public class JMH2 {
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

    // 一个基准方法：串行 setAll
    @Benchmark
    public void setAll() {
        Arrays.setAll(la, n -> n);
    }

    // 另一个基准方法：并行 setAll
    @Benchmark
    public void parallelSetAll() {
        Arrays.parallelSetAll(la, n -> n);
    }
}
/* Output:
......以下是编辑后的结果：
Benchmark               (size)  Mode  Cnt       Score       Error  Units
JMH2.setAll                  1  avgt    5       0.002 ±     0.001  us/op
JMH2.parallelSetAll          1  avgt    5       0.029 ±     0.001  us/op
JMH2.setAll                 10  avgt    5       0.003 ±     0.001  us/op
JMH2.parallelSetAll         10  avgt    5      11.418 ±     0.616  us/op
JMH2.setAll                100  avgt    5       0.023 ±     0.001  us/op
JMH2.parallelSetAll        100  avgt    5      12.580 ±     1.857  us/op
JMH2.setAll               1000  avgt    5       0.219 ±     0.002  us/op
JMH2.parallelSetAll       1000  avgt    5      11.563 ±     1.948  us/op
JMH2.setAll              10000  avgt    5       2.479 ±     0.090  us/op
JMH2.parallelSetAll      10000  avgt    5      14.133 ±     3.506  us/op
JMH2.setAll             100000  avgt    5      27.197 ±     0.127  us/op
JMH2.parallelSetAll     100000  avgt    5      21.450 ±     3.413  us/op
JMH2.setAll            1000000  avgt    5     374.640 ±    82.942  us/op
JMH2.parallelSetAll    1000000  avgt    5      78.537 ±     0.549  us/op
JMH2.setAll           10000000  avgt    5    6627.524 ±   888.619  us/op
JMH2.parallelSetAll   10000000  avgt    5    4877.681 ±    77.833  us/op
JMH2.setAll          100000000  avgt    5   68582.035 ±  6862.212  us/op
JMH2.parallelSetAll  100000000  avgt    5   48871.312 ±   772.342  us/op
JMH2.setAll          250000000  avgt    5  181572.089 ± 69440.199  us/op
JMH2.parallelSetAll  250000000  avgt    5  122968.043 ±  3829.839  us/op
 */
