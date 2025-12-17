package ch19_reflection;

import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/12/17 周三
 * @time 20:23
 * P.601 §19.2 Class 对象 §19.2.2 泛型类的引用
 * <p>
 * DynamicSupplier 会强制要求它使用的任何类型都有一个 public 的无参构造器（一个没有参数的构造器）。如果不符合条件，就会抛出一个异常。
 * 在上面的例子中，ID 自动生成的无参构造器不是 public 的，因为 ID 类不是 public 的。所以我们必须显式定义它。
 * 而在下面的例子中，ID2 类是 public 的，因此自动生成的无参构造器也是 public 的，这时我们不需要显式的定义它。
 */
public class ID2 {
    private static long counter;
    private final long id = counter++;

    @Override
    public String toString() {
        return Long.toString(id);
    }

    public static void main(String[] args) {
        Stream.generate(new DynamicSupplier<>(ID2.class))
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
