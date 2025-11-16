package ch15_exceptions;

/**
 * @author runningpig66
 * @date 2025/11/15 周六
 * @time 16:32
 * 代码清单 P.446 使用 finally 执行清理：finally 是干什么用的
 * Finally is always executed
 * <p>
 * 即使抛出的异常没有被当前的这组 catch 子句捕获，
 * 在异常处理机制向更高一层中继续搜索异常处理程序之前，finally 也会执行。
 * <p>
 * 当涉及 break 和 continue 语句时，finally 语句也会执行。
 * 将 finally 与带标签的 break 和 continue 配合使用，Java 中就不再需要 goto 语句了。
 */
class FourException extends Exception {
}

public class AlwaysFinally {
    public static void main(String[] args) {
        System.out.println("Entering first try block");
        try {
            System.out.println("Entering second try block");
            try {
                throw new FourException();
            } finally {
                System.out.println("finally in 2nd try block");
            }
        } catch (FourException e) {
            System.out.println("Caught FourException in 1st try block");
        } finally {
            System.out.println("finally in 1st try block");
        }
    }
}
/* Output:
Entering first try block
Entering second try bloc
finally in 2nd try block
Caught FourException in 1st try block
finally in 1st try block
 */
