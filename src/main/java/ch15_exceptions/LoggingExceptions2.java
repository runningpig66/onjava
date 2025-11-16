package ch15_exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

/**
 * @author runningpig66
 * @date 2025/11/11 周二
 * @time 15:32
 * 代码清单 P.425 创建自己的异常：异常与日志记录
 * <p>
 * LoggingException 用到的方法非常方便，因为它把所有的日志基础设施都构建在了异常本身之中，
 * 因此它可以自动工作，无须客户程序员干预。然而，更常见的情况是捕捉别人的异常，
 * 并将其记录到日志中，所以我们必须在异常处理程序中生成日志信息。
 * <p>
 * Logging caught exceptions
 */
public class LoggingExceptions2 {
    private static final Logger logger = Logger.getLogger("LoggingExceptions2");

    static void logException(Exception e) {
        StringWriter trace = new StringWriter();
        e.printStackTrace(new PrintWriter(trace));
        logger.severe(trace.toString());
    }

    public static void main(String[] args) {
        // 控制台日期乱码，使用 gradle.properties 全局配置代替以下注释行代码。
        // Locale.setDefault(Locale.US);
        try {
            throw new NullPointerException();
        } catch (NullPointerException e) {
            logException(e);
        }
    }
}
/* Output:
Nov 16, 2025 2:42:21 AM ch15_exceptions.LoggingExceptions2 logException
SEVERE: java.lang.NullPointerException
	at ch15_exceptions.LoggingExceptions2.main(LoggingExceptions2.java:33)
 */
