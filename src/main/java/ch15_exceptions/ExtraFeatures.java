package ch15_exceptions;

/**
 * @author runningpig66
 * @date 2025/11/11 周二
 * @time 16:19
 * 代码清单 P.426 创建自己的异常：异常与日志记录
 * Further embellishment of exception classes
 * <p>
 * 创建自己的异常的过程还可以更进一步。我们可以添加更多构造器和成员。
 */
class MyException2 extends Exception {
    private int x;

    MyException2() {
    }

    MyException2(String msg) {
        super(msg);
    }

    MyException2(String msg, int x) {
        super(msg);
        this.x = x;
    }

    public int val() {
        return x;
    }

    @Override
    public String getMessage() {
        return "Detail Message: " + x + " " + super.getMessage();
    }
}

public class ExtraFeatures {
    public static void f() throws MyException2 {
        System.out.println("Throwing MyException2 from f()");
        throw new MyException2();
    }

    public static void g() throws MyException2 {
        System.out.println("Throwing MyException2 from g()");
        throw new MyException2("Originated in g()");
    }

    public static void h() throws MyException2 {
        System.out.println("Throwing MyException2 from h()");
        throw new MyException2("Originated in h()", 47);
    }

    public static void main(String[] args) {
        try {
            f();
        } catch (MyException2 e) {
            e.printStackTrace(System.out);
        }
        try {
            g();
        } catch (MyException2 e) {
            e.printStackTrace(System.out);
        }
        try {
            h();
        } catch (MyException2 e) {
            e.printStackTrace(System.out);
            System.out.println("e.val() = " + e.val());
        }
    }
}
/* Output:
Throwing MyException2 from f()
ch15_exceptions.MyException2: Detail Message: 0 null
	at ch15_exceptions.ExtraFeatures.f(ExtraFeatures.java:40)
	at ch15_exceptions.ExtraFeatures.main(ExtraFeatures.java:55)
Throwing MyException2 from g()
ch15_exceptions.MyException2: Detail Message: 0 Originated in g()
	at ch15_exceptions.ExtraFeatures.g(ExtraFeatures.java:45)
	at ch15_exceptions.ExtraFeatures.main(ExtraFeatures.java:60)
Throwing MyException2 from h()
ch15_exceptions.MyException2: Detail Message: 47 Originated in h()
	at ch15_exceptions.ExtraFeatures.h(ExtraFeatures.java:50)
	at ch15_exceptions.ExtraFeatures.main(ExtraFeatures.java:65)
e.val() = 47
 */
