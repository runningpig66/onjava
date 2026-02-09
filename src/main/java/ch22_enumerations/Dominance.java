package ch22_enumerations;

/**
 * @author runningpig66
 * @date 2月9日 周一
 * @time 12:21
 * P.055 §1.17 新特性：模式匹配 §1.17.3 支配性
 * {NewFeature} Preview in JDK 17
 * Compile with javac flags:
 * --enable-preview --source 17
 * <p>
 * switch 中 case 语句的顺序很重要。如果基类先出现，就会支配任何出现在后面的 case：支配即 dominate，
 * 如果前面 case 的判断条件所覆盖的范围包含后面的 case，即可认为前者支配了后者，也就是说后者实际上不会得到执行的机会了。
 */
sealed interface Base permits Derived {
}

record Derived() implements Base {
}

public class Dominance {
    static String test(Base base) {
        return switch (base) {
            case Derived d -> "Derived";
            case Base b -> "B"; // [1]
        };
    }
}
