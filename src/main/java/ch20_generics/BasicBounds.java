package ch20_generics;

import java.awt.*;

/**
 * @author runningpig66
 * @date 2025/12/31 周三
 * @time 15:58
 * P.686 §20.8 边界
 * <p>
 * 本章在之前对边界做过简单的介绍。边界让你在使用泛型的时候，可以在参数类型上增加限制。
 * 虽然这可以强制执行应用泛型的类型规则，但更重要的潜在效果是，你可以用边界类型上的方法了。
 * <p>
 * 由于类型擦除移除了类型信息，对于无边界的泛型参数，你仅能调用 Object 中可用的方法。
 * 不过如果能够将参数类型限制在某个类型子集中，你就可以调用该子集上可用的方法了。
 * 为了应用这种限制，Java 泛型复用了 extends 关键字。
 * <p>
 * 相较于 extends 关键字的常规用法，它在泛型边界上下文中代表着完全不同的意义，理解这一点非常重要。
 * 下面这个示例演示了边界的一些基本要素；你可能会发现，BasicBounds.java 似乎有些可以通过继承消除的冗余信息。
 */
interface HasColor {
    Color getColor();
}

class WithColor<T extends HasColor> {
    T item;

    WithColor(T item) {
        this.item = item;
    }

    T getItem() {
        return item;
    }

    // The bound allows you to call a method:
    Color color() {
        return item.getColor();
    }
}

class Coord {
    public int x, y, z;
}

// This fails. Class must be first, then interfaces:
// class WithColorCoord<T extends HasColor & Coord> {

// Multiple bounds:
class WithColorCoord<T extends Coord & HasColor> {
    T item;

    WithColorCoord(T item) {
        this.item = item;
    }

    T getItem() {
        return item;
    }

    Color color() {
        return item.getColor();
    }

    int getX() {
        return item.x;
    }

    int getY() {
        return item.y;
    }

    int getZ() {
        return item.z;
    }
}

interface Weight {
    int weight();
}

// As with inheritance, you can have only one concrete class but multiple interfaces:
class Solid<T extends Coord & HasColor & Weight> {
    T item;

    Solid(T item) {
        this.item = item;
    }

    T getItem() {
        return item;
    }

    Color color() {
        return item.getColor();
    }

    int getX() {
        return item.x;
    }

    int getY() {
        return item.y;
    }

    int getZ() {
        return item.z;
    }

    int weight() {
        return item.weight();
    }
}

class Bounded extends Coord implements HasColor, Weight {
    @Override
    public Color getColor() {
        return null;
    }

    @Override
    public int weight() {
        return 0;
    }
}

public class BasicBounds {
    public static void main(String[] args) {
        Solid<Bounded> solid = new Solid<>(new Bounded());
        solid.color();
        solid.getY();
        solid.weight();
    }
}
