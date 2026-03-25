package ch21_arrays;

import onjava.ArrayShow;
import onjava.Count;

import java.util.Arrays;

/**
 * @author runningpig66
 * @date 2026-03-25
 * @time 19:47
 * P.791 §21.14 数组复制
 * Demonstrate Arrays.copyOf() and System.arraycopy()
 * <p>
 * copyOf() 和 copyOfRange() 方法复制数组的速度远远快于用 for 循环等手写代码的实现方式。
 * 这类方法对所有类型都有对应的重载版本。下面先来看看 int 和 Integer 类型的数组复制：
 */
class Sup { // Superclass
    private int id;

    Sup(int n) {
        id = n;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + id;
    }
}

class Sub extends Sup { // Subclass
    Sub(int n) {
        super(n);
    }
}

public class ArrayCopying {
    public static final int SZ = 15;

    public static void main(String[] args) {
        int[] a1 = new int[SZ];
        Arrays.setAll(a1, new Count.Integer()::get);
        ArrayShow.show("a1", a1);
        // [1] 这是最基本的复制数组的方法：只需指定要复制的结果大小（size）即可。这在改变数组大小时非常有用。
        // 复制完成后，我们把 a1 的所有元素都赋值为 1，以此来证明复制并不会影响 a2（的值）。
        // static int[] copyOf(int[] original, int newLength)
        int[] a2 = Arrays.copyOf(a1, a1.length);
        // Prove they are distinct arrays:
        Arrays.fill(a1, 1);
        ArrayShow.show("a1", a1);
        ArrayShow.show("a2", a2);

        // [2] 通过改变结果的 size（最后一个参数），我们可以改变结果数组的长度。
        // Create a shorter result:
        a2 = Arrays.copyOf(a2, a2.length / 2);
        ArrayShow.show("a2", a2);
        // Allocate more space:
        a2 = Arrays.copyOf(a2, a2.length + 5);
        ArrayShow.show("a2", a2);

        // [3] copyOf() 和 copyOfRange() 都适用于包装类。copyOfRange() 需要指定开始索引和结束索引。
        // Also copies wrapped arrays:
        Integer[] a3 = new Integer[SZ];
        Arrays.setAll(a3, new Count.Integer()::get);
        // static <T> T[] copyOfRange(T[] original, int from, int to)
        Integer[] a4 = Arrays.copyOfRange(a3, 4, 12);
        ArrayShow.show("a4", a4);

        Sub[] d = new Sub[SZ / 2];
        Arrays.setAll(d, Sub::new);
        // [4] copyOf() 和 copyOfRange() 都有创建不同类型数组的重载版本，在调用时给方法的最后一个参数传入目标类型即可。
        // 我一开始猜想这可能是用来在基本类型数组和包装类数组之间相互转化的，但后来发现并非如此。
        // 它们其实是用来做“向上转型”和“向下转型”的，也就是说，如果你想根据一个已有的子类型数组生成它的基类数组，这些方法能帮你达到目的。
        // Produce Sup[] from Sub[]:
        // static <T,U> T[] copyOf(U[] original, int newLength, Class<? extends T[]> newType)
        Sup[] b = Arrays.copyOf(d, d.length, Sup[].class);
        ArrayShow.show(b);

        // [5] 你甚至可以成功地“向下转型”，从父类数组生成子类数组。现有代码能正确运行，因为我们只做了“向上转型”。
        // This "downcast" works fine:
        Sub[] d2 = Arrays.copyOf(b, b.length, Sub[].class);
        ArrayShow.show(d2);

        // Bad "downcast" compiles but throws exception:
        Sup[] b2 = new Sup[SZ / 2];
        Arrays.setAll(b2, Sup::new);
        try {
            // [6] 该行“数组转型”是可以通过编译的，但如果类型不匹配，则会抛出运行时异常。
            // 在此处强行把基类当作其子类是非法的，因为子类对象中的一些数据和方法很可能在基类对象中并不存在。
            Sub[] d3 = Arrays.copyOf(b2, b2.length, Sub[].class);
        } catch (Exception e) {
            System.out.println(e);
        }

        // 本示例说明了基本类型数组和对象数组都可以进行复制。然而，如果要复制对象数组，则只有引用会被复制，这并不存在对象自身的副本。这被称为浅拷贝。
        // 另外还有个方法 System.arraycopy()，也可以将一个数组复制到另一个已分配好的数组中。此处并不会发生自动装箱或自动拆箱，这是因为相关的两个数组的类型必须完全一致。
        /*
        原书关于“不会发生自动装箱或拆箱，类型必须完全一致”的描述，实际上是特指基本类型数组与包装类数组之间的拷贝场景。
        System.arraycopy() 作为底层极其追求性能的 native 方法，执行的是内存块的极速浅拷贝。在物理内存布局上，
        基本类型数组（如 int[]）连续存储的是纯粹的二进制数值，而包装类数组（如 Integer[]）存储的则是指向堆内存中各个对象的引用地址。
        如果允许两者互相拷贝，底层 C++ 就被迫需要暂停拷贝，去堆内存中逐个为数值动态创建新对象（装箱）或解引用获取真实值（拆箱），这不仅耗时，也违背了该方法作为极速内存拷贝工具的设计初衷。
        因此，当基本类型参与数组复制时，虚拟机强制要求源数组与目标数组的物理类型必须严格一模一样，底层绝不会自动进行任何装拆箱转换。

        对于对象引用数组的拷贝，规则则完全不同，这里允许数组的声明类型不一致，其核心在于面向对象的多态机制与 JVM 运行时的安全拦截。
        对象数组之间的拷贝纯粹是引用的复制，但在执行过程中，JVM 会严格校验类型安全。它不会死板地只看数组的声明类型，
        而是在底层逐个检查源数组中每个真实对象的对象头（Object Header）。只要源数组中实际存放的对象类型能够合法地赋值给目标数组的元素类型
        （例如将真实的子类对象放入父类数组实现向上转型，或将实际就是子类实例的对象放回子类数组实现安全的向下转型），底层的引用拷贝就会顺利放行。
        反之，如果试图将真实的父类实例强行塞入子类类型的数组，底层代码会立即阻断这一违背多态原则的操作，并抛出 ArrayStoreException 运行时异常。
         */
        int[] a = new int[SZ];
        Integer[] aB = new Integer[SZ];
        try {
            // static native void arraycopy(Object src, int srcPos, Object dest, int destPos, int length);
            System.arraycopy(a, 0, aB, 0, SZ);
        } catch (Exception e) {
            // Error: java.lang.ArrayStoreException: arraycopy: type mismatch: can not copy int[] into object array[]
            System.out.println(e);
        }
    }
}
/* Output:
a1: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14]
a1: [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]
a2: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14]
a2: [0, 1, 2, 3, 4, 5, 6]
a2: [0, 1, 2, 3, 4, 5, 6, 0, 0, 0, 0, 0]
a4: [4, 5, 6, 7, 8, 9, 10, 11]
[Sub0, Sub1, Sub2, Sub3, Sub4, Sub5, Sub6]
[Sub0, Sub1, Sub2, Sub3, Sub4, Sub5, Sub6]
java.lang.ArrayStoreException: arraycopy: element type mismatch: can not cast one of the elements of ch21_arrays.Sup[] to the type of the destination array, ch21_arrays.Sub
java.lang.ArrayStoreException: arraycopy: type mismatch: can not copy int[] into object array[]
 */
