package ch15_exceptions;

/**
 * @author runningpig66
 * @date 2025/11/14 周五
 * @time 18:40
 * 代码清单 P.443 使用 finally 执行清理
 * The finally clause is always executed
 * <p>
 * 这个程序演示了 finally 子句总会执行。从输出可以看出，无论是否抛出异常，finally 子句都执行了。
 */
class ThreeException extends Exception {
}

public class FinallyWorks {
    static int count = 0;

    public static void main(String[] args) {
        while (true) {
            try {
                // Post-increment is zero first time:
                if (count++ == 0) {
                    throw new ThreeException();
                }
                System.out.println("No exception");
            } catch (ThreeException e) {
                System.out.println("ThreeException");
            } finally {
                System.out.println("In finally clause");
                // Java 中的异常不允许我们回退到异常被抛出的地方，正如前面所讨论的。
                // 如果把try块放到一个循环中，我们可以设置一个在程序继续执行之前必须满足的条件。
                // 还可以添加一个静态的计数器，或其他某种设施，让循环在放弃之前尝试几种不向的方法。
                // 这样可以提高程序的稳健性。
                if (count == 2) {
                    break; // out of "while"
                }
            }
        }
    }
}
/* Output:
ThreeException
In finally clause
No exception
In finally clause
 */
