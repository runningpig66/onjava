package ch15_exceptions;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/11/16 周日
 * @time 1:18
 * 代码清单 P.458 try-with-resources 语句
 * <p>
 * 它是如何工作的呢？在 try-with-resources 定义子句中（也就是括号内）创建的对象必须
 * 实现 java.lang.AutoCloseable 接口，该接口只有一个方法————close().
 * 当 Java 7 引人 AutoCloseable 时，很多接口和类也被修改了，以实现这个接口。
 * 我们在 AutoCloseable 的 Java 文档中可以看到一份滑单，其中就包括 Stream 类。
 * Java 5 的 Closeable 接口也被修改了，以继承 AutoCloseable,
 * 因此过去支持 Closeable 的任何东西都可以配合 try-with-resources 使用。
 */
public class StreamsAreAutoCloseable {
    public static void main(String[] args) throws IOException {
        final String root = "src/main/java/ch15_exceptions/";
        try (Stream<String> in = Files.lines(Paths.get(root, "StreamsAreAutoCloseable.java"));
             PrintWriter outfile = new PrintWriter(new File(root, "Reuslt.txt"));
             // [1] 在这里可以看到另一个特性：资源说明头可以包含多个定义，用分号隔开（最后的分号也可以接受，不过是可选的)。
             // 在这个头部定义的每个对象都将在 try 块的末尾调用其 close().
        ) {
            in.skip(23)
                    .limit(1)
                    .map(String::toLowerCase)
                    .forEachOrdered(outfile::println);
        } // [2] try-with-resources 的 try 块可以独立存在，没有 catch 或 finally。
        // 这里，IOException 会通过 main() 传递出去，所以不需要在 try 块的末尾捕获。
    }
}
