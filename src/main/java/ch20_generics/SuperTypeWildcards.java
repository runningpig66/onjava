package ch20_generics;

import java.util.List;

/**
 * @author runningpig66
 * @date 2026/1/1 周四
 * @time 20:17
 * P.694 §20.9 通配符 §20.9.2 逆变性
 * <p>
 * 还有另一种可能的方式，即利用超类通配符（supertype wildcard）。这里，你可以认为是为通配符增加了边界限制，
 * 边界范围是某个类的任何基类，具体方式为 <? super MyClass> 或者甚至是使用类型参数 <? super T>
 * （不过你无法给泛型参数设置超类边界，也就是说，无法这样声明：<T super MyClass>）。
 * 这样可以安全地将类型对象传入泛型类型中。因此，有了超类通配符后，你就可以向 Collection 中进行写操作了。
 * <p>
 * 参数 apples 是由某种 Apple 的基类组成的 List，因此你知道可以安全地向其中添加 Apple 类型或其子类型。
 * 不过下界（lower bound）是 Apple，所以你并不知道是否可以安全地向这样一个 List 中添加 Fruit，
 * 因为这会使得 List 对其他非 Apple 的类型也敞开怀抱，而这违反了静态类型的安全性。
 * <p>
 * notes: 11-泛型机制：PECS 原则、协变与逆变.md
 */
public class SuperTypeWildcards {
    static void writeTo(List<? super Apple> apples) {
        apples.add(new Apple());
        apples.add(new Jonathan());
        // apples.add(new Fruit()); // Error
    }
}
