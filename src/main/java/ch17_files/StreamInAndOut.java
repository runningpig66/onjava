package ch17_files;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/12/11 周四
 * @time 12:07
 * P.545 §17.6 读写文件
 * {Working directory: E:\IdeaProjects\onjava\src\main\java\ch17_files}
 * <p>
 * 如果把文件当作一个由行组成的输入流来处理，那么 Files.lines() 非常有用，
 * 但是如果我们想在一个流中完成读取、处理和写入，那该怎么办呢？这就需要稍微复杂些的代码了。
 * <p>
 * 因为我们是在同一个块中执行的所有操作，所以两个文件可以在相同的 try-with-resources 块中打开。
 * PrintWriter 是一个旧式的 java.io 类，允许我们“打印”到一个文件，所以它是这个应用的理想选择。
 * 如果看一下 StreamInAndOut.txt，会发现里面的内容确实是全部大写的。
 */
public class StreamInAndOut {
    public static void main(String[] args) {
        try (Stream<String> input = Files.lines(Paths.get("StreamInAndOut.java"));
             PrintWriter output = new PrintWriter("StreamInAndOut.txt")) {
            input.map(String::toUpperCase).forEachOrdered(output::println);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
