package ch14_streams;

import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/11/10 周一
 * @time 3:29
 * 代码清单 P.411 终结操作：匹配
 * <p>
 * 当使用所提供的 Predicate 检测流中的元素时，如果每一个元素都得到 true, 则返回 true.
 * 在遇到第一个 false 时，会短路计算。也就是说，在找到一个 false 之后，它不会继续计算。
 * boolean allMatch(Predicate<? super T> predicate);
 * <p>
 * 当使用所提供的 Predicate 检测流中的元素时，如果有任何一个元素能得到 true, 则返回 true.
 * 在遇到第一个 true 时，会短路计算。
 * boolean anyMatch(Predicate<? super T> predicate);
 * <p>
 * 当使用所提供的 Predicate 检测流中的元素时，如果没有元素得到 true, 则返回 true.
 * 在遇到第一个 true 时，会短路计算。
 * boolean noneMatch(Predicate<? super T> predicate);
 */
interface Matcher extends BiPredicate<Stream<Integer>, Predicate<Integer>> {
}

public class Matching {
    static void show(Matcher match, int val) {
        System.out.println(
                // 对 match.test() 的调用会被翻译成对 Stream::*Match 函数的调用。
                match.test(
                        IntStream.rangeClosed(1, 9)
                                .boxed()
                                // peek() 表明在短路发生之前测试已经走了多远。从输出中可以看到短路计算行为。
                                .peek(n -> System.out.format("%d ", n)),
                        n -> n < val));
    }

    public static void main(String[] args) {
        show(Stream::allMatch, 10);
        show(Stream::allMatch, 4);
        show(Stream::anyMatch, 2);
        show(Stream::anyMatch, 0);
        show(Stream::noneMatch, 5);
        show(Stream::noneMatch, 0);
    }
}
/* Output:
1 2 3 4 5 6 7 8 9 true
1 2 3 4 false
1 true
1 2 3 4 5 6 7 8 9 false
1 false
1 2 3 4 5 6 7 8 9 true
 */
