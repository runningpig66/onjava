package ch20_generics;

import ch19_reflection.pets.Dog;

import java.util.function.Consumer;

/**
 * @author runningpig66
 * @date 2026-01-27 周二
 * @time 19:05
 * P.741 §20.17 Java 8 中的辅助潜在类型机制
 * "Assisted Latent Typing"
 */
class PerformingDogA extends Dog {
    public void speak() {
        System.out.println("Woof!");
    }

    public void sit() {
        System.out.println("Sitting");
    }

    public void reproduce() {
    }
}

class RobotA {
    public void speak() {
        System.out.println("Click!");
    }

    public void sit() {
        System.out.println("Clank!");
    }

    public void oilChange() {
    }
}

class CommunicateA {
    public static <P> void perform(P performer, Consumer<P> action1, Consumer<P> action2) {
        action1.accept(performer);
        action2.accept(performer);
    }
}

public class DogsAndRobotMethodReferences {
    public static void main(String[] args) {
        CommunicateA.perform(new PerformingDogA(), PerformingDogA::speak, PerformingDogA::sit);
        CommunicateA.perform(new RobotA(), RobotA::speak, RobotA::sit);
        CommunicateA.perform(new Mime(), Mime::walkAgainstTheWind, Mime::pushInvisibleWalls);
    }
}
/* Output:
Woof!
Sitting
Click!
Clank!
 */
