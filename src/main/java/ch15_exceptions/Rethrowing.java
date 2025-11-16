package ch15_exceptions;

/**
 * @author runningpig66
 * @date 2025/11/14 周五
 * @time 0:39
 * 代码清单 P.433 捕捉任何异常：重新抛出异常
 * Demonstrating fillInStackTrace()
 * <p>
 * 如果重新抛出当前的异常，在 printStackTrace() 中打印的关于异常的信息，
 * 仍将是原来的异常抛出点的信息，而不是重新抛出异常的地方的信息。
 * 要加人新的栈轨迹信息，可以调用 fillInStackTrace()，它会返回一个 Throwable 对象，
 * 这个对象是它通过将当前栈的信息塞到原来的异常对象中而创建的，就像下面这样：
 * <p>
 * notes: fillInStackTrace() 会把“这个异常对象”的栈轨迹在当前调用点重写：丢弃当前方法之上的所有栈帧，
 * 并把本行设为新的抛出起点；异常类型与 message 不变，返回的仍是同一实例。
 * 关于这个异常的原始调用点的信息会丢失。用它等于改写历史；调用有开销，仅在“刻意改起点/隐藏内部细节”的场景用。
 * 想保留原始出处更常见做法是 throw new XxxException(msg, e)（用 cause 留根因）。
 * public synchronized Throwable fillInStackTrace()
 */
public class Rethrowing {
    public static void f() throws Exception {
        System.out.println("originating the exception in f()");
        throw new Exception("thrown from f()");
    }

    public static void g() throws Exception {
        try {
            f();
        } catch (Exception e) {
            System.out.println("Inside g(), e.printStackTrace()");
            e.printStackTrace(System.out);
            throw e;
        }
    }

    public static void h() throws Exception {
        try {
            f();
        } catch (Exception e) {
            System.out.println("Inside h(), e.printStackTrace()");
            e.printStackTrace(System.out);
            // fillInStackTrace() 被调用的那一行，成了这个异常的新起点。
            throw (Exception) e.fillInStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            g();
        } catch (Exception e) {
            System.out.println("main: printStackTrace()");
            e.printStackTrace(System.out);
        }
        try {
            h();
        } catch (Exception e) {
            System.out.println("main: printStackTrace()");
            e.printStackTrace(System.out);
        }
    }
}
/* Output:
originating the exception in f()
Inside g(), e.printStackTrace()
java.lang.Exception: thrown from f()
	at ch15_exceptions.Rethrowing.f(Rethrowing.java:24)
	at ch15_exceptions.Rethrowing.g(Rethrowing.java:29)
	at ch15_exceptions.Rethrowing.main(Rethrowing.java:50)
main: printStackTrace()
java.lang.Exception: thrown from f()
	at ch15_exceptions.Rethrowing.f(Rethrowing.java:24)
	at ch15_exceptions.Rethrowing.g(Rethrowing.java:29)
	at ch15_exceptions.Rethrowing.main(Rethrowing.java:50)
originating the exception in f()
Inside h(), e.printStackTrace()
java.lang.Exception: thrown from f()
	at ch15_exceptions.Rethrowing.f(Rethrowing.java:24)
	at ch15_exceptions.Rethrowing.h(Rethrowing.java:39)
	at ch15_exceptions.Rethrowing.main(Rethrowing.java:56)
main: printStackTrace()
java.lang.Exception: thrown from f()
	at ch15_exceptions.Rethrowing.h(Rethrowing.java:44)
	at ch15_exceptions.Rethrowing.main(Rethrowing.java:56)
 */
