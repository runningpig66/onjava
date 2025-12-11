package ch17_files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

/**
 * @author runningpig66
 * @date 2025/12/11 周四
 * @time 11:24
 * P.544 §17.6 读写文件
 * {Working directory: E:\IdeaProjects\onjava\src\main\java\ch17_files}
 * <p>
 * Files.write() 也被重载了，可以将 byte 数组或任何实现了 Iterable 接口的类的对象（还包括一个 Charset 选项）写入文件。
 * public static Path write(Path path, byte[] bytes, OpenOption... options) throws IOException
 * public static Path write(Path path, Iterable<? extends CharSequence> lines, OpenOption... options) throws IOException
 * public static Path write(Path path, Iterable<? extends CharSequence> lines, Charset cs, OpenOption... options) throws IOException
 */
public class Writing {
    static Random rand = new Random(47);
    static final int SIZE = 1000;

    public static void main(String[] args) throws IOException {
        // Write bytes to a file:
        byte[] bytes = new byte[SIZE];
        rand.nextBytes(bytes); // TODO
        Files.write(Paths.get("bytes.dat"), bytes);
        System.out.println("bytes.dat: " + Files.size(Paths.get("bytes.dat")));

        // Write an iterable to a file:
        List<String> lines = Files.readAllLines(Paths.get("../ch14_streams/Cheese.dat"));
        Files.write(Paths.get("Cheese.txt"), lines);
        System.out.println("Cheese.txt: " + Files.size(Paths.get("Cheese.txt")));
    }
}
/* Output:
bytes.dat: 1000
Cheese.txt: 199
 */
