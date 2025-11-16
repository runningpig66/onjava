package ch15_exceptions;

/**
 * @author runningpig66
 * @date 2025/11/14 周五
 * @time 1:40
 * 代码清单 P.434 捕捉任何异常：重新抛出异常
 * Rethrow a different object from the one you caught
 * <p>
 * 重新抛出一个与所捕获的异常不同的异常也是可以的。这样做会得到与使用 fillInStackTrace() 类似的效果，
 * 关于这个异常的原始调用点的信息会丢失，剩下的是与新的 throw 有关的信息。
 */
class OneException extends Exception {
    OneException(String s) {
        super(s);
    }
}

class TwoException extends Exception {
    TwoException(String s) {
        super(s);
    }
}

public class RethrowNew {
    public static void f() throws OneException {
        System.out.println("originating the exception in f()");
        throw new OneException("thrown from f()");
    }

    public static void main(String[] args) {
        try {
            try {
                f();
            } catch (OneException e) {
                System.out.println("Caught in inner try, e.printStackTrace()");
                e.printStackTrace(System.out);
                throw new TwoException("from inner try");
            }
        } catch (TwoException e) {
            System.out.println("Caught in outer try, e.printStackTrace()");
            // 最后的异常只知道它来自内部的 try 块，而不知道它来自 f()。
            e.printStackTrace(System.out);
        }
    }
}
/* Output:
originating the exception in f()
Caught in inner try, e.printStackTrace()
ch15_exceptions.OneException: thrown from f()
	at ch15_exceptions.RethrowNew.f(RethrowNew.java:28)
	at ch15_exceptions.RethrowNew.main(RethrowNew.java:34)
Caught in outer try, e.printStackTrace()
ch15_exceptions.TwoException: from inner try
	at ch15_exceptions.RethrowNew.main(RethrowNew.java:38)
 */
