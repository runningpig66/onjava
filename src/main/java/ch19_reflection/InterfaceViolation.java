package ch19_reflection;

import ch19_reflection.interfacea.A;

/**
 * @author runningpig66
 * @date 2025/12/24 周三
 * @time 22:00
 * P.634 §19.9 接口和类型信息
 * Sneaking around an interface
 * <p>
 * 下面的示例显示了如何偷偷访问实际的实现类型：通过反射，可以发现 a 实际上是被当作 B 实现的。通过强制转型为 B，我们可以调用不在 A 中的方法。
 * <p>
 * 这是完全合法并且可接受的，但你可能不希望客户程序员这样做，因为这给了他们一个机会，让他们的代码与你的代码耦合程度超出你的期望。
 * 也就是说，你可能认为 interface 关键字正在保护着你，但事实并非如此，而且本例中使用 B 来实现 A 这一事实，实际上是公开可见的。
 * <p>
 * getClass() 的定义： Object.getClass() 是一个 final 的 native 方法。它的职责是去堆内存里看这个对象的“出生证明”。
 * 无论你把这个对象向上转型（Upcast）成 A 接口、Object 类，还是其他父类，对象本身的类型信息（Class Metadata）是不会改变的。
 * public final native Class<?> getClass();
 */
class B implements A {
    @Override
    public void f() {
    }

    public void g() {
    }
}

public class InterfaceViolation {
    public static void main(String[] args) {
        A a = new B();
        a.f();
        // a.g(); // Compile error
        // getClass() 永远返回对象在内存中的“真实身份”（即运行时的确切类型），而不管你用什么“标签”（引用类型）去指向它。
        // a.getClass()：动态的。询问“你到底是谁？”。如果你给它一个 B，它就返回 B.class。
        System.out.println(a.getClass().getName());
        // A.class 字面量：静态的。这仅仅是拿到接口 A 的类对象。
        System.out.println(A.class.getName());
        if (a instanceof B) {
            B b = (B) a;
            b.g();
        }
    }
}
/* Output:
ch19_reflection.B
ch19_reflection.interfacea.A
 */
