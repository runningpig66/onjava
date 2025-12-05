package ch17_files;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author runningpig66
 * @date 2025/12/5 周五
 * @time 15:43
 * P.532 §17.1 文件和目录路径 §17.1.3 添加或删除路径片段
 * <p>
 * notes: AddAndSubtractPaths.md 的练习测试
 * AddAndSubtractPaths.java
 */
public class AddAndSubtractPathsMDTest {

    @Test
    public void testPathOperations() {
        // 1. `Paths.get()` / `Path.of()` (Java 11+)
        Path path = Paths.get("a", "b", "c");
        assertEquals("a\\b\\c", path.toString());

        Path path1 = Path.of("a\\b\\c");
        assertEquals(path1, path);

        // 注意第一个参数决定路径性质
        // 相对路径，因为第一个是".."
        Path path2 = Paths.get("..", "test");
        assertEquals("..\\test", path2.toString());
        assertFalse(path2.isAbsolute());

        // 绝对路径，因为第一个是"E:"
        Path path3 = Paths.get("E:", "..", "test");
        assertEquals("E:\\..\\test", path3.toString());
        assertTrue(path3.isAbsolute());


        Path e1 = Paths.get("E");
        assertEquals("E", e1.toString());
        assertFalse(e1.isAbsolute());

        Path e2 = Paths.get("E:");
        assertEquals("E:", e2.toString());
        assertFalse(e2.isAbsolute());

        Path e3 = Paths.get("E:/");
        assertEquals("E:\\", e3.toString());
        assertTrue(e3.isAbsolute());

        Path e4 = Paths.get("E:\\");
        assertEquals("E:\\", e4.toString());
        assertTrue(e4.isAbsolute());


        // 2. `toAbsolutePath()`
        // 工作目录：E:\IdeaProjects\onjava
        Path path4 = Paths.get("..", "config").toAbsolutePath();
        // 注意：结果包含".."，可能会影响后续操作
        assertEquals("E:\\IdeaProjects\\onjava\\..\\config", path4.toString());

        // 3. `normalize()`
        Path path5 = Paths.get("a/./b/../c").normalize();
        assertEquals("a\\c", path5.toString());

        Path path6 = Paths.get("..").normalize();
        assertEquals("..", path6.toString());

        Path path7 = Paths.get("../a/../b").normalize();
        assertEquals("..\\b", path7.toString());

        Path path8 = Paths.get("a/../../b").normalize();
        assertEquals("..\\b", path8.toString());

        // 4. `relativize(Path other)`
        // 必须同为绝对或相对，本例 base 和 target 同为绝对路径
        Path base = Paths.get("/a/b");
        Path target = Paths.get("/a/b/c/d");
        Path path9 = base.relativize(target);
        assertEquals("c\\d", path9.toString());

        // 必须同为绝对或相对，本例 base2 和 target2 同为相对路径
        Path base2 = Paths.get("a/b");
        Path target2 = Paths.get("a/b/c/d");
        Path path10 = base2.relativize(target2);
        assertEquals("c\\d", path10.toString());

        // 错误示例：不符合条件 -> 必须同为绝对或相对
        Path absolute = Paths.get("/a/b");
        Path relative = Paths.get("c/d");
        assertThrows(IllegalArgumentException.class, () -> absolute.relativize(relative));

        // 5. `toRealPath(LinkOption... options)`
        // 路径不存在或无法访问，必须捕获IOException
        Path nonexistent = Paths.get("nonexistent");
        assertThrows(IOException.class, nonexistent::toRealPath);

        // 与normalize()的区别（工作路径：E:\IdeaProjects\onjava）
        Path path11 = Paths.get("..\\..").normalize();
        try {
            Path path12 = Paths.get("..\\..").toRealPath();
            assertEquals("..\\..", path11.toString());
            assertEquals("E:\\", path12.toString());
        } catch (IOException e) {
            // 路径不存在或无法访问，必须捕获IOException
            throw new RuntimeException(e);
        }

        // 6. `resolve(Path other)`
        Path base3 = Paths.get("/a/b");
        Path path12 = base3.resolve("c/d");
        assertEquals("\\a\\b\\c\\d", path12.toString());
        Path path13 = base3.resolve("/c/d");
        assertEquals("\\c\\d", path13.toString());
        Path path14 = base3.resolve("../d");
        assertEquals("\\a\\b\\..\\d", path14.toString());
        Path path15 = base3.resolveSibling("other");
        assertEquals("\\a\\other", path15.toString());

        // 7. `resolveSibling(Path other)`
        Path file = Paths.get("\\a\\b\\c.txt");
        Path path16 = file.resolveSibling("d.txt");
        assertEquals("\\a\\b\\d.txt", path16.toString());
        Path path17 = file.resolveSibling("..\\d.txt");
        assertEquals("\\a\\b\\..\\d.txt", path17.toString());

        // 无父目录的情况
        Path root = Paths.get("file.txt");
        Path path18 = root.resolveSibling("other.txt");
        assertEquals("other.txt", path18.toString());

        // 路径字符串末尾的 \, 只是人为表示「目录」的习惯写法，不属于 Path 的结构信息
        Path p1 = Paths.get("E:\\IdeaProjects\\onjava\\src\\main");
        Path p2 = Paths.get("E:\\IdeaProjects\\onjava\\src\\main\\");
        assertEquals(p1, p2);
        assertEquals("E:\\IdeaProjects\\onjava\\src\\main", p1.toString());
        assertEquals("E:\\IdeaProjects\\onjava\\src\\main", p2.toString());
    }
}
