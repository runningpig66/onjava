package ch17_files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author runningpig66
 * @date 2025/12/11 周四
 * @time 11:46
 * P.544 §17.6 读写文件
 * {Working directory: E:\IdeaProjects\onjava\src\main\java\ch17_files}
 * <p>
 * 如果文件大小是个问题怎么办？可能是以下情况之一：
 * 1. 这个文件非常大，如果一次性读取整个文件，可能会耗尽内存；
 * 2. 我们只需要文件的一部分就能得到想要的结果，所以读取整个文件是在浪费时间。
 * Files.lines() 可以很方便地将一个文件变为一个由行组成的 Stream。
 * public static Stream<String> lines(Path path) throws IOException
 * public static Stream<String> lines(Path path, Charset cs) throws IOException
 */
public class ReadLineStream {
    public static void main(String[] args) throws IOException {
        Files.lines(Paths.get("PathInfo.java"))
                .skip(24)
                .findFirst()
                .ifPresent(System.out::println);
    }
}
/* Output:
        show("RegularFile: ", Files.isRegularFile(p));
 */
