package ch22_enumerations;

/**
 * @author runningpig66
 * @date 2月9日 周一
 * @time 1:30
 * P.044 §1.15 新特性：将 switch 作为表达式
 * {NewFeature} Since JDK 14
 * <p>
 * switch 一直以来都只是一个语句，并不会生成结果。JDK 14 使得 switch 还可以作为一个表达式来使用，因此它可以得到一个值：
 */
public class SwitchExpression {
    static int colon(String s) {
        var result = switch (s) {
            case "i":
                yield 1;
            case "j":
                yield 2;
            case "k":
                yield 3;
            default:
                yield 0;
        };
        return result;
    }

    static int arrow(String s) {
        var result = switch (s) {
            case "i" -> 1;
            case "j" -> 2;
            case "k" -> 3;
            default -> 0;
        };
        return result;
    }

    public static void main(String[] args) {
        for (var s : new String[]{"i", "j", "k", "z"}) {
            System.out.format("%s %d %d%n", s, colon(s), arrow(s));
        }
    }
}
/* Output:
i 1 1
j 2 2
k 3 3
z 0 0
 */
