package ch20_generics;

import java.util.Date;

/**
 * @author runningpig66
 * @date 2026/1/15 周四
 * @time 1:03
 * P.726 §20.14 混型 §20.14.2 与接口混合
 * <p>
 * 一种常见的推荐方案是使用接口来达到混型的效果，就像这样：
 * <p>
 * Mixin 类基本上是使用了委托机制（delegation），因此每个被混入的类型都需要在 Mixin 中有一个字段，
 * 而你必须在 Mixin 中编写所有必要的方法来将调用转发到合适的对象上。本例中的类都很简单，如果是更复杂的混型，则代码量会大幅增长。
 */
interface TimeStamped {
    long getStamp();
}

class TimeStampedImp implements TimeStamped {
    private final long timeStamp;

    TimeStampedImp() {
        timeStamp = new Date().getTime();
    }

    @Override
    public long getStamp() {
        return timeStamp;
    }
}

interface SerialNumbered {
    long getSerialNumber();
}

class SerialNumberedImp implements SerialNumbered {
    private static long counter = 1;
    private final long serialNumber = counter++;

    @Override
    public long getSerialNumber() {
        return serialNumber;
    }
}

interface Basic {
    void set(String val);

    String get();
}

class BasicImp implements Basic {
    private String value;

    @Override
    public void set(String val) {
        value = val;
    }

    @Override
    public String get() {
        return value;
    }
}

// 这个例子本质上是委托（Delegation）。这就是手动实现的委托，目的是为了模拟多重继承。
class Mixin extends BasicImp implements TimeStamped, SerialNumbered {
    private TimeStamped timeStamp = new TimeStampedImp();
    private SerialNumbered serialNumber = new SerialNumberedImp();

    @Override
    public long getStamp() {
        return timeStamp.getStamp();
    }

    @Override
    public long getSerialNumber() {
        return serialNumber.getSerialNumber();
    }
}

public class Mixins {
    public static void main(String[] args) {
        Mixin mixin1 = new Mixin(), mixin2 = new Mixin();
        mixin1.set("test string 1");
        mixin2.set("test string 2");
        System.out.println(mixin1.get() + " " + mixin1.getStamp() + " " + mixin1.getSerialNumber());
        System.out.println(mixin2.get() + " " + mixin2.getStamp() + " " + mixin2.getSerialNumber());
    }
}
/* Output:
test string 1 1768411262991 1
test string 2 1768411262991 2
 */
