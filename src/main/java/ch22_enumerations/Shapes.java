package ch22_enumerations;

import java.util.List;

/**
 * @author runningpig66
 * @date 2月9日 周一
 * @time 11:32
 * P.052 §1.17 新特性：模式匹配 §1.17.2 守卫
 * {NewFeature} Preview in JDK 17
 * Compile with javac flags:
 * --enable-preview --source 17
 * Run with java flag: --enable-preview
 * <p>
 * 守卫（guard）使你可以进一步细化匹配条件，而不只是简单地匹配类型。它是出现在类型判断和 && 后的一项测试。
 * 守卫可以是任何布尔表达式。如果选择器表达式和 case 的类型相同，并且守卫判断为 true，那么模式就匹配上了：
 * <p>
 * 注：Java 最终在正式版中弃用了 && 作为 switch 守卫的连接符，改用了专门的关键字 when。
 * 使用 && 会让编译器误以为你在尝试进行位运算或逻辑运算，从而导致语法解析错误。
 * 从 JDK 19 开始，预览版的 && 守卫正式变更为 when 关键字，并一直延续到 JDK 21 正式版。
 */
sealed interface Shape permits Circle, Rectangle {
    double area();
}

record Circle(double radius) implements Shape {
    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
}

record Rectangle(double side1, double side2) implements Shape {
    @Override
    public double area() {
        return side1 * side2;
    }
}

public class Shapes {
    static void classify(Shape s) {
        System.out.println(switch (s) {
            case Circle c when c.area() < 100.0 -> "Small Circle: " + c;
            case Circle c -> "Large Circle: " + c;
            case Rectangle r when r.side1() == r.side2() -> "Square: " + r;
            case Rectangle r -> "Rectangle: " + r;
        });
    }

    public static void main(String[] args) {
        List.of(
                new Circle(5.0),
                new Circle(25.0),
                new Rectangle(12.0, 12.0),
                new Rectangle(12.0, 15.0)
        ).forEach(t -> classify(t));
    }
}
/* Output:
Small Circle: Circle[radius=5.0]
Large Circle: Circle[radius=25.0]
Square: Rectangle[side1=12.0, side2=12.0]
Rectangle: Rectangle[side1=12.0, side2=15.0]
 */
