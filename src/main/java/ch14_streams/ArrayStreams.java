package ch14_streams;

import java.util.Arrays;

/**
 * @author runningpig66
 * @date 2025/10/28 周二
 * @time 20:33
 * 代码清单 P.387 Arrays.stream() 方法也可以生成 IntStream, LongStream 和 DoubleStream
 */
public class ArrayStreams {
    public static void main(String[] args) {
        Arrays.stream(new double[]{3.14159, 2.718, 1.618})
                .forEach(n -> System.out.format("%f ", n));
        System.out.println();
        Arrays.stream(new int[]{1, 3, 5})
                .forEach(n -> System.out.format("%d ", n));
        System.out.println();
        Arrays.stream(new long[]{11, 22, 44, 66})
                .forEach(n -> System.out.format("%d ", n));
        System.out.println();
        // Select a subrange
        Arrays.stream(new int[]{1, 3, 5, 7, 15, 28, 37}, 3, 6)
                .forEach(n -> System.out.format("%d ", n));
    }
}
/* Output:
3.141590 2.718000 1.618000
1 3 5
11 22 44 66
7 15 28
 */