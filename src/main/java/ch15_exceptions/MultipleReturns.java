package ch15_exceptions;

/**
 * @author runningpig66
 * @date 2025/11/15 周六
 * @time 16:41
 * 代码清单 P.446 使用 finally 执行清理：在 return 期间使用 finally
 * <p>
 * 因为 finally 子句总会执行，所以在一个方祛中，我们可以从多个点返回，并且仍然能够确保重要的清理工作得到执行。
 * 输出表明，从哪里返回并不重要，finally 子句总会运行。
 */
public class MultipleReturns {
    public static void f(int i) {
        System.out.println("Initialization that requires cleanup");
        try {
            System.out.println("Point 1");
            if (i == 1) return;
            System.out.println("Point 2");
            if (i == 2) return;
            System.out.println("Point 3");
            if (i == 3) return;
            System.out.println("End");
        } finally {
            System.out.println("Performing cleanup");
        }
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 4; i++) {
            f(i);
        }
    }
}
/* Output:
Initialization that requires cleanup
Point 1
Performing cleanup
Initialization that requires cleanup
Point 1
Point 2
Performing cleanup
Initialization that requires cleanup
Point 1
Point 2
Point 3
Performing cleanup
Initialization that requires cleanup
Point 1
Point 2
Point 3
End
Performing cleanup
 */
