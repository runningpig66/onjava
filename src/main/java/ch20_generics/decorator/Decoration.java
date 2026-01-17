package ch20_generics.decorator;

import java.util.Date;

/**
 * @author runningpig66
 * @date 2026/1/15 周四
 * @time 1:46
 * P.728 §20.14 混型 §20.14.3 使用装饰器模式
 * {java generics.decorator.Decoration}
 * <p>
 * 如果你仔细看看混型的使用方法，就会发现混型的概念和装饰器设计模式（Decorator）的关系看起来非常紧密。
 * 装饰器常被用于满足各种可能的组合，简单的子类化会产生大量的类，导致这种方法显得不现实。
 * <p>
 * 装饰器模式使用了分层的对象来动态、透明地为个别对象添加职责。该模式指定了所有用于包装你的原始对象的对象都有着相同的基础接口。
 * 某个类是可装饰的，然后你通过将其他的类包装在其上，来分层叠加功能。这使得装饰器是透明的————有一个公共的消息集，
 * 不论一个对象是否被装饰，你都可以向它发送该消息集。用于装饰的类也同样可以添加方法，不过你会看到，这其中有一定的局限性。
 * <p>
 * 装饰器是通过组合和规范的结构（可装饰物 + 装饰器的层次结构）实现的，而混型是基于继承的。
 * 可以将基于参数化类型的混型想象成一种泛型装饰器的机制，这种机制不要求具有装饰器设计模式的继承结构。
 * 前面的示例可以用装饰器来改写:
 * <p>
 * 由混型所产生的类包含了所有需要的方法，但是使用适配器产生的对象类型是该对象最后一层被装饰的类型。
 * 也就是说，虽然可以添加不止一层，但是最后一层才是实际的类型，因此只有最后一层的方法是可见的，
 * 而混型的类型则是所有被混合在一起的类型。因此，装饰器的一个显著缺点是它只能有效应用于一层装饰（也就是最后那层）之上，
 * 而混型的方式则显然更自然一些。因此，装饰器只是对混型所能解决问题的一种比较局限的方案。
 */
class Basic {
    private String value;

    public void set(String val) {
        value = val;
    }

    public String get() {
        return value;
    }
}

class Decorator extends Basic {
    protected Basic basic;

    Decorator(Basic basic) {
        this.basic = basic;
    }

    @Override
    public void set(String val) {
        basic.set(val);
    }

    @Override
    public String get() {
        return basic.get();
    }
}

class TimeStamped extends Decorator {
    private final long timeStamp;

    TimeStamped(Basic basic) {
        super(basic);
        timeStamp = new Date().getTime();
    }

    public long getStamp() {
        return timeStamp;
    }
}

class SerialNumbered extends Decorator {
    private static long counter = 1;
    private final long serialNumber = counter++;

    SerialNumbered(Basic basic) {
        super(basic);
    }

    public long getSerialNumber() {
        return serialNumber;
    }
}

public class Decoration {
    public static void main(String[] args) {
        TimeStamped t = new TimeStamped(new Basic());
        TimeStamped t2 = new TimeStamped(new SerialNumbered(new Basic()));
        //- t2.getSerialNumber(); // Not available
        SerialNumbered s = new SerialNumbered(new Basic());
        SerialNumbered s2 = new SerialNumbered(new TimeStamped(new Basic()));
        //- s2.getStamp(); // Not available
    }
}
