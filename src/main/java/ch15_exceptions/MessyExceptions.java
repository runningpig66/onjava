package ch15_exceptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author runningpig66
 * @date 2025/11/16 周日
 * @time 0:29
 * 代码清单 P.457 try-with-resources 语句
 * <p>
 * 我们并不是总能这么轻易地回避该问题 (InputFile2.java). 有时有些对象会出现如下的情况：
 * 1. 需要清理；
 * 2. 需要在特定时刻清理————当走出某个作用域的时候（通过正常方式或通过异常）。
 * 一个常见的例子是 java.io.FileInputStream（会在进阶卷第7章中描述）。
 * 为了正确使用它，我们必须写一些棘手的样板代码。
 */
public class MessyExceptions {
    public static void main(String[] args) {
        InputStream in = null;
        try {
            in = new FileInputStream(new File("src/main/java/ch15_exceptions/MessyExceptions.java"));
            int contents = in.read();
            // Process contents
        } catch (IOException e) {
            // Handle errors
        } finally {
            if (in != null) {
                // 当 finally 子句中又有自己的 try 块时，感觉事情已经变得过于复杂了。
                try {
                    in.close();
                } catch (IOException e) {
                    // Handle the close() error
                }
            }
        }
    }
}
