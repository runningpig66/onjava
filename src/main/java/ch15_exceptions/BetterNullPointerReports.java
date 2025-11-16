package ch15_exceptions;

/**
 * @author runningpig66
 * @date 2025/11/14 周五
 * @time 18:27
 * 代码清单 P.442 新特性：更好的 NullPointerException 报告机制
 * {NewFeature} Since JDK 15
 * <p>
 * NullPointerException 有个令人丧的问题：当我们遇到这种情况时，能看到的信息不多。
 * JDK 15 敲定了更好的 NullPointerException 报告机制。考虑如下示例，null 被插入一个对象链条中。
 * <p>
 * 当使用 JDK 8 编译并运行时，几乎没什么信息：
 * null
 * java.lang.NullPointerException
 * java.lang.NullPointerException
 * <p>
 * 然而如果使用JDK15或更高版本，可以看到：
 * null
 * java.lang.NullPointerException: Cannot read field "s" because "c.b.a" is null
 * java.lang.NullPointerException: Cannot read field "a" because "c.b" is null
 */
class A {
    String s;

    A(String s) {
        this.s = s;
    }
}

class B {
    A a;

    B(A a) {
        this.a = a;
    }
}

class C {
    B b;

    C(B b) {
        this.b = b;
    }
}

public class BetterNullPointerReports {
    public static void main(String[] args) {
        C[] ca = {
                new C(new B(new A(null))),
                new C(new B(null)),
                new C(null),
        };
        for (C c : ca) {
            try {
                System.out.println(c.b.a.s);
            } catch (NullPointerException npe) {
                System.out.println(npe);
            }
        }
    }
}
/* Output:
null
java.lang.NullPointerException: Cannot read field "s" because "c.b.a" is null
java.lang.NullPointerException: Cannot read field "a" because "c.b" is null
 */
