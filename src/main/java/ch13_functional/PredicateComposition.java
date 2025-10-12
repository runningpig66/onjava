package ch13_functional;

import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/10/12 周日
 * @time 11:45
 * 代码清单 P.371
 * 函数组合 - 以下示例演示了 Predicate（谓词）的逻辑运算：
 */
public class PredicateComposition {
    static Predicate<String>
            p1 = s -> s.contains("bar"),
            p2 = s -> s.length() < 5,
            p3 = s -> s.contains("foo"),
            p4 = p1.negate().and(p2).or(p3);

    public static void main(String[] args) {
        Stream.of("bar", "foobar", "foobaz", "fongopuckey")
                .filter(p4)
                .forEach(System.out::println);
    }
}
/* Output:
foobar
foobaz
 */
