package ch19_reflection;

import java.lang.reflect.Field;

/**
 * @author runningpig66
 * @date 2025/12/25 周四
 * @time 0:35
 * P.638 §19.9 接口和类型信息
 * <p>
 * 看来没有任何方法可以阻止反射进入并调用非公共访问权限的方法。对于字段，甚至是 private 的字段，也是如此：
 * <p>
 * 不过，final 字段实际上是安全的，不会发生变化。运行时系统在接受任何更改尝试时并不会报错，但实际上什么也不会发生。
 * <p>
 * 一般来说，这些访问违规并不是世界上最糟糕的事情。如果有人使用这种技术来调用你标记为 private 或包访问权限的方法
 * （即这些方法不应该被调用），那么当你更改这些方法的某些方面时，他们就不应该抱怨。此外，Java 语言提供了一个后门来访问类，
 * 这一事实可以让你能够解决某些特定类型的问题。如果没有这个后门的话，这些问题会难以解决。甚至不可能解决。反射带来的好处通常很难否认。
 */
class WithPrivateFinalField {
    private int i = 1;
    private final String s = "I'm totally safe";
    private String s2 = "Am I safe?";

    @Override
    public String toString() {
        return "i = " + i + ", " + s + ", " + s2;
    }
}

public class ModifyingPrivateFields {
    public static void main(String[] args) throws Exception {
        WithPrivateFinalField pf = new WithPrivateFinalField();
        System.out.println(pf);
        Field f = pf.getClass().getDeclaredField("i");
        f.setAccessible(true);
        System.out.println("f.getInt(pf): " + f.getInt(pf));
        f.setInt(pf, 47);
        System.out.println(pf);
        f = pf.getClass().getDeclaredField("s");
        f.setAccessible(true);
        System.out.println("f.get(pf): " + f.get(pf));
        f.set(pf, "No, you're not!");
        System.out.println(pf); // final field
        f = pf.getClass().getDeclaredField("s2");
        f.setAccessible(true);
        System.out.println("f.get(pf): " + f.get(pf));
        f.set(pf, "No, you're not!");
        System.out.println(pf);
    }
}
/* Output:
i = 1, I'm totally safe, Am I safe?
f.getInt(pf): 1
i = 47, I'm totally safe, Am I safe?
f.get(pf): I'm totally safe
i = 47, I'm totally safe, Am I safe?
f.get(pf): Am I safe?
i = 47, I'm totally safe, No, you're not!
 */
