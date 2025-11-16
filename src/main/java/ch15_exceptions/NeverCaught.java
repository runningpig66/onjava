package ch15_exceptions;

/**
 * @author runningpig66
 * @date 2025/11/14 周五
 * @time 18:18
 * 代码清单 P.441 标准Java异常
 * {ThrowsException}
 * <p>
 * 我们从来不会在异常说明里说一个方法可能会抛出 RuntimeException（或者任何继承自 RuntimeException 的类型），
 * 因为它们是“非检查型异常” (unchecked exception).
 * 如果不捕捉这类异常，会发生什么呢？编译器不会强制我们将其放到异常说明之中，
 * 所以一个 RuntimeException 有可能层层渗透，直到进人我们的 main() 方法中，而不会被捕获。
 * 如果一个 RuntimeException 一直到 main() 都没有被捕获，这个异常的 printStackTrace() 会在程序退出时被调用。
 */
public class NeverCaught {
    static void f() {
        throw new RuntimeException("From f()");
    }

    static void g() {
        f();
    }

    public static void main(String[] args) {
        g();
    }
}
/* Output:
Exception in thread "main" java.lang.RuntimeException: From f()
	at ch15_exceptions.NeverCaught.f(NeverCaught.java:17)
	at ch15_exceptions.NeverCaught.g(NeverCaught.java:21)
	at ch15_exceptions.NeverCaught.main(NeverCaught.java:25)
 */
