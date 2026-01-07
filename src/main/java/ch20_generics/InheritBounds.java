package ch20_generics;

import java.awt.*;

/**
 * @author runningpig66
 * @date 2025/12/31 周三
 * @time 17:06
 * P.687 §20.8 边界
 * <p>
 * 你可能会发现，BasicBounds.java 似乎有些可以通过继承消除的冗余信息。下面可以看到，每一层继承也会增加边界的限制：
 * <p>
 * HoldItem 持有一个对象，因此该行为继承到了 WithColor2 中，WithColor2 同样要求其参数和 HasColor 一致。
 * WithColorCoord2 和 Solid2 进一步扩展了该继承结构，对每一层都增加了边界。现在这些方法都被继承下来了，而不用在每个类里面再去重复定义。
 */
class HoldItem<T> {
    T item;

    HoldItem(T item) {
        this.item = item;
    }

    T getItem() {
        return item;
    }
}

class WithColor2<T extends HasColor> extends HoldItem<T> {
    WithColor2(T item) {
        super(item);
    }

    Color color() {
        return item.getColor();
    }
}

class WithColorCoord2<T extends Coord & HasColor> extends WithColor2<T> {
    WithColorCoord2(T item) {
        super(item);
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

class Solid2<T extends Coord & HasColor & Weight> extends WithColorCoord2<T> {
    Solid2(T item) {
        super(item);
    }

    int weight() {
        return item.weight();
    }
}

public class InheritBounds {
    public static void main(String[] args) {
        Solid2<Bounded> solid2 = new Solid2<>(new Bounded());
        solid2.color();
        solid2.getY();
        solid2.weight();
    }
}
