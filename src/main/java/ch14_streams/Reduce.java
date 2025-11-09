package ch14_streams;

import java.util.Random;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/11/10 周一
 * @time 2:40
 * 代码清单 P.410 终结操作：组合所有的流元素
 * <p>
 * 使用 BinaryOperator 来组合所有的流元素。因为这个流可能为空，所以返回的是一个 Optional。
 * Optional<T> reduce(BinaryOperator<T> accumulator);
 */
class Frobnitz {
    int size;

    Frobnitz(int sz) {
        size = sz;
    }

    @Override
    public String toString() {
        return "Frobnitz(" + size + ")";
    }

    // Generator:
    static Random rand = new Random(47);
    static final int BOUND = 100;

    static Frobnitz supply() {
        return new Frobnitz(rand.nextInt(BOUND));
    }
}

public class Reduce {
    public static void main(String[] args) {
        Stream.generate(Frobnitz::supply)
                .limit(10)
                .peek(System.out::println)
                /* lambda 表达式中的第一个参数 fr0 是上次调用这个 reduce() 时带回的结果，第二个参数 fr1 是来自流中的新值。
                reduce()中的 lambda 表达式使用了一个三元选择操作符，如果 fr0 的 size 小于 50, 就接受 fr0,
                否则就接受 fr1, 也就是序列中的下一个元素。作为结果，我们得到的是流中第一个 size 小于 50 的 Frobnitz————
                一旦找到了一个这样的对象，它就会抓住不放，哪怕还会出现其他候选。尽管这个约束相当奇怪，但它确实让我们对 reduce() 有了更多的了解。*/
                .reduce((fr0, fr1) -> fr0.size < 50 ? fr0 : fr1)
                .ifPresent(System.out::println);
    }
}
/* OutPut:
Frobnitz(58)
Frobnitz(55)
Frobnitz(93)
Frobnitz(61)
Frobnitz(61)
Frobnitz(29)
Frobnitz(68)
Frobnitz(0)
Frobnitz(22)
Frobnitz(7)
Frobnitz(29)
 */
