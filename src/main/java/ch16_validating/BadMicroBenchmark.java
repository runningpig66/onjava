package ch16_validating;

import onjava.Timer;

import java.util.Arrays;

/**
 * @author runningpig66
 * @date 2025/11/23 周日
 * @time 4:48
 * P.512 §16.6 基准测试 §16.6.1 微基准测试
 * {ExcludeFromTravisCI}
 */
public class BadMicroBenchmark {
    static final int SIZE = 999_999_999;

    public static void main(String[] args) {
        try { // For machines with insufficient memory
            long[] la = new long[SIZE];
            System.out.println("setAll: " +
                    Timer.duration(() -> Arrays.setAll(la, n -> n)));
            System.out.println("parallelSetAll: "
                    + Timer.duration(() -> Arrays.parallelSetAll(la, n -> n)));
        } catch (OutOfMemoryError e) {
            System.out.println("Insufficient memory");
            System.exit(0);
        }
    }
}
/* Output:
setAll: 535
parallelSetAll: 502
 */
