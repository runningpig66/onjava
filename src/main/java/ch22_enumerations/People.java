package ch22_enumerations;

import java.util.List;

/**
 * @author runningpig66
 * @date 2月9日 周一
 * @time 19:51
 * P.056 §1.17 新特性：模式匹配 §1.17.3 支配性
 * {NewFeature} Preview in JDK 17
 * Compile with javac flags:
 * --enable-preview --source 17
 * Run with java flag: --enable-preview
 * <p>
 * 编译器只有在一个模式中的类型支配了另一个模式中的类型时，才能检测出支配性问题。它无法知道守卫中的逻辑是否会导致问题：
 */
record Person(String name, int age) {
}

public class People {
    static String categorize(Person person) {
        return switch (person) {
            case Person p when p.age() > 40 -> p + " is middle aged"; // [1]
            case Person p when (p.name().contains("D") || p.age() == 14) -> p + " D or 14";
            // Warning: Condition '!(p.age() >= 100)' is always 'true'
            case Person p when !(p.age() >= 100) -> p + " is not a centenarian"; // [2]
            // Warning: Switch label 'Person p' is unreachable
            case Person p -> p + " Everyone else";
        };
    }

    public static void main(String[] args) {
        List.of(
                new Person("Dorothy", 15),
                new Person("John Bigboote", 42),
                new Person("Morty", 14),
                new Person("Morty Jr.", 1),
                new Person("Jose", 39),
                new Person("Kane", 118)
        ).forEach(p -> System.out.println(categorize(p)));
    }
}
/* Output:
Person[name=Dorothy, age=15] D or 14
Person[name=John Bigboote, age=42] is middle aged
Person[name=Morty, age=14] D or 14
Person[name=Morty Jr., age=1] is not a centenarian
Person[name=Jose, age=39] is not a centenarian
Person[name=Kane, age=118] is middle aged
 */
