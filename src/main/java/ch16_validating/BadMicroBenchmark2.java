package ch16_validating;

import onjava.Timer;

import java.util.Arrays;
import java.util.Random;
import java.util.SplittableRandom;

/**
 * @author runningpig66
 * @date 2025/11/23 周日
 * @time 5:35
 * P.513 §16.6 基准测试 §16.6.1 微基准测试
 * Relying on a common resource
 */
public class BadMicroBenchmark2 {
    // SIZE reduced to make it run faster:
    static final int SIZE = 5_000_000;

    public static void main(String[] args) {
        long[] la = new long[SIZE];
        Random r = new Random();
        System.out.println("parallelSetAll: " +
                Timer.duration(() -> Arrays.parallelSetAll(la, n -> r.nextLong())));
        System.out.println("setAll: " +
                Timer.duration(() -> Arrays.setAll(la, n -> r.nextLong())));
        SplittableRandom sr = new SplittableRandom();
        System.out.println("parallelSetAll: " +
                Timer.duration(() -> Arrays.parallelSetAll(la, n -> sr.nextLong())));
        System.out.println("setAll: " +
                Timer.duration(() -> Arrays.setAll(la, n -> sr.nextLong())));
    }
}
/* Output:
parallelSetAll: 945
setAll: 77
parallelSetAll: 63
setAll: 11
 */
