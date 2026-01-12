package ch20_generics;

/**
 * @author runningpig66
 * @date 2026/1/12 周一
 * @time 21:31
 * P.718 §20.11 自限定类型 §20.11.3 参数协变性
 * <p>
 * 自限定类型的价值在于它可以生成协变参数类型（covariant argument type）————方法参数的类型会随着子类而变化。
 * 虽然自限定类型也可以生成和子类类型相同的返回类型，但这并不那么重要，因为 Java 5 中引入了协变返回类型（covariant return type）：
 * <p>
 * DerivedGetter 中的 get() 方法重写自 OrdinaryGetter 中的 get() 方法，并返回 OrdinaryGetter.get() 的返回类型的子类。
 * 虽然这在逻辑上很完美————子类方法可以返回比其所重写的基类方法更具体的类型————但这在更早的 Java 版本中是非法的。
 */
class Base {
}

class Derived extends Base {
}

interface OrdinaryGetter {
    Base get();
}

// 返回值协变：展示了 Java 5 以后原生就支持协变返回类型，即子类重写方法时，可以手动将返回值修改为更具体的子类，而无需借助自限定泛型。
interface DerivedGetter extends OrdinaryGetter {
    // Overridden method return type can vary:
    @Override
    Derived get();
}

public class CovariantReturnTypes {
    void test(DerivedGetter d) {
        Derived d2 = d.get();
    }
}
