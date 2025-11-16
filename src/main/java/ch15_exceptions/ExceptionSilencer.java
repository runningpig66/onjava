package ch15_exceptions;

/**
 * @author runningpig66
 * @date 2025/11/15 周六
 * @time 17:21
 * 代码清单 P.448 使用 finally 执行清理：缺陷：异常丢失
 * <p>
 * 还有一种更简单的会丢失异常的方式，是在 finally 子句中执行 return。
 * 运行这个程序，我们会发现它没有任何输出，尽管有一个异常被抛出了。
 */
public class ExceptionSilencer {
    public static void main(String[] args) {
        try {
            throw new RuntimeException();
        } finally {
            // Using 'return' inside the finally block
            // will silence any thrown exception.
            return;
        }
    }
}
