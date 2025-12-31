package ch20_generics;

import java.util.function.Supplier;

/**
 * @author runningpig66
 * @date 2025/12/29 周一
 * @time 22:07
 * P.677 §20.7 对类型擦除的补偿 §20.7.1 创建类型实例
 * <p>
 * 试图在 Erased.java 中创建 new T() 是不会成功的，部分原因是类型擦除，另一部分原因是编译器无法验证 T 中是否存在无参构造器。
 * 但是在 C++ 中，这种操作相当自然，并且简单安全（因为会在编译时检查）。
 * <p>
 * Java 的解决方案是传入一个工厂对象，并通过它来创建新实例。
 * Class 对象就是一个方便的工厂对象，因此如果你使用了类型标签，便可以通过 newInstance() 来创建该类型的新对象：
 * <p>
 * 这样可以编译成功，但如果使用 ClassAsFactory<Integer> 则会失败，因为 Integer 中并没有无参构造器。
 * 由于该错误并不是在编译期捕获的，因此这种方法遭到 Java 设计者们的反对。
 * 他们建议使用显式工厂（Supplier），并对类型进行限制，使其仅能接收实现了该工厂的类。
 */
class ClassAsFactory<T> implements Supplier<T> {
    Class<T> kind;

    ClassAsFactory(Class<T> kind) {
        this.kind = kind;
    }

    @Override
    public T get() {
        try {
            return kind.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

class Employee {
    public Employee() {
    }

    @Override
    public String toString() {
        return "Employee";
    }
}

public class InstantiateGenericType {
    public static void main(String[] args) {
        ClassAsFactory<Employee> fe = new ClassAsFactory<>(Employee.class);
        System.out.println(fe.get());
        ClassAsFactory<Integer> fi = new ClassAsFactory<>(Integer.class);
        try {
            System.out.println(fi.get());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
/* Output:
Employee
java.lang.NoSuchMethodException: java.lang.Integer.<init>()
 */
