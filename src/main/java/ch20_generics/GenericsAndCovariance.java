package ch20_generics;

import java.util.ArrayList;
import java.util.List;

/**
 * @author runningpig66
 * @date 2026/1/1 周四
 * @time 2:29
 * P.691 §20.9 通配符
 * <p>
 * 不过，有时你会想要在这两者间建立某种向上转型的关系：通配符便可以达到这个目的。
 * <p>
 * 该类型必须持有某个具体的 Fruit 或其子类，但是如果你实际上并不关心它是什么类型，你能用这样一个 List 来做什么呢？
 * 如果你不知道 List 持有的是什么类型，你又如何能安全地向其中添加对象呢？正如同 CovariantArrays.java 中的“向上转型”数组一样，
 * 你不能这样做，除非编译器能阻止类型不匹配的操作发生，而不是等到运行时系统（再来阻止）。你稍后就会发现该问题。
 * <p>
 * 你可能会辩驳说，这有点过头了，因为现在你甚至无法向刚提到的、可以持有 Apple 的 List 中添加一个 Apple。
 * 是的，但是编译器并不知道这件事。List<? extends Fruit> 可以合法地指向 List<Orange>。
 * 一旦你进行了这种“向上转型”，便失去了向其中传入任何对象的能力，即使是传入 Object 也是如此。
 * <p>
 * 另一方面，如果你调用的是返回 Fruit 的方法，则是安全的，因为你知道 List 中的任何元素都必须至少是 Fruit 类型，所以编译器允许这么做。
 * <p>
 * notes: 10-类型兼容性：数组协变 vs 泛型不变 vs 通配符.md
 */
public class GenericsAndCovariance {
    public static void main(String[] args) {
        // Wildcards allow covariance:
        List<? extends Fruit> flist = new ArrayList<>();
        // Compile Error: can't add any type of object:
        // flist.add(new Apple());
        // flist.add(new Fruit());
        // flist.add(new Object());
        flist.add(null); // Legal but uninteresting
        // We know it returns at least Fruit:
        Fruit f = flist.get(0);
    }
}
