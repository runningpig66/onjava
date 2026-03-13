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
 * 示例中引人了 concat(), 它会按照参数的顺序将两个流组合到一起。
 * <p>
 * 将流中的每个元素映射为独立的 IntStream，并将其扁平化合并为一个单一的流。
 * 此方法接收一个映射函数（mapper），对上游流中的每个元素进行转换，生成相应的 IntStream。
 * 随后，流处理引擎会将这些生成的离散流展平，并按顺序连接成一个连续的数值流。
 * 适用于需要将单个元素展开为多个数值序列的数据处理场景。
 * 参数：mapper 用于将当前流元素转换为 IntStream 的无状态函数；返回：扁平化合并后的新 IntStream。
 * IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper);
 * <p>
 * 惰性连接两个 IntStream。该方法会创建一个新的连续流，其元素由第一个输入流（a）的所有元素与第二个输入流（b）
 * 的所有元素按传入顺序拼接而成。此连接操作具有惰性求值（Lazy Evaluation）特性，仅在终端操作触发时才会依次从源流中拉取数据。
 * 架构注意：应避免通过循环进行深度嵌套或重复调用 concat 方法。深层嵌套的流在触发计算时会产生过深的调用链，极端情况下可能引发 StackOverflowError。
 * static IntStream concat(IntStream a, IntStream b)
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
