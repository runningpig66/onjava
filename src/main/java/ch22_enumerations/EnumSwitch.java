package ch22_enumerations;

/**
 * @author runningpig66
 * @date 2月9日 周一
 * @time 1:55
 * P.045 §1.15 新特性：将 switch 作为表达式
 * {NewFeature} Since JDK 14
 * <p>
 * （SwitchExpression.java 中的）arrow() 会产生和 colon() 相同的效果，但是要注意这样的语法会更清晰、更紧凑，也更容易读。
 * 可以看看这种方式为 TrafficLight.java 带来的改进（相较于在本章前面实现的版本）：
 */
public class EnumSwitch {
    enum Signal {GREEN, YELLOW, RED,}

    Signal color = Signal.RED;

    public void change() {
        color = switch (color) {
            case RED -> Signal.GREEN;
            case GREEN -> Signal.YELLOW;
            case YELLOW -> Signal.RED;
        };
    }
}
