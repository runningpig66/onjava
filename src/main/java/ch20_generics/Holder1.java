package ch20_generics;

/**
 * @author runningpig66
 * @date 2025/12/25 周四
 * @time 1:37
 * P.643 §20.2 简单泛型
 * <p>
 * 我们来看一个类。这个类持有一个简单的对象，并可以指定对象的具体类型，就像这样：
 * <p>
 * 这个工具的复用性并不高，因为它无法用来持有任何其他的东西。我们不会想去为每个遇到的类型都写一份相同的代码。
 */
class Automobile {
}

public class Holder1 {
    private Automobile a;

    public Holder1(Automobile a) {
        this.a = a;
    }

    Automobile get() {
        return a;
    }
}
