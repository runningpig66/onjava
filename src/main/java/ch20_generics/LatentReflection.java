package ch20_generics;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author runningpig66
 * @date 2026-01-23
 * @time 20:51
 * P.736 §20.16 对于缺少（直接的）潜在类型机制的补偿 §20.16.1 反射
 * Using reflection for latent typing
 * <p>
 * 虽然 Java 没有直接支持潜在类型机制，但这并不意味着泛型代码就无法跨类型层次应用。你可以创建出真正意义上的泛型代码，
 * 但是需要花些额外的功夫。一种可选方案是反射。下面是实现了潜在类型机制的反射 perform()：
 * <p>
 * 此处，这两个类之间并无直接关联，也没有共同的基类（除了 Object）或接口。通过反射，CommunicateReflectively.perform()
 * 可以动态地确定所需的方法是否可用，然后进行调用。它甚至可以处理 Mime 只有一个必要方法的情况，并部分地实现了它的目标。
 */
// Does not implement Performs:
class Mime {
    public void walkAgainstTheWind() {
    }

    public void sit() {
        System.out.println("Pretending to sit");
    }

    public void pushInvisibleWalls() {
    }

    @Override
    public String toString() {
        return "Mime";
    }
}

// Does not implement Performs:
class SmartDog {
    public void speak() {
        System.out.println("Woof!");
    }

    public void sit() {
        System.out.println("Sitting");
    }

    public void reproduce() {
    }
}

class CommunicateReflectively {
    public static void perform(Object speaker) {
        Class<?> spkr = speaker.getClass();
        try {
            try {
                Method speak = spkr.getMethod("speak");
                speak.invoke(speaker);
            } catch (NoSuchMethodException e) {
                System.out.println(speaker + " cannot speak");
            }
            try {
                Method sit = spkr.getMethod("sit");
                sit.invoke(speaker);
            } catch (NoSuchMethodException e) {
                System.out.println(speaker + " cannot sit");
            }
        } catch (SecurityException |
                 IllegalAccessException |
                 IllegalArgumentException |
                 InvocationTargetException e) {
            throw new RuntimeException(speaker.toString(), e);
        }
    }
}

public class LatentReflection {
    public static void main(String[] args) {
        CommunicateReflectively.perform(new SmartDog());
        CommunicateReflectively.perform(new Robot());
        CommunicateReflectively.perform(new Mime());
    }
}
/* Output:
Woof!
Sitting
Click!
Clank!
Mime cannot speak
Pretending to sit
 */
