package ch21_arrays.jmh;

import onjava.Rand;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.Arrays;

/**
 * @author runningpig66
 * @date 2026-03-28
 * @time 1:26
 * P.801 §21.17 数组排序 §21.17.2 并行排序
 * <p>
 * 如果排序的性能成了问题，你可以使用 Java 8 中的 parallelSort()。该方法对所有可能的情况都实现了对应的重载版本，包括对数组中的多个部分进行排序，
 * 或者使用 Comparator。为了比较 parallelSort() 相较于传统 sort() 的优势，我们使用了 JMH（在第 16 章中做过介绍）来演示：
 * <p>
 * parallelSort() 的算法不断将大数组分割成较小的数组，直到数组的大小到达限制，继而使用传统的 Arrays.sort() 方法，然后将结果合并。
 * 该算法要求额外的内存空间，但是不会比原始数组的空间更大。在我的机器上，并行排序将速度提升了约 3 倍，而在你的机器上的效果可能会有区别。
 * 因为并行的版本实现起来很简单，所以你可能很想在所有地方都使用它，以取代 Arrays.sort()。当然，这可能没那么容易（参见 16.6 节的内容）。
 */
// 每个基准测试实例的状态，按“线程”隔离
@State(Scope.Thread)
public class ParallelSort {
    private long[] sourceArray;

    // 在每次基准测试前执行一次，用来准备测试数据
    @Setup
    public void setup() {
        // 生成纯净的原始乱序数据，作为只读模板
        sourceArray = new Rand.Plong().array(10_000_000);
    }

    // 一个基准方法：串行 sort
    @Benchmark
    public void sort() {
        // 每次测试拷贝一份全新的乱序数组
        long[] la = Arrays.copyOf(sourceArray, sourceArray.length);
        Arrays.sort(la);
    }

    // 另一个基准方法：并行 sort
    @Benchmark
    public void parallelSort() {
        // 每次测试拷贝一份全新的乱序数组
        long[] la = Arrays.copyOf(sourceArray, sourceArray.length);
        Arrays.parallelSort(la);
    }
}
/* Output:
# JMH version: 1.37
# VM version: JDK 21.0.9, Java HotSpot(TM) 64-Bit Server VM, 21.0.9+7-LTS-338
# VM invoker: C:\Program Files\Java\jdk-21\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2024.1.2\lib\idea_rt.jar=62938 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 5 iterations, 10 s each
# Measurement: 5 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: ch21_arrays.jmh.ParallelSort.parallelSort

# Run progress: 0.00% complete, ETA 00:16:40
# Fork: 1 of 5
# Warmup Iteration   1: 12.792 ops/s
# Warmup Iteration   2: 12.750 ops/s
# Warmup Iteration   3: 13.570 ops/s
# Warmup Iteration   4: 13.479 ops/s
# Warmup Iteration   5: 13.338 ops/s
Iteration   1: 13.315 ops/s
Iteration   2: 13.328 ops/s
Iteration   3: 13.383 ops/s
Iteration   4: 13.319 ops/s
Iteration   5: 13.586 ops/s

# Run progress: 10.00% complete, ETA 00:15:13
# Fork: 2 of 5
# Warmup Iteration   1: 13.038 ops/s
# Warmup Iteration   2: 13.039 ops/s
# Warmup Iteration   3: 12.863 ops/s
# Warmup Iteration   4: 12.869 ops/s
# Warmup Iteration   5: 13.878 ops/s
Iteration   1: 13.906 ops/s
Iteration   2: 13.802 ops/s
Iteration   3: 13.836 ops/s
Iteration   4: 13.639 ops/s
Iteration   5: 12.944 ops/s

# Run progress: 20.00% complete, ETA 00:13:31
# Fork: 3 of 5
# Warmup Iteration   1: 12.694 ops/s
# Warmup Iteration   2: 13.763 ops/s
# Warmup Iteration   3: 13.172 ops/s
# Warmup Iteration   4: 11.884 ops/s
# Warmup Iteration   5: 12.831 ops/s
Iteration   1: 13.051 ops/s
Iteration   2: 13.725 ops/s
Iteration   3: 13.715 ops/s
Iteration   4: 12.740 ops/s
Iteration   5: 12.612 ops/s

# Run progress: 30.00% complete, ETA 00:11:50
# Fork: 4 of 5
# Warmup Iteration   1: 13.603 ops/s
# Warmup Iteration   2: 13.614 ops/s
# Warmup Iteration   3: 13.545 ops/s
# Warmup Iteration   4: 12.854 ops/s
# Warmup Iteration   5: 13.394 ops/s
Iteration   1: 13.507 ops/s
Iteration   2: 13.195 ops/s
Iteration   3: 13.599 ops/s
Iteration   4: 13.834 ops/s
Iteration   5: 13.706 ops/s

# Run progress: 40.00% complete, ETA 00:10:08
# Fork: 5 of 5
# Warmup Iteration   1: 13.726 ops/s
# Warmup Iteration   2: 13.763 ops/s
# Warmup Iteration   3: 13.872 ops/s
# Warmup Iteration   4: 13.698 ops/s
# Warmup Iteration   5: 13.416 ops/s
Iteration   1: 12.319 ops/s
Iteration   2: 12.527 ops/s
Iteration   3: 13.567 ops/s
Iteration   4: 12.943 ops/s
Iteration   5: 13.109 ops/s


Result "ch21_arrays.jmh.ParallelSort.parallelSort":
  13.328 ±(99.9%) 0.335 ops/s [Average]
  (min, avg, max) = (12.319, 13.328, 13.906), stdev = 0.448
  CI (99.9%): [12.993, 13.664] (assumes normal distribution)


# JMH version: 1.37
# VM version: JDK 21.0.9, Java HotSpot(TM) 64-Bit Server VM, 21.0.9+7-LTS-338
# VM invoker: C:\Program Files\Java\jdk-21\bin\java.exe
# VM options: -javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2024.1.2\lib\idea_rt.jar=62938 -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8
# Blackhole mode: compiler (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
# Warmup: 5 iterations, 10 s each
# Measurement: 5 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: ch21_arrays.jmh.ParallelSort.sort

# Run progress: 50.00% complete, ETA 00:08:27
# Fork: 1 of 5
# Warmup Iteration   1: 2.311 ops/s
# Warmup Iteration   2: 2.333 ops/s
# Warmup Iteration   3: 2.333 ops/s
# Warmup Iteration   4: 2.326 ops/s
# Warmup Iteration   5: 2.316 ops/s
Iteration   1: 2.321 ops/s
Iteration   2: 2.338 ops/s
Iteration   3: 2.338 ops/s
Iteration   4: 2.344 ops/s
Iteration   5: 2.332 ops/s

# Run progress: 60.00% complete, ETA 00:06:47
# Fork: 2 of 5
# Warmup Iteration   1: 2.253 ops/s
# Warmup Iteration   2: 2.293 ops/s
# Warmup Iteration   3: 2.289 ops/s
# Warmup Iteration   4: 2.302 ops/s
# Warmup Iteration   5: 2.298 ops/s
Iteration   1: 2.303 ops/s
Iteration   2: 2.307 ops/s
Iteration   3: 2.306 ops/s
Iteration   4: 2.307 ops/s
Iteration   5: 2.308 ops/s

# Run progress: 70.00% complete, ETA 00:05:06
# Fork: 3 of 5
# Warmup Iteration   1: 2.301 ops/s
# Warmup Iteration   2: 2.314 ops/s
# Warmup Iteration   3: 2.313 ops/s
# Warmup Iteration   4: 2.308 ops/s
# Warmup Iteration   5: 2.264 ops/s
Iteration   1: 2.311 ops/s
Iteration   2: 2.313 ops/s
Iteration   3: 2.316 ops/s
Iteration   4: 2.320 ops/s
Iteration   5: 2.317 ops/s

# Run progress: 80.00% complete, ETA 00:03:24
# Fork: 4 of 5
# Warmup Iteration   1: 2.328 ops/s
# Warmup Iteration   2: 2.352 ops/s
# Warmup Iteration   3: 2.350 ops/s
# Warmup Iteration   4: 2.330 ops/s
# Warmup Iteration   5: 2.307 ops/s
Iteration   1: 2.340 ops/s
Iteration   2: 2.336 ops/s
Iteration   3: 2.332 ops/s
Iteration   4: 2.344 ops/s
Iteration   5: 2.345 ops/s

# Run progress: 90.00% complete, ETA 00:01:42
# Fork: 5 of 5
# Warmup Iteration   1: 2.274 ops/s
# Warmup Iteration   2: 2.267 ops/s
# Warmup Iteration   3: 2.290 ops/s
# Warmup Iteration   4: 2.275 ops/s
# Warmup Iteration   5: 2.281 ops/s
Iteration   1: 2.279 ops/s
Iteration   2: 2.270 ops/s
Iteration   3: 2.259 ops/s
Iteration   4: 2.281 ops/s
Iteration   5: 2.274 ops/s


Result "ch21_arrays.jmh.ParallelSort.sort":
  2.314 ±(99.9%) 0.019 ops/s [Average]
  (min, avg, max) = (2.259, 2.314, 2.345), stdev = 0.025
  CI (99.9%): [2.295, 2.332] (assumes normal distribution)


# Run complete. Total time: 00:17:05

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

Benchmark                   Mode  Cnt   Score   Error  Units
ParallelSort.parallelSort  thrpt   25  13.328 ± 0.335  ops/s
ParallelSort.sort          thrpt   25   2.314 ± 0.019  ops/s
 */
