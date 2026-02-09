package ch22_enumerations;

import java.util.stream.IntStream;

/**
 * @author runningpig66
 * @date 2月9日 周一
 * @time 0:38
 * P.041 §1.13 新特性：switch 中的箭头语法
 * {NewFeature} Since JDK 14
 * <p>
 * JDK 14 增加了在 switch 中使用一种不同语法的 case 的能力。在下面的示例中，colons() 用的是旧方式，arrows() 用的是新方式：
 */
public class ArrowInSwitch {
    static void colons(int i) {
        switch (i) {
            case 1:
                System.out.println("one");
                break;
            case 2:
                System.out.println("two");
                break;
            case 3:
                System.out.println("three");
                break;
            default:
                System.out.println("default");
        }
    }

    static void arrows(int i) {
        switch (i) {
            case 1 -> System.out.println("one");
            case 2 -> System.out.println("two");
            case 3 -> System.out.println("three");
            default -> System.out.println("default");
        }
    }

    public static void main(String[] args) {
        IntStream.range(0, 4).forEach(i -> colons(i));
        IntStream.range(0, 4).forEach(i -> arrows(i));
    }
}
/* Output:
default
one
two
three
default
one
two
three
 */
