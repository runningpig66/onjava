package ch22_enumerations;

import java.util.List;

/**
 * @author runningpig66
 * @date 2月9日 周一
 * @time 20:09
 * P.057 §1.17 新特性：模式匹配 §1.17.4 覆盖范围
 * {NewFeature} Preview in JDK 17
 * Compile with javac flags:
 * --enable-preview --source 17
 * Run with java flag: --enable-preview
 * <p>
 * 模式匹配会引导你逐渐使用 sealed 关键字，这有助于确保你已覆盖了所有可能传入选择器表达式的类型。不过接下来再看一个示例：
 */
sealed interface Transport {
}

record Bicycle(String id) implements Transport {
}

record Glider(int size) implements Transport {
}

record Surfboard(double weight) implements Transport {
}
// If you uncomment this:
// record Skis(int length) implements Transport {}
// You get an error: "the switch expression
// does not cover all possible input values"

public class SealedPatternMatch {
    static String exhaustive(Transport t) {
        return switch (t) {
            case Bicycle b -> "Bicycle " + b.id();
            case Glider g -> "Glider " + g.size();
            case Surfboard s -> "Surfboard " + s.weight();
        };
    }

    public static void main(String[] args) {
        List.of(
                new Bicycle("Bob"),
                new Glider(65),
                new Surfboard(6.4)
        ).forEach(t -> System.out.println(exhaustive(t)));
        try {
            exhaustive(null); // Always possible! // [1]
        } catch (NullPointerException e) {
            System.out.println("Not exhaustive: " + e);
        }
    }
}
/* Output:
Bicycle Bob
Glider 65
Surfboard 6.4
Not exhaustive: java.lang.NullPointerException
 */
