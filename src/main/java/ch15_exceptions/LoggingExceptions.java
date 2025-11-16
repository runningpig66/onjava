package ch15_exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

/**
 * @author runningpig66
 * @date 2025/11/11 周二
 * @time 14:50
 * 代码清单 P.424 创建自己的异常：异常与日志记录
 * <p>
 * 我们可以使用 java.util.logging 工具将输出记录到日志中。
 * <p>
 * An exception that reports through a Logger
 */
class LoggingException extends Exception {
    private static final Logger logger = Logger.getLogger("LoggingException");

    LoggingException() {
        StringWriter trace = new StringWriter();
        printStackTrace(new PrintWriter(trace));
        logger.severe(trace.toString());
    }
}

public class LoggingExceptions {
    public static void main(String[] args) {
        // 控制台日期乱码，使用 gradle.properties 全局配置代替以下注释行代码。
        // Locale.setDefault(Locale.US);
        try {
            throw new LoggingException();
        } catch (LoggingException e) {
            System.err.println("Caught " + e);
        }
        try {
            throw new LoggingException();
        } catch (LoggingException e) {
            System.err.println("Caught " + e);
        }
    }
}
/* Output:
Nov 16, 2025 2:42:43 AM ch15_exceptions.LoggingException <init>
SEVERE: ch15_exceptions.LoggingException
	at ch15_exceptions.LoggingExceptions.main(LoggingExceptions.java:32)

Caught ch15_exceptions.LoggingException
Nov 16, 2025 2:42:43 AM ch15_exceptions.LoggingException <init>
SEVERE: ch15_exceptions.LoggingException
	at ch15_exceptions.LoggingExceptions.main(LoggingExceptions.java:37)

Caught ch15_exceptions.LoggingException
 */
