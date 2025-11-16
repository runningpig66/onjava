package ch15_exceptions;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author runningpig66
 * @date 2025/11/16 周日
 * @time 5:06
 * 代码清单 P.470 其他可选方式：把异常传递到控制台
 * <p>
 * 在简单的程序中，要想在不编写大量代码的悄况下保留异常，最简单的方法是将它们从 main() 中传递到控制台。
 * 通过将其传递给控制台，我们就不用在 main() 的方法体内编写 try-catch 子句了。
 * 不幸的是，有些文件 I/O 可能要比这个示例中复杂得多。第 17 章和进阶卷第 7 章会进一步介绍。
 */
public class MainException {
    // Pass exceptions to the console:
    public static void main(String[] args) throws Exception {
        // Open the file:
        List<String> lines = Files.readAllLines(Paths.get("src/main/java/ch15_exceptions/MainException.java"));
        // Use the file ...
    }
}
