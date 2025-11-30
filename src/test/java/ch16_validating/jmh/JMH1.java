package ch16_validating.jmh;

import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author runningpig66
 * @date 2025/11/23 周日
 * @time 5:51
 * P.514 §16.6 基准测试 §16.6.2 介绍 JMH
 * <p>
 * Java 微基准测试工具（Java Microbenchmarking Harness, JMH）
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
public class JMH1 {
    private long[] la;

    // 在每次基准测试前执行一次，用来准备测试数据
    @Setup
    public void setup() {
        la = new long[250_000_000];
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
# JMH version: 1.37
# VM version: JDK 21.0.2, OpenJDK 64-Bit Server VM, 21.0.2+13-58
# VM invoker: C:\Users\tianyanyu\.jdks\openjdk-21.0.2\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2024.1.2\lib\idea_rt.jar=50070 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 5 iterations, 10 s each
# Measurement: 5 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: ch16_validating.jmh.JMH1.parallelSetAll

# Run progress: 0.00% complete, ETA 00:03:20
# Fork: 1 of 1
# Warmup Iteration   1: 128717.986 us/op
# Warmup Iteration   2: 126872.529 us/op
# Warmup Iteration   3: 125125.280 us/op
# Warmup Iteration   4: 124812.549 us/op
# Warmup Iteration   5: 125928.984 us/op
Iteration   1: 127042.757 us/op
Iteration   2: 131165.970 us/op
Iteration   3: 128176.328 us/op
Iteration   4: 125359.144 us/op
Iteration   5: 126171.206 us/op


Result "ch16_validating.jmh.JMH1.parallelSetAll":
  127583.081 ±(99.9%) 8700.298 us/op [Average]
  (min, avg, max) = (125359.144, 127583.081, 131165.970), stdev = 2259.440
  CI (99.9%): [118882.783, 136283.379] (assumes normal distribution)


# JMH version: 1.37
# VM version: JDK 21.0.2, OpenJDK 64-Bit Server VM, 21.0.2+13-58
# VM invoker: C:\Users\tianyanyu\.jdks\openjdk-21.0.2\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2024.1.2\lib\idea_rt.jar=50070 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 5 iterations, 10 s each
# Measurement: 5 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Average time, time/op
# Benchmark: ch16_validating.jmh.JMH1.setAll

# Run progress: 50.00% complete, ETA 00:01:42
# Fork: 1 of 1
# Warmup Iteration   1: 183316.727 us/op
# Warmup Iteration   2: 182592.198 us/op
# Warmup Iteration   3: 172952.681 us/op
# Warmup Iteration   4: 173907.122 us/op
# Warmup Iteration   5: 169052.492 us/op
Iteration   1: 168739.032 us/op
Iteration   2: 166213.025 us/op
Iteration   3: 166902.095 us/op
Iteration   4: 168777.073 us/op
Iteration   5: 168133.612 us/op


Result "ch16_validating.jmh.JMH1.setAll":
  167752.967 ±(99.9%) 4416.259 us/op [Average]
  (min, avg, max) = (166213.025, 167752.967, 168777.073), stdev = 1146.889
  CI (99.9%): [163336.708, 172169.227] (assumes normal distribution)


# Run complete. Total time: 00:03:25

REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
experiments, perform baseline and negative tests that provide experimental control, make sure
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
Do not assume the numbers tell you what you want them to tell.

NOTE: Current JVM experimentally supports Compiler Blackholes, and they are in use. Please exercise
extra caution when trusting the results, look into the generated code to check the benchmark still
works, and factor in a small probability of new VM bugs. Additionally, while comparisons between
different JVMs are already problematic, the performance difference caused by different Blackhole
modes can be very significant. Please make sure you use the consistent Blackhole mode for comparisons.

Benchmark            Mode  Cnt       Score      Error  Units
JMH1.parallelSetAll  avgt    5  127583.081 ± 8700.298  us/op
JMH1.setAll          avgt    5  167752.967 ± 4416.259  us/op
 */
