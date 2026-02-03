package ch20_generics;

import ch19_reflection.pets.Dog;

/**
 * @author runningpig66
 * @date 2026-01-23
 * @time 18:39
 * P.735 §20.15 潜在类型机制 §20.15.4 Java 中的直接潜在类型机制
 * No (direct) latent typing in Java
 * <p>
 * 由于 Java 较晚才加入泛型，没有办法实现任何类型的潜在类型机制，因此 Java 没有支持这项特性。
 * 所以相较于支持潜在类型机制的语言，Java 的泛型机制起初看起来“不够泛型”（Java 用类型擦除实现的泛型有时称为第二类泛型类型）。
 * 举例来说，如果试图在 Java 8 之前实现 dog 和 robots 的例子，就必须用到一个类或接口，并将其指定在边界表达式中：
 */
class PerformingDog extends Dog implements Performs {
    @Override
    public void speak() {
        System.out.println("Woof!");
    }

    @Override
    public void sit() {
        System.out.println("Sitting");
    }

    public void reproduce() {
    }
}

class Robot implements Performs {
    @Override
    public void speak() {
        System.out.println("Click!");
    }

    @Override
    public void sit() {
        System.out.println("Clank!");
    }

    public void oilChange() {
    }
}

class Communicate {
    public static <T extends Performs> void perform(T performer) {
        performer.speak();
        performer.sit();
    }
}

public class DogsAndRobots {
    public static void main(String[] args) {
        Communicate.perform(new PerformingDog());
        Communicate.perform(new Robot());
    }
}
/* Output:
Woof!
Sitting
Click!
Clank!
 */
