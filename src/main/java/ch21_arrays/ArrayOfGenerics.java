package ch21_arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author runningpig66
 * @date 3月17日 周二
 * @time 3:51
 * P.762 §21.5 数组和泛型
 * <p>
 * 其实要说绝对无法创建一个泛型类型的数组，也不完全正确。是的，编译器不会允许你实例化一个泛型数组，但它允许你创建对此类数组的引用。
 * 例如：List<String>[] ls; 这样写是可以通过编译的，并且尽管你无法创建一个能实际持有泛型的数组对象，但还是可以创建非泛型的数组，然后强制类型转换：
 */
public class ArrayOfGenerics {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        List<String>[] ls;
        List[] la = new List[10];
        ls = (List<String>[]) la; // Unchecked cast
        ls[0] = new ArrayList<>();

        //- ls[1] = new ArrayList<Integer>();
        // error: incompatible types: ArrayList<Integer> cannot be converted to List<String>
        //         ls[1] = new ArrayList<Integer>();
        //                 ^

        // 你可以看到，一旦持有了对 List<String>[] 的引用，就会得到编译时检查。
        // 问题在于，数组是协变的，所以一个 List<String>[] 同时也是一个 Object[]，
        // 你可以利用这一点，将 ArrayList<Integer> 分配到数组里，而且无论编译时还是运行时都不会报错：
        // The problem: List<String> is a subtype of Object
        Object[] objects = ls; // So assignment is OK
        // Compiles and runs without complaint:
        objects[1] = new ArrayList<Integer>(Arrays.asList(1, 2, 3));

        /* 演示 Java 数组的协变性（Covariance）、泛型的不变性（Invariance）与类型擦除（Type Erasure）机制之间的底层冲突。
         * 此代码片段展示了上述冲突如何导致堆污染（Heap Pollution）现象。核心问题在于：
         * 非法的赋值操作在编译期和运行时均不会引发报错，而是将类型安全问题隐蔽地延迟到了数据提取的边界处。
         * 1. 编译期放行（基于数组协变）：ls（声明为 List<String>[]）被赋值给 objects（声明为 Object[]）。
         * 由于数组具备协变性，将 ArrayList<Integer> 赋值给 Object[] 的元素在编译器看来是合法的多态赋值，因此编译通过。
         * 2. 运行期放行（基于类型擦除）：在 JVM 运行期间，泛型参数被擦除。底层数组的实际类型为原生 List[]，
         * 存入的对象实际类型为原生 ArrayList。因为 ArrayList 是 List 的子类，
         * 该赋值操作符合 JVM 的数组运行时类型检查规则，不会抛出 ArrayStoreException。
         * 3. 延迟触发的类型转换异常（ClassCastException）：
         * 经过上述操作，声明为 List<String> 的 ls[1] 内部实际存储了 Integer 数据。
         * 当后续代码通过泛型引用读取数据（如调用 ls[1].get(0)）时，编译器会自动插入向下转型 (String) 的字节码指令。
         * 此时，由于实际取出的元素是 Integer，将在运行时触发 ClassCastException。
         */
        // String s = ls[1].get(0); // error: ClassCastException: class Integer cannot be cast to class String

        // 不过，如果你确定不会向上转型，需求也相对简单，那么你或许可以创建一个泛型数组，它也会提供基本的编译时类型检查。
        // 然而通常来说，泛型集合是比泛型数组更好的选择。
        // However, if your needs are straightforward it is possible to create an array of generics,
        // albeit with an "unchecked cast" warning:
        List<BerylliumSphere>[] spheres = (List<BerylliumSphere>[]) new List[10];
        Arrays.setAll(spheres, n -> new ArrayList<>());
    }
}
