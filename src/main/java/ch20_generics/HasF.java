package ch20_generics;

/**
 * @author runningpig66
 * @date 2025/12/26 周五
 * @time 18:07
 * P.667 §20.6 类型擦除的奥秘 §20.6.1 C++ 的实现方法
 * <p>
 * 用 C++ 编写这类代码很简单，因为在实例化模板的时候，模板代码知道其自身模板参数的类型。Java 泛型则不同。
 * 下面是用 Java 实现的 HasF：
 */
public class HasF {
    public void f() {
        System.out.println("HasF.f()");
    }
}
