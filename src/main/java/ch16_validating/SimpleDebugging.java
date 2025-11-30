package ch16_validating;

/**
 * @author runningpig66
 * @date 2025/11/23 周日
 * @time 4:20
 * P.508 §16.5 调试 §16.5.1 使用 JDB 进行调试
 * {ThrowsException}
 */
public class SimpleDebugging {
    private static void foo1() {
        System.out.println("In foo1");
        foo2();
    }

    private static void foo2() {
        System.out.println("In foo2");
        foo3();
    }

    private static void foo3() {
        System.out.println("In foo3");
        int j = 1;
        j--;
        int i = 5 / j;
    }

    public static void main(String[] args) {
        foo1();
    }
}
/* Output:
In foo1
In foo2
In foo3
3 actionable tasks: 2 executed, 1 up-to-date
Exception in thread "main" java.lang.ArithmeticException: / by zero
	at ch16_validating.SimpleDebugging.foo3(SimpleDebugging.java:25)
	at ch16_validating.SimpleDebugging.foo2(SimpleDebugging.java:18)
	at ch16_validating.SimpleDebugging.foo1(SimpleDebugging.java:13)
	at ch16_validating.SimpleDebugging.main(SimpleDebugging.java:29)
 */
