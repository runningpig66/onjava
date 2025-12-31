package ch20_generics;

import onjava.Suppliers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author runningpig66
 * @date 2025/12/29 周一
 * @time 22:49
 * P.678 §20.7 对类型擦除的补偿 §20.7.1 创建类型实例
 * <p>
 * 这样可以编译成功，但如果使用 ClassAsFactory<Integer> 则会失败，因为 Integer 中并没有无参构造器。
 * 由于该错误并不是在编译期捕获的，因此这种方法遭到 Java 设计者们的反对。
 * 他们建议使用显式工厂（Supplier），并对类型进行限制，使其仅能接收实现了该工厂的类。下面是创建工厂的两种不同方法：
 * <p>
 * IntegerFactory 自身是个实现了 Supplier<Integer> 接口的工厂。Widget 包含了一个作为工厂的内部类。
 * 注意 Fudge 并不执行任何类似工厂的操作，但是传入 Fudge::new 仍然会产生工厂的行为，
 * 因为编译器将对函数方法 ::new 的调用，转变成了对 get() 的调用。
 * <p>
 * 笔记：【显式工厂模式（Supplier） vs 反射工厂（Class）】
 * 对比前例（InstantiateGenericType），本例展示了 Java 泛型中创建对象的最佳实践。
 * 1. 局限性突破：反射方式（`Class.newInstance`）强制依赖公共无参构造器，且错误延迟至运行时爆发（如 `Integer` 案例）。
 * 2. 编译期安全：使用 `Supplier<T>` 将创建逻辑的控制权交还给开发者，类型不匹配会在编译期直接报错。
 * 3. 灵活性与适配：`Supplier` 允许调用任意参数的构造器（如 `Widget`）或包含复杂初始化逻辑。
 * 同时，结合 Java 8 特性，既可使用普通类/内部类实现接口，也可直接使用方法引用（`Fudge::new`）作为轻量级工厂。
 */
class IntegerFactory implements Supplier<Integer> {
    private int i = 0;

    @Override
    public Integer get() {
        return ++i;
    }
}

class Widget {
    private int id;

    Widget(int n) {
        id = n;
    }

    @Override
    public String toString() {
        return "Widget " + id;
    }

    public static class Factory implements Supplier<Widget> {
        private int i = 0;

        @Override
        public Widget get() {
            return new Widget(++i);
        }
    }
}

class Fudge {
    private static int count = 1;
    private int n = count++;

    @Override
    public String toString() {
        return "Fudge " + n;
    }
}

class Foo2<T> {
    private List<T> x = new ArrayList<>();

    Foo2(Supplier<T> factory) {
        Suppliers.fill(x, factory, 5);
    }

    @Override
    public String toString() {
        return x.toString();
    }
}

public class FactoryConstraint {
    public static void main(String[] args) {
        System.out.println(new Foo2<>(new IntegerFactory()));
        System.out.println(new Foo2<>(new Widget.Factory()));
        System.out.println(new Foo2<>(Fudge::new));
    }
}
/* Output:
[1, 2, 3, 4, 5]
[Widget 1, Widget 2, Widget 3, Widget 4, Widget 5]
[Fudge 1, Fudge 2, Fudge 3, Fudge 4, Fudge 5]
 */
