package ch22_enumerations;

/**
 * @author runningpig66
 * @date 2月9日 周一
 * @time 2:03
 * P.046 §1.15 新特性：将 switch 作为表达式
 * {NewFeature} Since JDK 14
 * <p>
 * 如果一个 case 需要多个语句或表达式，那就将它们放在一对花括号中。
 * 注意，在从多行代码组成的 case 表达式中生成结果时，即便使用了箭头语法，也要使用 yield。
 */
enum CelestialBody {
    MERCURY, VENUS, EARTH, MARS, JUPITER, SATURN, URANUS, NEPTUNE, PLUTO
}

public class Planets {
    public static String classify(CelestialBody b) {
        var result = switch (b) {
            case MERCURY, VENUS, EARTH, MARS,
                 JUPITER, SATURN, URANUS, NEPTUNE -> {
                System.out.print("A planet: ");
                yield b.toString();
            }
            case PLUTO -> {
                System.out.print("Not a planet: ");
                yield b.toString();
            }
        };
        return result;
    }

    public static void main(String[] args) {
        System.out.println(classify(CelestialBody.MARS));
        System.out.println(classify(CelestialBody.PLUTO));
    }
}
/* Output:
A planet: MARS
Not a planet: PLUTO
 */
