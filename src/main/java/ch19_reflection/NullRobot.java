package ch19_reflection;

import onjava.Null;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/12/23 周二
 * @time 17:51
 * P.632 §19.8 使用 Optional §19.8.1 标签接口
 * Using a dynamic proxy to create an Optional
 * <p>
 * 可能会有许多不同类型的 Robot，而且对于每种 Robot 类型，如果为 Null，则做一些特殊操作————
 * 本例中会提供 Robot 的确切类型信息。此信息由动态代理捕获：
 * <p>
 * 每当需要一个空的 Robot 对象时，调用 newNullRobot() 即可。传递给它想要的 Robot 类型。它会返回一个代理。
 * 代理会同时满足 Robot 和 Null 接口的要求，并提供它所代理的类型的特定名称。
 * <p>
 * notes: NullRobot.md
 * 利用标签接口与动态代理创建可识别的 Optional 对象——通用的“空对象模式” (Null Object Pattern)
 */
class NullRobotProxyHandler implements InvocationHandler {
    private String nullName;
    private Robot proxied = new NRobot();

    NullRobotProxyHandler(Class<? extends Robot> type) {
        nullName = type.getSimpleName() + " NullRobot";
    }

    private class NRobot implements Null, Robot {
        @Override
        public String name() {
            return nullName;
        }

        @Override
        public String model() {
            return nullName;
        }

        @Override
        public List<Operation> operations() {
            return Collections.emptyList();
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(proxied, args);
    }
}

public class NullRobot {
    public static Robot newNullRobot(Class<? extends Robot> type) {
        return (Robot) Proxy.newProxyInstance(
                NullRobot.class.getClassLoader(),
                new Class[]{Null.class, Robot.class},
                new NullRobotProxyHandler(type)
        );
    }

    public static void main(String[] args) {
        Stream.of(
                new SnowRobot("SnowBee"),
                newNullRobot(SnowRobot.class)
        ).forEach(Robot::test);
    }
}
/* Output:
Robot name: SnowBee
Robot model: SnowBot Series 11
SnowBee can shovel snow
SnowBee shoveling snow
SnowBee can chip ice
SnowBee chipping ice
SnowBee can clear the roof
SnowBee clearing roof
[Null Robot]
Robot name: SnowRobot NullRobot
Robot model: SnowRobot NullRobot
 */
