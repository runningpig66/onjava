package ch22_enumerations;

import java.util.List;

/**
 * @author runningpig66
 * @date 2月9日 周一
 * @time 11:53
 * P.054 §1.17 新特性：模式匹配 §1.17.2 守卫
 * {NewFeature} Preview in JDK 17
 * Compile with javac flags:
 * --enable-preview --source 17
 * Run with java flag: --enable-preview
 * <p>
 * 下面是个更复杂的守卫应用示例。Tank（罐子）可以持有不同类型的液体，而液体的 Level（浓度）必须在 0%~100% 范围内。
 */
enum Type {TOXIC, FLAMMABLE, NEUTRAL}

record Level(int percent) {
    Level {
        if (percent < 0 || percent > 100) {
            throw new IndexOutOfBoundsException(percent + " percent");
        }
    }
}

record Tank(Type type, Level level) {
}

public class Tanks {
    static String check(Tank tank) {
        return switch (tank) {
            case Tank t when t.type() == Type.TOXIC -> "Toxic: " + t;
            // 支配即 dominate，如果前面 case 的判断条件所覆盖的范围包含后面的 case，即可认为前者支配了后者，也就是说后者实际上不会得到执行的机会了。
            // Warning: Condition 't.type() == Type.TOXIC && t.level().percent() < 50' is always 'false'
            case Tank t when ( // [1] 如果守卫包含多个表达式，简单地将其放在圆括号内即可。
                    t.type() == Type.TOXIC && t.level().percent() < 50
            ) -> "Toxic, low: " + t;
            case Tank t when t.type() == Type.FLAMMABLE -> "Flammable: " + t;
            // Equivalent to "default":
            case Tank t -> "Other Tank: " + t;
        };
    }

    public static void main(String[] args) {
        List.of(new Tank(Type.TOXIC, new Level(49)),
                new Tank(Type.FLAMMABLE, new Level(52)),
                new Tank(Type.NEUTRAL, new Level(75))
        ).forEach(t -> System.out.println(check(t)));
    }
}
/* Output:
Toxic: Tank[type=TOXIC, level=Level[percent=49]]
Flammable: Tank[type=FLAMMABLE, level=Level[percent=52]]
Other Tank: Tank[type=NEUTRAL, level=Level[percent=75]]
 */
