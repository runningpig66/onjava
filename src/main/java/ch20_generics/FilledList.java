package ch20_generics;

import onjava.Suppliers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author runningpig66
 * @date 2025/12/28 周日
 * @time 17:38
 * P.673 §20.6 类型擦除的奥秘 §20.6.4 边界的行为
 * <p>
 * 编译器并未产生警告，尽管我们知道（由于类型擦除）在 create() 内部的 new ArrayList<T>() 方法中，<T> 被擦除了————
 * 在运行时，类中并没有<T>，因此它看起来似乎并没有什么实际用处。但是如果顺着这个思路将表达式修改为 new ArrayList()，编译器会产生警告。
 * <p>
 * 所以在这里它真的是毫无用处吗？如果你在创建 List 的时候将一些对象放入其中，又会怎样呢？如下所示：
 * <p>
 * 尽管在 add() 内部，编译器无法获知任何关于 T 的信息，但仍然可以在编译时确保向 FilledList 中放入的是类型 T。
 * 因此，即使类型擦除移除了方法或类中的实际类型信息，编译器仍然能够确保类型使用方式的内部一致性。
 * <p>
 * notes: 泛型边界行为：数组的运行时依赖与集合的编译期一致性分析.md
 */
public class FilledList<T> extends ArrayList<T> {
    FilledList(Supplier<T> gen, int size) {
        Suppliers.fill(this, gen, size);
    }

    public FilledList(T t, int size) {
        for (int i = 0; i < size; i++) {
            this.add(t);
        }
    }

    public static void main(String[] args) {
        List<String> list = new FilledList<>("Hello", 4);
        System.out.println(list);
        // Supplier version:
        List<Integer> ilist = new FilledList<>(() -> 47, 4);
        System.out.println(ilist);
    }
}
/* Output:
[Hello, Hello, Hello, Hello]
[47, 47, 47, 47]
 */
