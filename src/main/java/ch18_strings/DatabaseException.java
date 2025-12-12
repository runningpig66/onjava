package ch18_strings;

/**
 * @author runningpig66
 * @date 2025/12/12 周五
 * @time 20:40
 * P.562 §18.5 格式化输出 §18.5.6 String.format()
 * <p>
 * Java 5 还借鉴了 C 语言中用来创建字符串的 sprintf()，提供了 String.format() 方法。
 * 它是一个静态方法，参数与 Formatter 类的 format() 方法完全相同，但返回一个 String。
 * 当只调用一次 format() 时，这个方法用起来就很方便：
 * public static String format(String format, Object... args)
 */
public class DatabaseException extends Exception {
    public DatabaseException(int transactionID, int queryID, String message) {
        super(String.format("(t%d, q%d) %s", transactionID, queryID, message));
    }

    public static void main(String[] args) {
        try {
            throw new DatabaseException(3, 7, "Write failed");
        } catch (DatabaseException e) {
            System.out.println(e);
        }
    }
}
/* Output:
ch18_strings.DatabaseException: (t3, q7) Write failed
 */
