package ch19_reflection;

/**
 * @author runningpig66
 * @date 2025/12/21 周日
 * @time 19:25
 * P.616 §19.5 Instanceof 与 Class 的等价性
 * The difference between instanceof and class
 * {java ch19_reflection.FamilyVsExactType}
 * <p>
 * 当查询类型信息时，instanceof 和 isInstance() 的效果是一样的，而它们与 Class 对象的直接比较有着重要的区别。
 * 下面这个示例演示了它们的不同之处：
 * <p>
 * test() 方法使用两种形式的 instanceof 来对其参数进行类型检查。然后获取 Class 引用，并使用 == 和 equals() 来测试 Class 对象的相等性。
 * 令人欣慰的是，instanceof 和 isInstance() 产生了完全相同的结果，而 equals() 和 == 也一样。但从两组测试本身，我们可以得出不同的结论。
 * instanceof 与类型的概念保持了一致，它相当于表示“你是这个类，还是这个类的子类”。
 * 另一方面，如果你使用 == 比较实际的 Class 对象，则不需要考虑继承————它要么是确切的类型，要么不是。
 */
class Base {
}

class Derived extends Base {
}

public class FamilyVsExactType {
    static void test(Object x) {
        System.out.println("Testing x of type " + x.getClass());
        System.out.println("x instanceof Base " + (x instanceof Base));
        System.out.println("x instanceof Derived " + (x instanceof Derived));
        System.out.println("Base.isInstance(x) " + Base.class.isInstance(x));
        System.out.println("Derived.isInstance(x) " + Derived.class.isInstance(x));
        System.out.println("x.getClass() == Base.class " + (x.getClass() == Base.class));
        System.out.println("x.getClass() == Derived.class " + (x.getClass() == Derived.class));
        System.out.println("x.getClass().equals(Base.class) " + (x.getClass().equals(Base.class)));
        System.out.println("x.getClass().equals(Derived.class) " + (x.getClass().equals(Derived.class)));
    }

    public static void main(String[] args) {
        test(new Base());
        test(new Derived());
    }
}
/* Output:
Testing x of type class ch19_reflection.Base
x instanceof Base true
x instanceof Derived false
Base.isInstance(x) true
Derived.isInstance(x) false
x.getClass() == Base.class true
x.getClass() == Derived.class false
x.getClass().equals(Base.class) true
x.getClass().equals(Derived.class) false
Testing x of type class ch19_reflection.Derived
x instanceof Base true
x instanceof Derived true
Base.isInstance(x) true
Derived.isInstance(x) true
x.getClass() == Base.class false
x.getClass() == Derived.class true
x.getClass().equals(Base.class) false
x.getClass().equals(Derived.class) true
 */
