package ch20_generics;

import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/12/25 周四
 * @time 18:16
 * P.652 §20.3 泛型接口
 * Generate a Fibonacci sequence
 * <p>
 * 下面是 Supplier<T> 的第二种实现，这次是要生成斐波那契数列：
 * <p>
 * 虽然我们在该类的内外部使用的都是 int，但是类型参数是 Integer。这引出了 Java 泛型的限制之一：无法将基本类型作为类型参数。
 * 不过，Java 5 的自动装箱和自动拆箱机制实现了从基本类型到包装类型的双向转换。本例能实现这个效果，是因为该类可以无缝地使用和返回 int。
 */
public class Fibonacci implements Supplier<Integer> {
    private int count = 0;

    @Override
    public Integer get() {
        return fib(count++);
    }

    private int fib(int n) {
        if (n < 2) {
            return 1;
        }
        return fib(n - 2) + fib(n - 1);
    }

    public static void main(String[] args) {
        Stream.generate(new Fibonacci())
                .limit(18)
                .map(n -> n + " ")
                .forEach(System.out::print);
    }
}
/* Output:
1 1 2 3 5 8 13 21 34 55 89 144 233 377 610 987 1597 2584
 */
