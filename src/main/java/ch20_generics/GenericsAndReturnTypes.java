package ch20_generics;

/**
 * @author runningpig66
 * @date 2026/1/12 周一
 * @time 21:38
 * P.718 §20.11 自限定类型 §20.11.3 参数协变性
 * <p>
 * 自限定泛型实际上会生成精确的派生类型作为返回值，如下面的 get() 中所示：
 * <p>
 * 注意，这段代码只能在引人了协变返回类型的 Java5 之后的版本中才能译。
 */
interface GenericGetter<T extends GenericGetter<T>> {
    T get();
}

// 泛型与返回值：演示了自限定泛型可以“自动化”这个返回值变窄的过程，你只需在类定义时指定一次，所有方法的返回值就会自动精确适配到子类，省去了手动重写的麻烦。
interface Getter extends GenericGetter<Getter> {
}

public class GenericsAndReturnTypes {
    void test(Getter g) {
        Getter result = g.get();
        // Warning: Raw use of parameterized class 'GenericGetter'
        GenericGetter gg = g.get(); // Also the base type
    }
}
