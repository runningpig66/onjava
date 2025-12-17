package ch19_reflection;

import java.util.Random;

/**
 * @author runningpig66
 * @date 2025/12/17 周三
 * @time 15:41
 * P.597 §19.2 Class 对象 §19.2.1 类字面量
 * <p>
 * Java 还提供了另一种方式来生成 Class 对象的引用：类字面量（class literal）。对前面的程序而言，它看起来像这样：Initable.class;
 * 这更简单也更安全，因为它会进行编译时检查（因此不必放在 try 块中）。另外它还消除了对 forName() 方法的调用，所以效率也更高。
 * 类字面量适用于常规类以及接口、数组和基本类型。此外，每个基本包装类都有一个名为 TYPE 的标准字段。
 * TYPE 字段表示一个指向和基本类型对应的 Class 对象的引用，如类字面量 boolean.class 等价于 Boolean.TYPE.
 * <p>
 * 请注意，使用 ".class" 的形式创建 Class 对象的引用时，该 Class 对象不会自动初始化。
 * 初始化被延迟到首次引用静态方法（构造器是隐式静态的）或非常量静态字段时：
 */
class Initable {
    // static 变量初始化 和 static 块 是严格按照源码中从上到下的顺序执行的。
    static final int STATIC_FINAL = 47;
    static final int STATIC_FINAL2 = ClassInitialization.rand.nextInt(1000);

    static {
        System.out.println("Initializing Initable");
    }
}

class Initable2 {
    static int staticNonFinal = 147;

    static {
        System.out.println("Initializing Initable2");
    }
}

class Initable3 {
    static int staticNonFinal = 74;

    static {
        System.out.println("Initializing Initable3");
    }
}

public class ClassInitialization {
    public static Random rand = new Random(47);

    public static void main(String[] args) throws Exception {
        // 从 initable 引用的创建过程中可以看出，仅使用 .class 语法来获取对类的引用不会导致初始化。
        Class initable = Initable.class;
        System.out.println("After creating Initable ref");
        // Does not tigger initialization:
        // 如果一个 static final 字段的值是“编译时常量”，比如 Initable.STATIC_FINAL，那么这个值不需要初始化 Initable 类就能读取。
        System.out.println(Initable.STATIC_FINAL);
        // Triggers initialization:
        // 但是把一个字段设置为 static 和 final 并不能保证这种行为：对 Initable.STATIC_FINAL2 的访问会强制执行类的初始化，因为它不是编译时常量。
        // 访问运行时计算的 static final 字段（非常量）会触发类初始化，导致先执行完所有 static 初始化（包括块），才能拿到字段的值。
        System.out.println(Initable.STATIC_FINAL2);
        // Triggers initialization:
        // 如果 static 字段不是 final 的，那么访问它时，如果想要正常读取，总是需要先进行链接（为字段分配存储）
        // 和初始化（初始化该存储），正如在对 Initable2.staticNonFinal 的访问中看到的那样。
        System.out.println(Initable2.staticNonFinal);
        // 而 Class.forName() 会立即初始化类以产生 Class 引用，如 initable3 的创建所示。
        Class initable3 = Class.forName("ch19_reflection.Initable3");
        System.out.println("After creating Initable3 ref");
        System.out.println(Initable3.staticNonFinal);
    }
}
/* Output:
After creating Initable ref
47
Initializing Initable
258
Initializing Initable2
147
Initializing Initable3
After creating Initable3 ref
74
 */
