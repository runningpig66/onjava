package ch20_generics;

/**
 * @author runningpig66
 * @date 2026/1/12 周一
 * @time 21:47
 * P.719 §20.11 自限定类型 §20.11.3 参数协变性
 * <p>
 * 不过，在非泛型的代码中，参数的类型无法随子类型变化：
 * <p>
 * set(derived) 和 set(base) 都是非法的。因此 DerivedSetter.set() 并未重写 OrdinarySetter.set() 方法，而是重载该方法。
 * 从输出可以看出，DerivedSetter 中有两个方法，所以基类中的版本仍然是可用的，从而可以检测出它是被重载了。
 * 注意，如果使用 @Override，则会通过错误消息指出问题所在。
 */
class OrdinarySetter {
    void set(Base base) {
        System.out.println("OrdinarySetter.set(Base)");
    }
}

// 普通参数：揭示了在普通继承中参数是绝对不支持协变的，子类若试图将参数改为子类型，会被视为重载（Overload），导致父类那个宽泛的入口依然敞开，存在安全隐患。
class DerivedSetter extends OrdinarySetter {
    void set(Derived derived) {
        System.out.println("DerivedSetter.set(Derived)");
    }
}

public class OrdinaryArguments {
    public static void main(String[] args) {
        Base base = new Base();
        Derived derived = new Derived();
        DerivedSetter ds = new DerivedSetter();
        ds.set(derived);
        // Compiles--overloaded, not overridden!:
        ds.set(base);
    }
}
/* Output:
DerivedSetter.set(Derived)
OrdinarySetter.set(Base)
 */
