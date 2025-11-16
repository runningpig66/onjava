package ch15_exceptions;

/**
 * @author runningpig66
 * @date 2025/11/15 周六
 * @time 16:52
 * 代码清单 P.447 使用 finally 执行清理：缺陷：异常丢失
 * How an exception can be lost
 * <p>
 * 遗憾的是，Java 的异常实现有一点不足。尽管异常作为程序出错的标志绝对不应忽略，
 * 但是异常有可能丢失。这种情况发生在某种特殊的 finally 子句的使用情形之下：
 * <p>
 * 我们在输出中没有看到 VeryImportantException, 它被 finally 子句中的 HoHumException 取代了。
 * 这是相当严重的缺陷，因为它意味着一个异常可能会完全丢失，而且是以比前面的示例更微妙，更难以察觉的方式。
 * 相比之下，C++ 会将在第一个异常被处理之前抛出第二个异常的情况视为严重的编程错误。或许 Java 未来的版本会修复这个问题
 * （另一方面．我们通常会把任何可能抛出异常的方法————比如以下示例中的 dispose()————包在一个 try-catch 子句中)。
 */
class VeryImportantException extends Exception {
    @Override
    public String toString() {
        return "A very important exception!";
    }
}

class HoHumException extends Exception {
    @Override
    public String toString() {
        return "A trivial exception";
    }
}

public class LostMessage {
    void f() throws VeryImportantException {
        throw new VeryImportantException();
    }

    void dispose() throws HoHumException {
        throw new HoHumException();
    }

    public static void main(String[] args) {
        try {
            LostMessage lm = new LostMessage();
            try {
                lm.f();
            } finally {
                lm.dispose();
                // 我们通常会把任何可能抛出异常的方法————比如以上示例中的 dispose()————包在一个 try-catch 子句中
                // C++ 会将在第一个异常被处理之前抛出第二个异常的情况视为严重的编程错误。
                /*try {
                    lm.dispose();
                } catch (HoHumException e) {
                    System.out.println(e);
                }*/
            }
        } catch (VeryImportantException | HoHumException e) {
            System.out.println(e);
        }
    }
}
/* Output:
A trivial exception
 */
