package ch20_generics;

import java.util.Arrays;

/**
 * @author runningpig66
 * @date 2026/1/1 周四
 * @time 1:44
 * P.690 §20.9 通配符
 * <p>
 * 在第 12 章中，你已经见过了通配符（即泛型参数表达式中的问号）的一些简单用法。在第 19 章中这样的例子则更多。本节将对该特性进行更深入的探讨。
 * 我们会从一个示例开始，该示例展示了数组的一种特殊行为————可以将派生类型的数组赋值给基类数组的引用：
 * <p>
 * main() 方法的第一行创建了一个 Apple 数组，并将其赋值给一个指向 Fruit 数组的引用。
 * 这可以说得通————Apple 是一种 Fruit（苹果是一种水果），所以 Apple 数组也应该是一个 Fruit 数组。
 * <p>
 * 不过，如果实际的数组类型是 Apple[]，你就可以将 Apple 或 Apple 的子类放入该数组，这实际上在编译时和运行时都是没问题的。
 * 但是你也可以将 Fruit 对象放入该数组。这对于编译器来说是说得通的，因为它持有 Fruit[] 的引用————编译器有什么理由不允许将 Fruit 对象，
 * 或任何 Fruit 派生出来的对象，例如 Orange（橙子），放入该数组呢？因此在编译时，这是允许的。
 * 但是运行时的数组机制知道自己是在处理 Apple[]，并会在向该数组中放入异构类型时抛出异常。
 * <p>
 * notes: 10-类型兼容性：数组协变 vs 泛型不变 vs 通配符.md
 */
class Fruit {
}

class Apple extends Fruit {
}

class Jonathan extends Apple {
}

class Orange extends Fruit {
}

public class CovariantArrays {
    public static void main(String[] args) {
        // 对于“读”，协变是合理的。对于“写”，协变是危险的。
        Fruit[] fruit = new Apple[10];
        fruit[0] = new Apple(); // OK
        fruit[1] = new Jonathan(); // OK
        // Runtime type is Apple[], not Fruit[] or Orange[]:
        try {
            // Warning: Storing element of type 'Fruit' to array of 'Apple' elements will produce 'ArrayStoreException'
            // Compiler allows you to add Fruit:
            fruit[0] = new Fruit(); // ArrayStoreException
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            // Warning: Storing element of type 'Orange' to array of 'Apple' elements will produce 'ArrayStoreException'
            // Compiler allows you to add Oranges:
            fruit[0] = new Orange(); // ArrayStoreException
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(Arrays.toString(fruit));
    }
}
/* Output:
java.lang.ArrayStoreException: ch20_generics.Fruit
java.lang.ArrayStoreException: ch20_generics.Orange
[ch20_generics.Apple@8efb846, ch20_generics.Jonathan@2a84aee7, null, null, null, null, null, null, null, null]
 */
