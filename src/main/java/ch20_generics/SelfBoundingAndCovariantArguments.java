package ch20_generics;

/**
 * @author runningpig66
 * @date 2026/1/12 周一
 * @time 22:00
 * P.719 §20.11 自限定类型 §20.11.3 参数协变性
 * <p>
 * 不过，使用自限定类型的时候，子类中只有一个方法，而该方法将派生类型作为自身参数，而不是基类类型：
 * <p>
 * 编译器无法识别出想要将基类类型作为参数传入 set() 的意图，因为并不存在匹配这种签名的方法。该参数实际上已经被重写了。
 */
interface SelfBoundSetter<T extends SelfBoundSetter<T>> {
    void set(T arg);
}

// 自限定与参数协变：证明了只有自限定泛型才能真正模拟出参数协变（父类方法的参数类型自动变成子类类型）的效果，
// 它利用泛型约束强行将父类方法的参数锁定为当前子类类型，从而完美堵死了父类的宽泛入口。
interface Setter extends SelfBoundSetter<Setter> {
}

public class SelfBoundingAndCovariantArguments {
    void testA(Setter s1, Setter s2, SelfBoundSetter sbs) {
        s1.set(s2);
        //- s1.set(sbs);
        // SelfBoundingAndCovariantArguments.java:25: error: method set in interface SelfBoundSetter<T> cannot be applied to given types;
        // s1.set(sbs);
        //     ^
        // required: Setter
        // found:    SelfBoundSetter
        // reason: argument mismatch; SelfBoundSetter cannot be converted to Setter
        // where T is a type-variable:
        // T extends SelfBoundSetter<T> declared in interface SelfBoundSetter
        // 1 error
    }
}
