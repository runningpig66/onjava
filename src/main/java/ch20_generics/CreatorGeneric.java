package ch20_generics;

/**
 * @author runningpig66
 * @date 2025/12/29 周一
 * @time 23:25
 * P.680 §20.7 对类型擦除的补偿 §20.7.1 创建类型实例
 * <p>
 * 另一种方式是使用设计模式：模板方法（Template Method）。
 * 在下面的示例中，create() 就是那个模板方法，其在子类中被重写，用来生成该类型的对象：
 * <p>
 * GenericWithCreate 中包含了 element 字段，并强制通过无参构造器来进行自身的初始化，
 * 然后调用 abstract create() 方法。这种方法可以将创建逻辑定义在子类中，同时 T 的类型也得到了确定。
 */
abstract class GenericWithCreate<T> {
    final T element;

    GenericWithCreate() {
        element = create();
    }

    abstract T create();
}

class X {
}

class XCreator extends GenericWithCreate<X> {
    @Override
    X create() {
        return new X();
    }

    void f() {
        System.out.println(element.getClass().getSimpleName());
    }
}

public class CreatorGeneric {
    public static void main(String[] args) {
        XCreator xc = new XCreator();
        xc.f();
    }
}
/* Output:
X
 */
