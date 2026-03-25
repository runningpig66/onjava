package ch21_arrays;

import onjava.Rand;

import java.util.Arrays;

/**
 * @author runningpig66
 * @date 2026-03-24
 * @time 23:12
 * P.790 §21.12 关于数组并行 §21.12.2 parallelSetAll()
 * <p>
 * 我们可以用 setAll() 来初始化更大的数组。如果速度成了问题，Arrays.parallelSetAll()（应该）能更快地完成初始化（要记住 21.12 节中提到的问题）：
 */
public class ParallelSetAll {
    static final int SIZE = 10_000_000;

    static void intArray() {
        int[] ia = new int[SIZE];
        Arrays.setAll(ia, new Rand.Pint()::get);
        Arrays.parallelSetAll(ia, new Rand.Pint()::get);
    }

    static void longArray() {
        long[] la = new long[SIZE];
        Arrays.setAll(la, new Rand.Plong()::get);
        Arrays.parallelSetAll(la, new Rand.Plong()::get);
    }

    public static void main(String[] args) {
        intArray();
        longArray();
    }
}
