package ch20_generics;

/**
 * @author runningpig66
 * @date 2026/1/12 周一
 * @time 17:00
 * P.716 §20.11 自限定类型 §20.11.2 自限定
 * <p>
 * 自限定执行了额外的一步，强制将泛型作为自已的边界参数使用。接下来看看这样写出的类能用来做什么，又不能做什么：
 * <p>
 * 对参数进行自限定，带来了什么额外的价值呢？类型参数必须和要定义的类是同一种类型。正如你在类 B 的定义中所看到的，
 * 你也可以从使用了另一个 SelfBounded 参数的 SelfBounded 派生出类，尽管你看到的主要用法似乎是用于类 A 的。
 * 从试图定义 E 的那一行可以看出，你无法将非 SelfBounded 的类型作为类型参数。
 */
class SelfBounded<T extends SelfBounded<T>> {
    T element;

    SelfBounded<T> set(T arg) {
        element = arg;
        return this;
    }

    T get() {
        return element;
    }
}

class A extends SelfBounded<A> {
}

// 自限定类型只能保证参数处于继承体系内，却无法强制它就是当前类本身。
class B extends SelfBounded<A> { // Also OK
}

class C extends SelfBounded<C> {
    C setAndGet(C arg) {
        set(arg);
        return get();
    }
}

class D {
    // Can't do this:
    //- class E extends SelfBounded<D> {}
    // Compile error: type argument D is not within bounds of type-variable T
}

// Alas, you can do this, so you cannot force the idiom:
class F extends SelfBounded { // Warning: Raw use of parameterized class 'SelfBounded'
}

public class SelfBounding {
    public static void main(String[] args) {
        A a = new A();
        a.set(new A());
        a = a.set(new A()).get();
        a = a.get();
        C c = new C();
        c = c.setAndGet(new C());
    }
}
