package ch15_exceptions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/11/16 周日
 * @time 0:18
 * 代码清单 P.457 try-with-resources 语句
 * <p>
 * InputFile.java 的一个更好的设计是，在构造器中读取文件，并将其内容在内部缓存下来。
 * 这样的话，文件的打开、读取和关闭都发生在构造器中。
 * 或者，如果读取和存储文件不现实的话，也可以选择生成一个 Stream. 理想情况下应该这样设计：
 */
public class InputFile2 {
    private final String fname;

    public InputFile2(String fname) {
        this.fname = fname;
    }

    // 现在，getLines() 只负责打开文件并创建 Stream.
    public Stream<String> getLines() throws IOException {
        return Files.lines(Paths.get(fname));
    }

    public static void main(String[] args) throws IOException {
        new InputFile2("src/main/java/ch15_exceptions/InputFile2.java").getLines()
                .skip(29)
                .limit(1)
                .forEach(System.out::println);
    }
}
/* Output:
    public static void main(String[] args) throws IOException {
 */
