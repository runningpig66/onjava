package ch20_generics;

/**
 * @author runningpig66
 * @date 2026-01-23
 * @time 20:13
 * P.736 §20.15 潜在类型机制 §20.15.4 Java 中的直接潜在类型机制
 * Removing the generic; code still works
 * <p>
 * 然而，注意 perform() 的运行并不需要泛型，可以指定它接受 Performs 对象：
 * 在这里，泛型并不是必需的，因为这些类已经被强制实现了 Performs 接口。
 */
class CommunicateSimply {
    static void perform(Performs performer) {
        performer.speak();
        performer.sit();
    }
}

public class SimpleDogsAndRobots {
    public static void main(String[] args) {
        CommunicateSimply.perform(new PerformingDog());
        CommunicateSimply.perform(new Robot());
    }
}
/* Output:
Woof!
Sitting
Click!
Clank!
 */
