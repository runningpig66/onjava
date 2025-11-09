package ch14_streams;

import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/11/1 周六
 * @time 22:41
 * 代码清单 P.392 将函数应用于每个流元素 3：
 * 如果 Function 生成的结果类型是某种数值类型，必须使用相应的 mapTo 操作来代替：
 * IntStream mapToInt(ToIntFunction<? super T> mapper);
 * LongStream mapToLong(ToLongFunction<? super T> mapper);
 * DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper);
 */
public class FunctionMap3 {
    public static void main(String[] args) {
        Stream.of("5", "7", "9")
                .mapToInt(Integer::parseInt)
                .forEach(n -> System.out.format("%d ", n));
        System.out.println();
        Stream.of("17", "19", "23")
                .mapToLong(Long::parseLong)
                .forEach(n -> System.out.format("%d ", n));
        System.out.println();
        Stream.of("17", "1.9", ".23")
                .mapToDouble(Double::parseDouble)
                .forEach(n -> System.out.format("%f ", n));
    }
}
/* Output:
5 7 9
17 19 23
17.000000 1.900000 0.230000
 */
