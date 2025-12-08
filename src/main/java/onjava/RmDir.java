package onjava;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author runningpig66
 * @date 2025/12/8 周一
 * @time 11:24
 * P.534 §17.2 目录
 * <p>
 * Files 工具类包含了操作目录和文件所需的大部分操作。
 * 然而由于某些原因，其中并没有包括用于删除目录树的工具，所以我们会创建一个，并将其添加到 onjava 库中。
 *
 * <pre>
 * FileVisitor 接口 - 用于递归遍历文件树的回调接口。
 * T: 通常是 Path 类型，表示当前访问的文件或目录。使用 Files.walkFileTree() 方法时传入此接口的实现。
 * public static Path walkFileTree(Path start, FileVisitor<? super Path> visitor)
 *
 * FileVisitResult 枚举值说明：
 * CONTINUE：继续文件树的遍历
 * TERMINATE：立即终止文件树的遍历
 * SKIP_SUBTREE：跳过当前目录的子项（仅在 preVisitDirectory 中有效）
 * SKIP_SIBLINGS：跳过当前目录/文件的所有同级项
 *
 * public interface FileVisitor<T> {
 *     FileVisitResult preVisitDirectory(T dir, BasicFileAttributes attrs) throws IOException;
 *     FileVisitResult visitFile(T file, BasicFileAttributes attrs) throws IOException;
 *     FileVisitResult visitFileFailed(T file, IOException exc) throws IOException;
 *     FileVisitResult postVisitDirectory(T dir, IOException exc) throws IOException;
 * }
 * 注意：pre - 进入前，post - 离开后；每个方法都可以控制遍历流程（通过返回值）；异常通过参数传递，不会抛出中断遍历；
 * SimpleFileVisitor 提供了默认实现，通常继承它然后重写需要的方法。
 * </pre>
 */
public class RmDir {
    public static void rmdir(Path dir) throws IOException {
        Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
            @NotNull
            @Override
            public FileVisitResult visitFile(@NotNull Path file, @NotNull BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @NotNull
            @Override
            public FileVisitResult postVisitDirectory(@NotNull Path dir, @Nullable IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static void printDir(Path dir) throws IOException {
        Files.walkFileTree(dir, new SimpleFileVisitor<>() {
            /**
             * 在进入目录之前调用
             * @param dir 当前目录的 Path 对象
             * @param attrs 当前目录的基本属性
             * @return CONTINUE: 继续遍历 | SKIP_SUBTREE: 跳过此目录及其子目录 | SKIP_SIBLINGS: 跳过同级目录
             * <p>
             * 典型用途：目录权限检查、创建日志、统计信息
             * 注意：如果返回 SKIP_SUBTREE，则此目录的子项不会被访问，也不会调用 postVisitDirectory
             */
            @NotNull
            @Override
            public FileVisitResult preVisitDirectory(@NotNull Path dir, @NotNull BasicFileAttributes attrs) throws IOException {
                System.out.println("preVisitDirectory: " + dir);
                return FileVisitResult.CONTINUE;
            }

            /**
             * 访问文件时调用
             * @param file 当前文件的 Path 对象
             * @param attrs 当前文件的基本属性
             * @return CONTINUE: 继续遍历 | SKIP_SIBLINGS: 跳过同级文件 | TERMINATE: 终止遍历
             * <p>
             * 典型用途：文件内容检查、复制/移动文件、统计文件信息
             * 注意：只对文件调用，不对目录调用
             */
            @NotNull
            @Override
            public FileVisitResult visitFile(@NotNull Path file, @NotNull BasicFileAttributes attrs) throws IOException {
                System.out.println("visitFile: " + file);
                return super.visitFile(file, attrs);
            }

            /**
             * 访问文件失败时调用（如权限不足、文件损坏）
             * @param file 访问失败的文件 Path 对象
             * @param exc 导致访问失败的 IOException 异常
             * @return CONTINUE: 继续遍历 | SKIP_SIBLINGS: 跳过同级 | TERMINATE: 终止遍历
             * <p>
             * 典型用途：错误处理、记录失败日志、权限问题处理
             * 注意：如果不处理异常，遍历可能会提前终止
             */
            @NotNull
            @Override
            public FileVisitResult visitFileFailed(@NotNull Path file, @NotNull IOException exc) throws IOException {
                System.out.println("visitFileFailed: " + file);
                return FileVisitResult.CONTINUE;
            }

            /**
             * 在离开目录之后调用（所有子项访问完成后）
             * @param dir 当前目录的 Path 对象
             * @param exc 如果目录迭代过程中发生异常，则为该异常；否则为 null
             * @return CONTINUE: 继续遍历 | SKIP_SIBLINGS: 跳过同级目录 | TERMINATE: 终止遍历
             * <p>
             * 典型用途：清理资源、目录修改完成后的操作、统计目录信息
             * 注意：即使目录内发生异常，该方法也会被调用（异常通过参数传递）
             */
            @NotNull
            @Override
            public FileVisitResult postVisitDirectory(@NotNull Path dir, @Nullable IOException exc) throws IOException {
                System.out.println("postVisitDirectory: " + dir);
                return FileVisitResult.CONTINUE;

            }
        });
    }

    public static void main(String[] args) throws IOException {
        // E:\IdeaProjects\onjava\src\main\java\ch17_files
        Path path = Paths.get("src\\main\\java\\ch17_files");
        System.out.println(path);
        RmDir.printDir(path);
        System.out.println();

        Path normalizedPath = path.toAbsolutePath().normalize();
        System.out.println(normalizedPath);
        RmDir.printDir(normalizedPath);
    }
}
/* 目录结构
 * src
 * └── main
 *     └── java
 *         └── ch17_files
 *             ├── AddAndSubtractPaths.java
 *             ├── PartsOfPaths.java
 *             ├── PathAnalysis.java
 *             ├── PathInfo.java
 *             └── notes
 *                 ├── AddAndSubtractPaths.md
 *                 └── AddAndSubtractPaths2.md
 */
/* Output:
src\main\java\ch17_files
preVisitDirectory: src\main\java\ch17_files
visitFile: src\main\java\ch17_files\AddAndSubtractPaths.java
preVisitDirectory: src\main\java\ch17_files\notes
visitFile: src\main\java\ch17_files\notes\AddAndSubtractPaths.md
visitFile: src\main\java\ch17_files\notes\AddAndSubtractPaths2.md
postVisitDirectory: src\main\java\ch17_files\notes
visitFile: src\main\java\ch17_files\PartsOfPaths.java
visitFile: src\main\java\ch17_files\PathAnalysis.java
visitFile: src\main\java\ch17_files\PathInfo.java
postVisitDirectory: src\main\java\ch17_files

E:\IdeaProjects\onjava\src\main\java\ch17_files
preVisitDirectory: E:\IdeaProjects\onjava\src\main\java\ch17_files
visitFile: E:\IdeaProjects\onjava\src\main\java\ch17_files\AddAndSubtractPaths.java
preVisitDirectory: E:\IdeaProjects\onjava\src\main\java\ch17_files\notes
visitFile: E:\IdeaProjects\onjava\src\main\java\ch17_files\notes\AddAndSubtractPaths.md
visitFile: E:\IdeaProjects\onjava\src\main\java\ch17_files\notes\AddAndSubtractPaths2.md
postVisitDirectory: E:\IdeaProjects\onjava\src\main\java\ch17_files\notes
visitFile: E:\IdeaProjects\onjava\src\main\java\ch17_files\PartsOfPaths.java
visitFile: E:\IdeaProjects\onjava\src\main\java\ch17_files\PathAnalysis.java
visitFile: E:\IdeaProjects\onjava\src\main\java\ch17_files\PathInfo.java
postVisitDirectory: E:\IdeaProjects\onjava\src\main\java\ch17_files
 */
