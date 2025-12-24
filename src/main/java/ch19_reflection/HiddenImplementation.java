package ch19_reflection;

import ch19_reflection.interfacea.A;
import ch19_reflection.packageaccess.HiddenC;

import java.lang.reflect.Method;

/**
 * @author runningpig66
 * @date 2025/12/24 周三
 * @time 22:38
 * P.635 §19.9 接口和类型信息
 * Sneaking around package hiding
 * <p>
 * 现在，如果尝试向下转型为 C，你会发现无法做到，因为包外没有可用的 C 类型：
 * <p>
 * 你仍然可以使用反射来访问并调用所有的方法，甚至包括 private 的方法！如果你知道方法的名称，
 * 就可以通过调用 Method 对象的 setAccessible(true) 来设置，从而让这个方法可以被调用，就像 callHiddenMethod() 中所示的那样。
 * <p>
 * 你可能认为通过仅发布已编译的代码可以防止这种情况，但这不是解决方案。只需要运行 JDK 自带的反编译器 javap 就能绕过它。
 * 因此，任何人都可以获取你最私有的方法的名称和签名，并调用它们。
 */
public class HiddenImplementation {
    public static void main(String[] args) throws Exception {
        A a = HiddenC.makeA();
        a.f();
        System.out.println(a.getClass().getName());
        // Compile error: cannot find symbol 'C':
        // 'ch19_reflection.packageaccess.C' is not public in 'ch19_reflection.packageaccess'. Cannot be accessed from outside packag
        /* if (a instanceof C) {
            C c = (C)a;
            c.g();
        } */
        callHiddenMethod(a, "g");
        // And even less accessible methods!
        callHiddenMethod(a, "u");
        callHiddenMethod(a, "v");
        callHiddenMethod(a, "w");
    }

    static void callHiddenMethod(Object a, String methodName) throws Exception {
        Method g = a.getClass().getDeclaredMethod(methodName);
        g.setAccessible(true);
        g.invoke(a);
    }
}
/* Output:
public C.f()
ch19_reflection.packageaccess.C
public C.g()
package C.u()
protected C.v()
private C.w()
 */
