package ch19_reflection;

import ch19_reflection.interfacea.A;

/**
 * @author runningpig66
 * @date 2025/12/24 周三
 * @time 23:07
 * P.636 §19.9 接口和类型信息
 * Private inner classes can't hide from reflection.
 * <p>
 * 如果将接口实现为私有内部类会怎样？下面的示例展示了这种情况：
 * <p>
 * 这里对反射仍然没有隐藏任何东西。
 */
class InnerA {
    private static class C implements A {
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

        protected void v() {
            System.out.println("protected C.v()");
        }

        private void w() {
            System.out.println("private C.w()");
        }
    }

    public static A makeA() {
        return new C();
    }
}

public class InnerImplementation {
    public static void main(String[] args) throws Exception {
        A a = InnerA.makeA();
        a.f();
        System.out.println(a.getClass().getName());
        // Reflection still gets into the private class:
        HiddenImplementation.callHiddenMethod(a, "g");
        HiddenImplementation.callHiddenMethod(a, "u");
        HiddenImplementation.callHiddenMethod(a, "v");
        HiddenImplementation.callHiddenMethod(a, "w");
    }
}
/* Output:
public C.f()
ch19_reflection.InnerA$C
public C.g()
package C.u()
protected C.v()
private C.w()
 */
