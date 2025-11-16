package ch15_exceptions;

/**
 * @author runningpig66
 * @date 2025/11/11 周二
 * @time 17:16
 * 代码清单 P.429 捕捉任何异常
 * Demonstrating the Exception Methods
 * <p>
 * 下面的示例演示了 Exception 类的基本方法。
 * <p>
 * 以下代码用来获取详细信息，或者针对特定区域设暨调整过的信息。
 * public String getMessage()
 * public String getLocalizedMessage()
 * <p>
 * 以下代码返回 Throwable 的简短描述，如果有详细信息的话，也包含在里面。
 * public String toString()
 * <p>
 * 以下代码打印 Throwable 和 Throwable 的调用栈轨迹。调用栈显示了把我们带到异常抛出点的方法调用序列。
 * 第一个版本打印到标准错误流，第二个和第兰个版本打印到我们选择的流（在进阶卷第 7 章中，我们将了解为什么会有两种类型的流）。
 * public void printStackTrace()
 * public void printStackTrace(PrintStream s)
 * private void printStackTrace(PrintStreamOrWriter s)
 */
public class ExceptionMethods {
    public static void main(String[] args) {
        try {
            throw new Exception("My Exception");
        } catch (Exception e) {
            System.out.println("Caught Exception");
            System.out.println("getMessage(): " + e.getMessage());
            System.out.println("getLocalizedMessage(): " + e.getLocalizedMessage());
            System.out.println("toString(): " + e);
            System.out.println("printStackTrace(): ");
            e.printStackTrace(System.out);
            // 可以发现每个方法都比前一个方法提供了更多信息————实际上每个方法都是前一个方法的超集。
        }
    }
}
/* Output:
Caught Exception
getMessage(): My Exception
getLocalizedMessage(): My Exception
toString(): java.lang.Exception: My Exception
printStackTrace():
java.lang.Exception: My Exception
	at ch15_exceptions.ExceptionMethods.main(ExceptionMethods.java:16)
 */
