package ch15_exceptions;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author runningpig66
 * @date 2025/11/16 周日
 * @time 5:25
 * 代码清单 P.471 其他可选方式：将“检查型异常”转换为“非检查型异常”
 * "Turning off" Checked exceptions
 * <p>
 * 通过将一个检查型异常传递给 RuntimeException 构造器，我们可以将其包在一个 RuntimeException 中。
 * 这似乎是“关闭”检查型异常的理想方式：我们没有“吞掉”它，也没有将其放在方法的异常说明中，
 * 但是由于异常链的存在，我们不会丢失来自原始异常的任何信息。
 * 这种技巧提供了一种选择，我们可以忽略异常，并使其沿着调用栈向上“冒泡”，而不需要编写 try-catch 子句和异常说明。
 * 然而，我们仍然可以使用 getCause() 来捕捉和处理特定的异常。
 */
class WrapCheckedException {
    // throwRuntimeException() 包含了可以生成不同类型异常的代码。
    // 这些异常都被捕获并包进了 RuntimeException 对象中，所以它们又成了这些运行时异常的 cause 了。
    void throwRuntimeException(int type) {
        try {
            switch (type) {
                case 0:
                    throw new FileNotFoundException();
                case 1:
                    throw new IOException();
                case 2:
                    throw new RuntimeException("Where am I?");
                default:
            }
        } catch (IOException | RuntimeException e) {
            // Adapt to unchecked:
            throw new RuntimeException(e);
        }
    }
}

class SomeOtherException extends Exception {
}

public class TurnOffChecking {
    public static void main(String[] args) {
        WrapCheckedException wce = new WrapCheckedException();
        // You can call throwRuntimeException() without a try block,
        // and let RuntimeExceptions leave the method:
        wce.throwRuntimeException(3);
        // Or you can choose to catch exceptions:
        for (int i = 0; i < 4; i++) {
            try {
                if (i < 3) {
                    wce.throwRuntimeException(i);
                } else {
                    throw new SomeOtherException();
                }
            }
            // 首先要捕捉所有我们明确知道的、会从 try 块内的代码中抛出的异常————在这个示例中，就是先捕捉 SomeOtherException.
            catch (SomeOtherException e) {
                System.out.println("SomeOtherException: " + e);
            }
            // 最后，我们捕捉 RuntimeException, 并抛出 getCause() 的结果（也就是被包起来的那个原始异常）。
            // 这样就把原始的异常提取出来了，然后就可以用它们自己的 catch 子句来处理了。
            catch (RuntimeException re) {
                try {
                    throw re.getCause();
                } catch (FileNotFoundException e) {
                    System.out.println("FileNotFoundException: " + e);
                } catch (IOException e) {
                    System.out.println("IOException: " + e);
                } catch (Throwable e) {
                    System.out.println("Throwable: " + e);
                }
            }
        }
    }
}
/* Output:
FileNotFoundException: java.io.FileNotFoundException
IOException: java.io.IOException
Throwable: java.lang.RuntimeException: Where am I?
SomeOtherException: ch15_exceptions.SomeOtherException
 */
