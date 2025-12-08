package ch17_files;

import onjava.RmDir;

import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/12/8 周一
 * @time 14:33
 * P.535 §17.2 目录
 * {Working directory: E:\IdeaProjects\onjava\src\main\java\ch17_files}
 * <p>
 * 在下面的示例中，makeVariant() 接受了一个基准目录 test, 然后通过旋转 parts 列表生成了不同的子目录路径。
 * 使用 String.join() 将旋转后的 parts 通过路径分隔符 sep 连接到一起，然后将结果作为 Path 返回。
 * <p>
 * 为了显示结果，我们首先尝试了 newDirectoryStream(), 它似乎有希望，但结果是流中只有 test 目录下的内容，
 * 而没有进人更下层的目录。要获得包含整个目录树内容的流，请使用 Files.walk().
 * <p>
 * public static void rotate(List<?> list, int distance)
 * public static String join(CharSequence delimiter, Iterable<? extends CharSequence> elements)
 * public static DirectoryStream<Path> newDirectoryStream(Path dir) throws IOException
 * public static Stream<Path> walk(Path start, int maxDepth, FileVisitOption... options) throws IOException
 */
public class Directories {
    static Path test = Paths.get("test");
    static String sep = FileSystems.getDefault().getSeparator();
    static List<String> parts = Arrays.asList("foo", "bar", "baz", "bag");

    // 在工作目录 + "test" 这个 Path 的基础上，后续循环 parts 列表拼接出一个路径，每次调用返回的路径不同。
    // 初次返回的绝对路径：E:\IdeaProjects\onjava\src\main\java\ch17_files\test\bag\foo\bar\baz
    static Path makeVariant() {
        // 将列表中的元素进行循环移位；
        // distance 为正数：元素向右移动（如 rotate(list, 1)）。
        // distance 为负数：元素向左移动（如 rotate(list, -1)）。
        // public static void rotate(List<?> list, int distance)
        Collections.rotate(parts, 1);
        // 将 {@code Iterable} 集合中的多个 {@code CharSequence} 元素，
        // 用指定的分隔符 {@code delimiter} 连接成单个字符串。
        // 这是处理集合转字符串的便捷方法，避免了手动循环拼接。
        // public static String join(CharSequence delimiter, Iterable<? extends CharSequence> elements)
        return Paths.get("test", String.join(sep, parts));
    }

    // 效果：清空 test 目录。test 目录的绝对路径：E:\IdeaProjects\onjava\src\main\java\ch17_files\test
    static void refreshTestDir() throws IOException {
        if (Files.exists(test)) {
            RmDir.rmdir(test);
        }
        if (!Files.exists(test)) {
            Files.createDirectories(test);
        }
    }

    public static void main(String[] args) throws Exception {
        refreshTestDir();
        // 创建：E:\IdeaProjects\onjava\src\main\java\ch17_files\test\Hello.txt
        Files.createFile(test.resolve("Hello.txt"));
        // variant 拼接结果：E:\IdeaProjects\onjava\src\main\java\ch17_files\test\bag\foo\bar\baz
        Path variant = makeVariant();
        // Throws exception (too many levels):
        try {
            Files.createDirectory(variant);
        } catch (Exception e) {
            // java.nio.file.NoSuchFileException: test\bag\foo\bar\baz
            System.out.println("Nope, that doesn't work.");
        }
        populateTestDir();
        Path tempDir = Files.createTempDirectory(test, "DIR_");
        Files.createTempFile(tempDir, "pre", ".non");
        /*
         * 打开一个目录，并返回一个 {@link DirectoryStream} 以遍历该目录中的所有条目（文件和子目录）。
         * 这是一个针对目录遍历优化的、**非递归**的轻量级方法。
         * 返回的流是资源敏感的，**必须**在使用后正确关闭（强烈建议使用 try-with-resources）。
         *
         * @param   dir 要打开的目录的路径。
         *
         * @return  一个新的、打开的 {@code DirectoryStream} 对象。迭代器返回的元素类型为 {@link Path}，
         *          每个 {@code Path} 都是相对于参数 {@code dir} 的名称（即非绝对路径）。
         *
         * @throws  NotDirectoryException 如果文件存在但不是目录（或其他文件系统特定原因）。
         * @throws  IOException 如果发生 I/O 错误（例如目录不存在或无法访问）。
         * @throws  SecurityException 在安全管理器存在且其 {@link SecurityManager#checkRead(String)} 方法拒绝读取目录访问时。
         *
         * @apiNote
         * <strong>核心特性与用法：</strong>
         * 1. <strong>轻量非递归</strong>：仅遍历单层目录，不进入子目录，性能优于 {@link Files#walkFileTree}。
         * 2. <strong>资源管理</strong>：返回的流实现了 {@link AutoCloseable}，必须关闭以防资源泄漏。
         *    <pre>{@code
         *    // ✅ 正确用法：使用 try-with-resources 确保关闭
         *    try (DirectoryStream<Path> entries = Files.newDirectoryStream(Paths.get("/tmp"))) {
         *        for (Path entry : entries) {
         *            System.out.println(entry.getFileName());
         *        }
         *    } // 流在此处自动关闭
         *    }</pre>
         * 3. <strong>过滤支持</strong>：通过重载方法可以使用 {@link DirectoryStream.Filter} 进行 glob 或自定义过滤。
         *    <pre>{@code
         *    // 使用 Glob 模式过滤出 .txt 文件
         *    try (DirectoryStream<Path> txtFiles =
         *         Files.newDirectoryStream(dir, "*.txt")) { ... }
         *
         *    // 使用自定义过滤器（例如，仅显示目录）
         *    DirectoryStream.Filter<Path> filter = path -> Files.isDirectory(path);
         *    try (DirectoryStream<Path> dirs =
         *         Files.newDirectoryStream(dir, filter)) { ... }
         *    }</pre>
         * 4. <strong>返回路径</strong>：迭代返回的 {@code Path} 是目录条目的名称，它们是相对于给定目录的。
         *    例如，对于目录 "/tmp"，其子文件 "a.txt" 将返回为 {@code Path} 对象 "a.txt"（而不是 "/tmp/a.txt"）。
         *    可使用 {@code dir.resolve(entry)} 获取绝对路径。
         *
         * @implNote
         * 此方法在大多数实现中是“及时”的，即在迭代时从文件系统读取条目，而非一次性全部加载到内存，
         * 这使其在处理大型目录时表现良好。返回的迭代器不支持 {@link Iterator#remove} 操作。
         *
         * @see DirectoryStream
         * @see Files#list(Path)  (Java 8+, 返回更通用的 {@link java.util.stream.Stream})
         * @see Files#walkFileTree  (用于递归遍历或需要更多控制的情形)
         * @since 1.7
         * public static DirectoryStream<Path> newDirectoryStream(Path dir) throws IOException
         */
        try (DirectoryStream<Path> paths = Files.newDirectoryStream(test)) {
            paths.forEach(System.out::println);
        }
        System.out.println("*********");
        /*
         * 返回一个 {@link Stream}，通过深度优先遍历从给定的起始 {@code Path} 开始递归访问文件树。
         * 该流会“惰性”地遍历目录，仅在需要时获取下一个元素，适合处理大型目录结构。
         * <p>
         * <strong>⚠️ 重要：</strong>返回的流包装了一个或多个打开的文件流（例如 {@link DirectoryStream}）。
         * 为确保资源被释放，<strong>必须</strong>使用 try-with-resources 语句或显式调用 {@link Stream#close()} 来关闭流。
         * 不关闭流可能导致资源泄漏。
         *
         * @param start     起始目录的路径。
         * @param maxDepth  要访问的最大目录层级深度。
         *                  {@code 0} 表示仅访问起始文件/目录本身。
         *                  {@link Integer#MAX_VALUE} 可用于表示“无深度限制”。
         * @param options   遍历选项（可变参数）。
         *                  {@link FileVisitOption#FOLLOW_LINKS}：跟随符号链接（注意可能遇到循环链接）。
         *                  不传任何参数时，默认不跟随符号链接。
         *
         * @return 一个包含从起始路径开始的、所有文件和目录的 {@link Stream}{@code <Path>}。
         *         流中的元素顺序是“深度优先”的，但同一目录内的具体顺序取决于底层文件系统实现。
         *
         * @throws IllegalArgumentException 如果 {@code maxDepth} 为负数。
         * @throws SecurityException        如果安全管理器拒绝访问起始文件或其子文件。
         * @throws IOException              <strong>重要：</strong>如果在遍历过程中发生 I/O 错误，该方法可能会在打开流时立即抛出异常。
         *                                  但更常见的是，某些 I/O 错误（如无法访问某个子目录）会作为 {@link IOException} 嵌入在流的后续操作中抛出（例如在终端操作中抛出 {@link UncheckedIOException}）。
         *
         * @apiNote
         * <strong>典型用法示例：</strong>
         * <pre>{@code
         * // ✅ 正确用法：使用 try-with-resources 确保流被关闭
         * try (Stream<Path> pathStream = Files.walk(Paths.get("/project"), 3)) {
         *     pathStream
         *         .filter(Files::isRegularFile) // 过滤出普通文件
         *         .map(Path::toString)
         *         .filter(s -> s.endsWith(".java"))
         *         .forEach(System.out::println);
         * }
         *
         * // 简单的单行遍历打印（注意：此用法未显式关闭流，仅推荐在简单工具或流生命周期短暂时使用）
         * Files.walk(test).forEach(System.out::println); // 可能存在资源泄漏风险，不推荐在生产代码中直接使用
         * }</pre>
         *
         * <strong>核心特性与比较：</strong>
         * 1. <strong>递归遍历</strong>：与 {@link Files#newDirectoryStream}（单层）和 {@link Files#list}（单层）不同，此方法递归遍历整个目录树。
         * 2. <strong>返回流 (Stream)</strong>：与 {@link Files#walkFileTree}（使用 {@link FileVisitor} 回调）相比，此方法提供更现代的、函数式的流式处理能力。
         * 3. <strong>深度控制</strong>：通过 {@code maxDepth} 参数可以灵活控制遍历深度。
         * 4. <strong>惰性遍历</strong>：流的元素是惰性获取的，这使其在处理非常深的目录树或大量文件时内存效率更高。
         * 5. <strong>遍历顺序</strong>：深度优先。注意，流中可能同时包含文件和目录。
         *
         * @see Files#walkFileTree  (当需要更精细的控制，如在遍历前/后处理目录，或需要处理访问失败时)
         * @see Files#list  (仅列出单层目录内容，不递归)
         * @since 1.8
         * public static Stream<Path> walk(Path start, int maxDepth, FileVisitOption... options) throws IOException
         */
        try (Stream<Path> pathStream = Files.walk(test)) {
            pathStream.forEach(System.out::println);
        }
    }

    // 填充测试目录数据
    static void populateTestDir() throws IOException {
        for (int i = 0; i < parts.size(); i++) {
            Path variant = makeVariant();
            if (!Files.exists(variant)) {
                Files.createDirectories(variant);
                Files.copy(Paths.get("Directories.java"), variant.resolve("File.txt"));
                // 在指定的目录 {@code dir} 中创建一个新的空文件。
                // 文件名将使用给定的前缀、后缀和系统生成的随机部分自动生成，确保唯一性。
                // 此文件通常用作临时存储，程序退出时应（最好显式）删除。
                // public static Path createTempFile(Path dir, String prefix, String suffix, FileAttribute<?>... attrs)
                Files.createTempFile(variant, null, null);
            }
        }
    }
}
/* Output:
Nope, that doesn't work.
test\bag
test\bar
test\baz
test\DIR_1321988633188423092
test\foo
test\Hello.txt
*********
test
test\bag
test\bag\foo
test\bag\foo\bar
test\bag\foo\bar\baz
test\bag\foo\bar\baz\752057864474749062.tmp
test\bag\foo\bar\baz\File.txt
test\bar
test\bar\baz
test\bar\baz\bag
test\bar\baz\bag\foo
test\bar\baz\bag\foo\3913037979926907.tmp
test\bar\baz\bag\foo\File.txt
test\baz
test\baz\bag
test\baz\bag\foo
test\baz\bag\foo\bar
test\baz\bag\foo\bar\5642782423372563061.tmp
test\baz\bag\foo\bar\File.txt
test\DIR_1321988633188423092
test\DIR_1321988633188423092\pre15033458538872609800.non
test\foo
test\foo\bar
test\foo\bar\baz
test\foo\bar\baz\bag
test\foo\bar\baz\bag\16324891819459017723.tmp
test\foo\bar\baz\bag\File.txt
test\Hello.txt
 */
