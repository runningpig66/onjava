package ch17_files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author runningpig66
 * @date 2025/12/11 周四
 * @time 11:11
 * P.543 §17.6 读写文件
 * {Working directory: E:\IdeaProjects\onjava\src\main\java\ch17_files}
 * <p>
 * Files.readAllLines() 可以一次性读入整个文件（这也是“小”文件的重要性），生成一个 List<String>。
 * readAllLines() 有一个重载的版本，还包含一个 Charset 参数，用来确定文件的 Unicode 编码。
 * public static List<String> readAllLines(Path path) throws IOException
 * public static List<String> readAllLines(Path path, Charset cs) throws IOException
 */
public class ListOfLines {
    public static void main(String[] args) throws IOException {
        Files.readAllLines(Paths.get("../ch14_streams/Cheese.dat"))
                .stream()
                .filter(line -> !line.startsWith("//"))
                .map(line -> line.substring(0, line.length() / 2))
                .forEach(System.out::println);
    }
}
/* Output:
Not much of a cheese
Finest in the
And what leads you
Well, it's
It's certainly uncon
 */
