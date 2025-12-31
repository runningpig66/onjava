package ch20_generics;

/**
 * @author runningpig66
 * @date 2025/12/30 周二
 * @time 1:02
 * P.681 §20.7 对类型擦除的补偿 §20.7.2 泛型数组
 * <p>
 * 编译器接受了这种方式，没有产生警告。但是你永远无法创建该确切类型（包括类型参数）的数组，因此这有点让人疑惑。
 * 所有的数组不论持有的是什么类型，都有相同的结构（包括每个数组的大小和布局），因此你似乎可以创建一个 Object 数组，
 * 并将其转换为目标数组类型。这确实可以通过编译，但会在运行时抛出 ClassCastException 异常：
 * <p>
 * 问题在于数组时刻都掌握着它们的实际类型信息，而该类型是在创建数组的时刻确定的。因此尽管 gia 被转型为 Generic<Integer>[]，
 * 该信息也只会存在于编译时（并且如果未加上 @SuppressWarnings 注解，该转型还会产生警告）。在运行时，它仍然还是 Object 数组，
 * 而这会导致问题。唯一可以成功创建泛型类型数组的方法就是创建一个类型为被擦除类型的新数组，然后再对其进行类型转换。
 * <p>
 * notes: 09-泛型数组创建限制的原因分析.md
 */
/*
// 【关键注释：打破完美的幻象】
// 虽然 gia 引用提供了编译期的类型安全检查（边界守卫），但这是一个"残疾"的数组。
// 物理本质：底层对象依然是原生类型 Generic[]，彻底丢失了 <Integer> 类型参数的信息。
// 致命缺陷：它失去了数组核心的"运行时自我防卫能力"（Reification）。
// 如果将其向上转型为 Object[]，JVM 将无法拦截 Generic<String> 的写入（因为运行时大家都是 Generic），
// 从而导致堆污染悄无声息地发生，而真正的泛型数组本应在此处抛出 ArrayStoreException。

// A. 真正的完美数组（String[]）
String[] strArr = new String[10];
Object[] objArr = strArr; // 向上转型，合法
// 【关键点】：这里会炸！
// 即使引用是 Object[]，数组本身（物理对象）知道自己是 String[]。
// 它会立刻抛出 ArrayStoreException，拒绝脏数据进入。
objArr[0] = new Integer(100);

// B. 你的“伪”泛型数组（Generic[]）
Generic<Integer>[] gia = new Generic[10]; // 实际物理类型：Generic[]
Object[] objArr = gia; // 向上转型，合法
// 【关键点】：这里不会炸！沉默地发生了堆污染！
// 插入一个 Generic<String>（泛型不对，但原生类型对）。
// 因为底层数组是 Generic[]，它分不清 Generic<Integer> 和 Generic<String> 的区别。
// 真正的完美数组应该在这里报错，但它没有。
objArr[0] = new Generic<String>();
// 灾难推迟到了取出的时刻
Generic<Integer> item = gia[0]; // 看起来没问题...
Integer i = item.get(); // 💥 此时才报 ClassCastException，或者更晚。
*/
public class ArrayOfGeneric {
    static final int SIZE = 100;
    static Generic<Integer>[] gia;

    @SuppressWarnings({"unchecked", "DataFlowIssue"})
    public static void main(String[] args) {
        try {
            // Warning: Unchecked cast: 'java.lang.Object[]' to 'ch20_generics.Generic<java.lang.Integer>[]'
            gia = (Generic<Integer>[]) new Object[SIZE];
        } catch (ClassCastException e) {
            System.out.println(e.getMessage());
        }
        // Runtime type is the raw (erased) type:
        gia = (Generic<Integer>[]) new Generic[SIZE];
        System.out.println(gia.getClass().getSimpleName());
        gia[0] = new Generic<>();
        //- gia[1] = new Object(); // Compile-time error
        // Discovers type mismatch at compile time:
        //- gia[2] = new Generic<Double>();
    }
}
/* Output:
Note: Some input files use unchecked or unsafe operations.
Note: Recompile with -Xlint:unchecked for details.
class [Ljava.lang.Object; cannot be cast to class [Lch20_generics.Generic; ([Ljava.lang.Object; is in module java.base of loader 'bootstrap'; [Lch20_generics.Generic; is in unnamed module of loader 'app')
Generic[]
 */
