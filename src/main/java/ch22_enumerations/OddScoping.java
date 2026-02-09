package ch22_enumerations;

/**
 * @author runningpig66
 * @date 2月9日 周一
 * @time 2:48
 * P.048 §1.16 新特性：智能转型
 * {NewFeature} Since JDK 16
 * <p>
 * [1] 只有在不引入会引发异常的语句时，s 才在作用域中。
 * 如果注释掉 throw new RuntimeException()，编译器便会告诉它无法在行 [1] 中找到 s——这是你通常所预期的行为。
 * 乍一看这像个 bug，但它就是这么设计的——该行为在 JEP 394 中有着明确的描述。
 * 尽管这可以说是一个极端情况，但你能想象追踪由这种行为引起的 bug 会有多么困难。
 */
public class OddScoping {
    static void f(Object o) {
        if (!(o instanceof String s)) {
            System.out.println("Not a String");
            throw new RuntimeException();
        }
        // s is in scope here!
        System.out.println(s.toUpperCase()); // [1]
    }

    public static void main(String[] args) {
        f("Curiouser and Curiouser");
        f(null);
    }
}
/* Output:
CURIOUSER AND CURIOUSER
Not a String
Exception in thread "main" java.lang.RuntimeException
	at ch22_enumerations.OddScoping.f(OddScoping.java:14)
	at ch22_enumerations.OddScoping.main(OddScoping.java:22)
 */
