package ch20_generics;

/**
 * @author runningpig66
 * @date 2026/1/12 周一
 * @time 15:46
 * P.714 §20.11 自限定类型 §20.11.1 奇异递归泛型
 * <p>
 * 要理解这其中的意义，你可以试着大声说：“我要创建一个新类，它继承自将该新类类名作为自身参数的泛型类型。”
 * 泛型基类在拿到子类类名后，能做些什么呢？嗯，Java 泛型的重点在于参数和返回类型，因此可以生成将派生类型作为参数和返回值的基类。
 * 也可以将派生类型作为字段的类型，尽管它们会被擦除为 Object。下面这个泛型类诠释了该模式：
 * <p>
 * 这是一个很普通的泛型类型，它内部包含两个方法，分别用于接收和生成与参数类型一致的对象，
 * 以及一个用于操作存储的字段的方法（虽然只是对该字段执行了 Object 的操作）。
 */
public class BasicHolder<T> {
    T element;

    void set(T arg) {
        element = arg;
    }

    T get() {
        return element;
    }

    void f() {
        System.out.println(element.getClass().getSimpleName());
    }
}
