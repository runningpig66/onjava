package ch20_generics;

/**
 * @author runningpig66
 * @date 2026/1/12 周一
 * @time 19:55
 * P.717 §20.11 自限定类型 §20.11.2 自限定
 * <p>
 * 注意，你可以移除该限制，所有的类依然可以编译，但是 E 也可以编译了；很明显，自限定的限制只服务于强制继承关系。
 * 如果你使用自限定，你会知道该类使用的类型参数和使用该参数的类是同一种**基类**。它强制任何使用该类的人都遵从这种形式。
 * If you use self-bounding, you know that [the type parameter used by the class] is
 * [the same **basic type**] as [the class that’s using that parameter].
 */
public class NotSelfBounded<T> {
    T element;

    NotSelfBounded<T> set(T arg) {
        element = arg;
        return this;
    }

    T get() {
        return element;
    }
}

class A2 extends NotSelfBounded<A2> {
}

class B2 extends NotSelfBounded<A2> {
}

class C2 extends NotSelfBounded<C2> {
    C2 setAndGet(C2 arg) {
        set(arg);
        return get();
    }
}

class D2 {
}

// Now this is OK:
class E2 extends NotSelfBounded<D2> {
}
