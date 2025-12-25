package onjava;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

/**
 * @author runningpig66
 * @date 2025/12/25 周四
 * @time 21:30
 * P.655 §20.4 泛型方法 §20.4.2 通用 Supplier
 * Supplier from a class with a zero-argument constructor
 * <p>
 * 以下示例中的类可以为任何具有无参数构造器的类生成一个 Supplier，为了减少代码编写量，它还包含了一个用于生成 BasicSupplier 的泛型方法：
 * <p>
 * 该类提供了为符合以下条件的类生成对象的基本实现。
 * 1. 该类是 public 的。因为 BasicSupplier 在独立的包中，所以我们所讨论的类不能仅有包访问权限，还必须是 public 的。
 * 2. 该类具有无参构造器。要创建某个 BasicSupplier 对象，你需要调用 create() 方法，并传入你想要生成的类型的类型标记（token）。
 * 泛型的 create() 方法提供了更为方便的语法 BasicSupplier.create(MyType.class)，而不是麻烦的 new BasicSupplier<MyType>(MyType.class)。
 */
public class BasicSupplier<T> implements Supplier<T> {
    private Class<T> type;

    public BasicSupplier(Class<T> type) {
        this.type = type;
    }

    @Override
    public T get() {
        try {
            // Assumes type is a public class:
            return type.getConstructor().newInstance();
        } catch (InstantiationException |
                 IllegalAccessException |
                 InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    // Produce a default Supplier from a type token:
    public static <T> Supplier<T> create(Class<T> type) {
        return new BasicSupplier<>(type);
    }
}
