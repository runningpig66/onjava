package ch13_functional;

/**
 * @author runningpig66
 * @date 2025/9/23 周二
 * @time 23:41
 * 代码清单 P.351
 * Method reference without an object
 * 未绑定对象的方法引用
 */
class X {
    String f() {
        return "X::f()";
    }
}

interface MakeString {
    String make();
}

interface TransformX {
    String transform(X x);
}

public class UnboundMethodReference {
    public static void main(String[] args) {
        // MakeString ms = X::f; // [1] ERROR: Non-static method cannot be referenced from a static context
        TransformX sp = X::f;
        X x = new X();
        System.out.println(sp.transform(x)); // [2]
        System.out.println(x.f()); // Same effect
    }
}
/* Output:
X::f()
X::f()
 */
