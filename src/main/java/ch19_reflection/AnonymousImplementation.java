package ch19_reflection;

import ch19_reflection.interfacea.A;

/**
 * @author runningpig66
 * @date 2025/12/24 周三
 * @time 23:25
 * P.637 §19.9 接口和类型信息
 * Anonymous inner classes can't hide from reflection
 * <p>
 * 这里对反射仍然没有隐藏任何东西。那么匿名类呢？匿名内部类仍无法躲过反射。
 */
class AnonymousA {
    public static A makeA() {
        return new A() {
            @Override
            public void f() {
                System.out.println("public C.f()");
            }

            public void g() {
                System.out.println("public C.g()");
            }

            void u() {
                System.out.println("package C.u()");
            }

            // Class member declared 'protected' in 'final' class
            // 匿名内部类在 Java 中默认是 final 的，而在一个 final 类中使用 protected 修饰符是“逻辑冗余”且“误导”的。
            // 根据 Java 语言规范（JLS），所有匿名内部类都是 final 的。
            // 这意味着你永远无法写出类似 class MySubClass extends (某个匿名类) 这样的代码。因为它没有名字，所以它无法被继承。
            // 在 Java 中，protected 的权限范围是：1. 同包内的所有类可见。2. 不同包的子类可见。
            // 在 final 类中写 protected，其效果和“什么都不写”的默认（package-private）权限在功能上是完全等价的。
            protected void v() {
                System.out.println("protected C.v()");
            }

            private void w() {
                System.out.println("private C.w()");
            }
        };
    }
}

public class AnonymousImplementation {
    public static void main(String[] args) throws Exception {
        A a = AnonymousA.makeA();
        a.f();
        System.out.println(a.getClass().getName());
        // Reflection still gets into the anonymous class:
        HiddenImplementation.callHiddenMethod(a, "g");
        HiddenImplementation.callHiddenMethod(a, "u");
        HiddenImplementation.callHiddenMethod(a, "v");
        HiddenImplementation.callHiddenMethod(a, "w");
    }
}
/* Output:
public C.f()
ch19_reflection.AnonymousA$1
public C.g()
package C.u()
protected C.v()
private C.w()
 */
