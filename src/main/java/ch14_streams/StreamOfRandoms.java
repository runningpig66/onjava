package ch14_streams;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/11/1 周六
 * @time 23:29
 * 代码清单 P.394 在应用 map() 期间组合流：
 * 下面是另一个示例。我们从一个整数值组成的流开始，然后使用其中的每一个来创建很多随机数：
 * IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper);
 * 示例中引人了 concat(), 它会按照参数的顺序将两个流组合到一起。
 */
public class StreamOfRandoms {
    static Random rand = new Random(47);

    public static void main(String[] args) {
        Stream.of(1, 2, 3, 4, 5)
                .flatMapToInt(i -> IntStream.concat(
                        rand.ints(0, 100).limit(i),
                        IntStream.of(-1)))
                .forEach(n -> System.out.format("%d ", n));
    }
}
/*
58 -1 55 93 -1 61 61 29 -1 68 0 22 7 -1 88 28 51 89 9 -1
 */
