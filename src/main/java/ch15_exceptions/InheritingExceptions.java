package ch15_exceptions;

/**
 * @author runningpig66
 * @date 2025/11/11 周二
 * @time 2:22
 * 代码清单 P.422 创建自己的异常
 * <p>
 * 要创建自已的异常类，可以继承现有的异常类，最好是与我们要定义的新异常含义接近的（尽管这通常是不可能的）。
 * 创建一个新的异常类型的最简单方法就是，让编译器为我们创建无参构造器，几乎不需要任何代码。
 */
class SimpleException extends Exception {
}

public class InheritingExceptions {
    public void f() throws SimpleException {
        System.out.println("Throw SimpleException from f()");
        throw new SimpleException();
    }

    public static void main(String[] args) {
        InheritingExceptions sed = new InheritingExceptions();
        try {
            sed.f();
        } catch (SimpleException e) {
            System.out.println("Caught it!");
        }
    }
}
/* Output:
Throw SimpleException from f()
Caught it!
 */
