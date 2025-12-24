package ch19_reflection;

import java.util.Arrays;
import java.util.List;

/**
 * @author runningpig66
 * @date 2025/12/23 周二
 * @time 17:36
 * P.631 §19.8 使用 Optional §19.8.1 标签接口
 * <p>
 * 现在可以创建一个扫雪的 Robot：
 */
public class SnowRobot implements Robot {
    private String name;

    public SnowRobot(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String model() {
        return "SnowBot Series 11";
    }

    private List<Operation> ops = Arrays.asList(
            new Operation(
                    () -> name + " can shovel snow",
                    () -> System.out.println(name + " shoveling snow")
            ),
            new Operation(
                    () -> name + " can chip ice",
                    () -> System.out.println(name + " chipping ice")
            ),
            new Operation(
                    () -> name + " can clear the roof",
                    () -> System.out.println(name + " clearing roof")
            )
    );

    @Override
    public List<Operation> operations() {
        return ops;
    }

    public static void main(String[] args) {
        Robot.test(new SnowRobot("Slusher"));
    }
}
/* Output:
Robot name: Slusher
Robot model: SnowBot Series 11
Slusher can shovel snow
Slusher shoveling snow
Slusher can chip ice
Slusher chipping ice
Slusher can clear the roof
Slusher clearing roof
 */
