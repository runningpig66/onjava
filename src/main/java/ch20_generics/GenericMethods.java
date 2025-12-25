package ch20_generics;

/**
 * @author runningpig66
 * @date 2025/12/25 周四
 * @time 20:32
 * P.654 §20.4 泛型方法
 * <p>
 * 到目前为止，我们已经探讨了对整个类的参数化。还可以对类内部的方法进行参数化。类自身可以是泛型的，也可以不是————这和是否存在参数化的方法无关。
 * 泛型方法改变着方法的行为，而不受类的影响。可以使用这个准则：“尽早”使用泛型方法。相比于泛型化整个类，泛型化单个方法通常来说会更清晰。
 * <p>
 * 如果某个方法是静态的，它便没有访问类的泛型类型参数的权限，因此如果要用到泛型能力，它就必须是泛型方法。
 * <p>
 * 虽然类和类中的方法都可以同时被参数化，但该 GenericMethods 类并未被参数化。在这里，只有 f() 方法具有类型参数。
 * 使用泛型类时，在实例化类的时候必须指定类型参数。而使用泛型方法的时候，通常不需要指定参数类型，因为编译器会为你检测出来。
 * 这称为类型参数推断（type argument inference）。因此调用 f() 方法看起来就和调用普通方法一样，
 * 而且 f() 看起来就像不断地被重载。它甚至可以接收 GenericMethods 类型的参数。
 */
public class GenericMethods {
    // 要定义一个泛型方法，需要将泛型参数列表放在返回值之前，就像这样：
    public <T> void f(T x) {
        System.out.println(x.getClass().getName());
    }

    public static void main(String[] args) {
        GenericMethods gm = new GenericMethods();
        gm.f("");
        // 在用基本类型调用 f() 方法时，自动装箱机制便会生效，将基本类型自动包装为相应的对象。
        gm.f(1);
        gm.f(1.0);
        gm.f(1.0F);
        gm.f('c');
        gm.f(gm);
    }
}
/* Output:
java.lang.String
java.lang.Integer
java.lang.Double
java.lang.Float
java.lang.Character
ch20_generics.GenericMethods
 */
