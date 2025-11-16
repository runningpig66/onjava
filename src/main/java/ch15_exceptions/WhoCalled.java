package ch15_exceptions;

/**
 * @author runningpig66
 * @date 2025/11/11 周二
 * @time 18:04
 * 代码清单 P.432 捕捉任何异常：栈轨迹
 * Programmatic access to stack trace information
 * <p>
 * printStackTrace() 提供的信息也可以使用 getStackTrace() 直接访问。
 * 这个方法会返回一个由栈轨迹元素组成的数组．每个元素表示一个栈帧。
 * 元素 0 是栈顶，而且它是序列中的最后一个方法调用（这个 Throwable 被创建和抛出的位置）。
 * 数组中的最后一个元素和栈底则是序列中的第一个方法调用。下面是一个简单的演示。
 * public StackTraceElement[] getStackTrace()
 */
public class WhoCalled {
    static void f() {
        // Generate an exception to fill in the stack trace
        try {
            throw new Exception();
        } catch (Exception e) {
            for (StackTraceElement ste : e.getStackTrace()) {
                System.out.println(ste.getMethodName());
            }
        }
    }

    static void g() {
        f();
    }

    static void h() {
        g();
    }

    public static void main(String[] args) {
        f();
        System.out.println("*******");
        g();
        System.out.println("*******");
        h();
    }
}
/* Output:
f
main
*******
f
g
main
*******
f
g
h
main
 */
