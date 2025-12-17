package ch19_reflection;

import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/12/17 周三
 * @time 18:18
 * P.600 §19.2 Class 对象 §19.2.2 泛型类的引用
 * <p>
 * 下面是一个使用了泛型类语法的示例。它存储了一个类引用，然后使用 newInstance() 来生成对象：
 * <p>
 * DynamicSupplier 会强制要求它使用的任何类型都有一个 public 的无参构造器（一个没有参数的构造器）。如果不符合条件，就会抛出一个异常。
 * 在下面的例子中，ID 自动生成的无参构造器不是 public 的，因为 ID 类不是 public 的。所以我们必须显式定义它。
 * 而在下一个例子中，ID2 类是 public 的，因此自动生成的无参构造器也是 public 的，这时我们不需要显式的定义它。
 */
class ID {
    private static long counter;
    private final long id = counter++;

    @Override
    public String toString() {
        return Long.toString(id);
    }

    // A public default constructor is required to call getConstructor().newInstance():
    // 如果 ID 类不是 public 的，或者你想明确控制构造器可见性，就需要显式写一个 public 无参构造器。
    // 但如果 ID 是 public 类，且没有其他构造器，编译器会自动生成 public 无参构造器，反射也能用，不需要显式写。
    public ID() {
    }
}

public class DynamicSupplier<T> implements Supplier<T> {
    private Class<T> type;

    public DynamicSupplier(Class<T> type) {
        this.type = type;
    }

    @Override
    public T get() {
        try {
            // 当你通过反射来创建对象时，它会去严格查找一个 public 的无参构造器。
            return type.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Stream.generate(new DynamicSupplier<>(ID.class))
                .skip(10)
                .limit(5)
                .forEach(System.out::println);
    }
}
/* Output:
10
11
12
13
14
 */
