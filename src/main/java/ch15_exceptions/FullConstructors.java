package ch15_exceptions;

/**
 * @author runningpig66
 * @date 2025/11/11 周二
 * @time 2:29
 * 代码清单 P.422 创建自己的异常
 * <p>
 * 还可以创建一个异常类，使其带有接受一个 String 参数的构造器：
 */
class MyException extends Exception {
    MyException() {
    }

    MyException(String msg) {
        super(msg);
    }
}

public class FullConstructors {
    public static void f() throws MyException {
        System.out.println("Throwing MyException from f()");
        throw new MyException();
    }

    public static void g() throws MyException {
        System.out.println("Throwing MyException from g()");
        throw new MyException("Originated in g()");
    }

    public static void main(String[] args) {
        try {
            f();
        } catch (MyException e) {
            e.printStackTrace(System.out);
        }
        try {
            g();
        } catch (MyException e) {
            e.printStackTrace(System.out);
        }
    }
}
/* Output:
Throwing MyException from f()
ch15_exceptions.MyException
	at ch15_exceptions.FullConstructors.f(FullConstructors.java:23)
	at ch15_exceptions.FullConstructors.main(FullConstructors.java:33)
Throwing MyException from g()
ch15_exceptions.MyException: Originated in g()
	at ch15_exceptions.FullConstructors.g(FullConstructors.java:28)
	at ch15_exceptions.FullConstructors.main(FullConstructors.java:38)
 */
