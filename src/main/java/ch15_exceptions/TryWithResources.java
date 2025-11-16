package ch15_exceptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author runningpig66
 * @date 2025/11/16 周日
 * @time 0:48
 * 代码清单 P.458 try-with-resources 语句
 * <p>
 * 上一个示例 MessyExceptions.java 中：当 finally 子句中又有自己的 try 块时，感觉事情已经变得过于复杂了。
 * 幸好 Java7 引人了 try-with-resources 语法，可以很好地简化上述代码。
 * <p>
 * 在 Java7 之前，try 后面总是要跟着一个 {, 但是现在它后面可以跟一个括号定义，
 * 我们在这里创建了 FileInputStream 对象。括号中的内容叫作资源说明头 (resource specification header).
 * 现在 in 在这个 try 块的其余部分都是可用的。更重要的是，不管如何退出 try 块（无论是正常方式还是通过异常），
 * 都会执行与上一个示例中的 finally 子句等同的操作，但是不需要编写那么复杂棘手的代码了。这是一个重要的改进。
 */
public class TryWithResources {
    public static void main(String[] args) {
        try (InputStream in = new FileInputStream(
                new File("src/main/java/ch15_exceptions/TryWithResources.java"))
        ) {
            int contents = in.read();
            // Process contents
        } catch (IOException e) {
            // Handle errors
        }
    }
}
