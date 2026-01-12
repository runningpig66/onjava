package ch20_generics;

/**
 * @author runningpig66
 * @date 2026/1/12 周一
 * @time 22:14
 * P.720 §20.11 自限定类型 §20.11.3 参数协变性
 * <p>
 * 如果没有使用自限定，普通的继承机制就会介入，并且会进行重载，就和非泛型的情况一样：
 * <p>
 * 这段代码模仿了 OrdinaryArguments.java，在该例中，DerivedSetter 继承自含有 set(Base) 方法的 OrdinarySetter。
 * 而此处，DerivedGS 继承自同样带有 set(Base) 方法的 GenericSetter<Base>，该方法是由泛型创建的。
 * 而且就像 OrdinaryArguments.java 一样，从输出可以看出 DerivedGS 含有 set() 的 2 个重载版本。
 * 如果没有使用自限定，你就需要对参数类型进行重载。如果使用自限定，你最后只会有一个接收确切类型参数的方法版本。
 */
class GenericSetter<T> { // Not self-bounded
    void set(T arg) {
        System.out.println("GenericSetter.set(Base)");
    }
}

// 普通泛型继承 = 重载（Overload）：作为反面教材警告我们，如果只是使用普通泛型（而非自限定）并将类型固定为基类，
// 依然无法解决参数协变问题，结果会退化为普通的重载，和不用泛型没区别。
class DerivedGS extends GenericSetter<Base> {
    void set(Derived derived) {
        System.out.println("DerivedGS.set(Derived)");
    }
}

public class PlainGenericInheritance {
    public static void main(String[] args) {
        Base base = new Base();
        Derived derived = new Derived();
        DerivedGS dgs = new DerivedGS();
        dgs.set(derived);
        dgs.set(base); // Overloaded, not overridden!
    }
}
/* Output:
DerivedGS.set(Derived)
GenericSetter.set(Base)
 */
