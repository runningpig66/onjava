package ch17_files;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author runningpig66
 * @date 2025/12/3 周三
 * @time 21:02
 * P.532 §17.1 文件和目录路径 §17.1.3 添加或删除路径片段
 * {Working directory: E:\IdeaProjects\onjava\src\main\java\ch17_files}
 * <p>
 * 我们必须能够通过对自已的 Path 对象添加和删除某些路径片段来构建 Path 对象。
 * 要去掉 Path 这个基准路径，应该使用 relativize(). 要在一个 Path 对象的后面增加路径片段则应该使用 resolve().
 * <p>
 * public static Path get(String first, String... more)
 * public static Path of(String first, String... more)
 * Path toAbsolutePath();
 * Path normalize();
 * Path relativize(Path other);
 * Path toRealPath(LinkOption... options) throws IOException;
 * Path resolve(String other)
 * default Path resolveSibling(String other)
 * <p>
 * notes: AddAndSubtractPaths.md
 * AddAndSubtractPathsTest.java md 的练习测试
 * notes: AddAndSubtractPaths2.md（补充疑问）
 */
public class AddAndSubtractPaths {
    static Path base = Paths.get("..", "..", "..")
            .toAbsolutePath()
            .normalize();

    static void show(int id, Path result) {
        if (result.isAbsolute()) {
            System.out.println("(" + id + ")r " + base.relativize(result));
        } else {
            System.out.println("(" + id + ")  " + result);
        }
        try {
            System.out.println("RealPath: " + result.toRealPath());
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("os.name"));
        System.out.println(base);
        Path p = Paths.get("AddAndSubtractPaths.java").toAbsolutePath();
        show(1, p);
        Path convoluted = p.getParent().getParent()
                .resolve("strings")
                .resolve("..")
                .resolve(p.getParent().getFileName());
        show(2, convoluted);
        show(3, convoluted.normalize());

        Path p2 = Paths.get("..", "..");
        show(4, p2);
        show(5, p2.normalize());
        show(6, p2.toAbsolutePath().normalize());

        Path p3 = Paths.get(".").toAbsolutePath();
        Path p4 = p3.resolve(p2);
        show(7, p4);
        show(8, p4.normalize());

        Path p5 = Paths.get("").toAbsolutePath();
        show(9, p5);
        show(10, p5.resolveSibling("ch18_strings"));
        show(11, Paths.get("nonexistent"));

        Path p6 = Path.of(".\\a\\b\\..\\..\\..\\..\\").normalize();
        show(12, p6);
    }
}
/* Output:
Windows 11
E:\IdeaProjects\onjava\src
(1)r main\java\ch17_files\AddAndSubtractPaths.java
RealPath: E:\IdeaProjects\onjava\src\main\java\ch17_files\AddAndSubtractPaths.java
(2)r main\java\ch17_files
RealPath: E:\IdeaProjects\onjava\src\main\java\ch17_files
(3)r main\java\ch17_files
RealPath: E:\IdeaProjects\onjava\src\main\java\ch17_files
(4)  ..\..
RealPath: E:\IdeaProjects\onjava\src\main
(5)  ..\..
RealPath: E:\IdeaProjects\onjava\src\main
(6)r main
RealPath: E:\IdeaProjects\onjava\src\main
(7)r main
RealPath: E:\IdeaProjects\onjava\src\main
(8)r main
RealPath: E:\IdeaProjects\onjava\src\main
(9)r main\java\ch17_files
RealPath: E:\IdeaProjects\onjava\src\main\java\ch17_files
(10)r main\java\ch18_strings
RealPath: E:\IdeaProjects\onjava\src\main\java\ch18_strings
(11)  nonexistent
java.nio.file.NoSuchFileException: E:\IdeaProjects\onjava\src\main\java\ch17_files\nonexistent
(12)  ..\..
RealPath: E:\IdeaProjects\onjava\src\main
 */
