package ch22_enumerations;

import java.util.function.Consumer;

/**
 * @author runningpig66
 * @date 2月9日 周一
 * @time 0:44
 * P.042 §1.14 新特性：switch 中的 case null
 * {NewFeature} Preview in JDK 17
 * Compile with javac flags:
 * --enable-preview --source 17
 * Run with java flag: --enable-preview
 * <p>
 * JDK 17 新增了（预览）功能，可以在 switch 中引入原本非法的 case null。以前只能在 switch 的外部检查是否为 null，如 old() 中所示：
 * [Java 21 Update] null 标签必须独立使用，或者只能与 default 标签组合（case null, default），不可与具体常量值混合使用。
 */
public class CaseNull {
    static void old(String s) {
        if (s == null) {
            System.out.println("null");
            return;
        }
        switch (s) {
            case "XX" -> System.out.println("XX");
            default -> System.out.println("default");
        }
    }

    static void checkNull(String s) {
        switch (s) {
            case "XX" -> System.out.println("XX");
            case null -> System.out.println("null");
            default -> System.out.println("default");
        }
        // Works with colon syntax, too:
        switch (s) {
            case "XX":
                System.out.println("XX");
                break;
            case null:
                System.out.println("null");
                break;
            default:
                System.out.println("default");
        }
    }

    static void defaultOnly(String s) {
        switch (s) {
            case "XX" -> System.out.println("XX");
            default -> System.out.println("default");
        }
    }

    static void combineNullAndCase(String s) {
        switch (s) {
            // Error: Invalid case label combination:
            // 'null' can only be used as a single case label or paired only with 'default'
            //- case "XX", null -> System.out.println("XX|null");
            case "XX" -> System.out.println("XX");
            case null -> System.out.println("null");
            default -> System.out.println("default");
        }
    }

    static void combineNullAndDefault(String s) {
        switch (s) {
            case "XX" -> System.out.println("XX");
            case null, default -> System.out.println("both");
        }
    }

    static void test(Consumer<String> cs) {
        cs.accept("XX");
        cs.accept("YY");
        try {
            cs.accept(null);
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        test(CaseNull::old);
        test(CaseNull::checkNull);
        test(CaseNull::defaultOnly);
        test(CaseNull::combineNullAndCase);
        test(CaseNull::combineNullAndDefault);
    }
}
/* Output:
XX
default
null
XX
XX
default
default
null
null
XX
default
Cannot invoke "String.hashCode()" because "<local1>" is null
XX
default
null
XX
both
both
 */
