package ch17_files;

import java.io.IOException;
import java.nio.file.*;

/**
 * @author runningpig66
 * @date 2025/12/10 周三
 * @time 16:31
 * P.541 §17.5 查找文件
 * {Working directory: E:\IdeaProjects\onjava\src\main\java\ch17_files}
 * <p>
 * 到目前为止，要查找文件的话，我们一直在使用相当笨拙的方法，即在 Path 上调用 toString()，
 * 然后使用 String 的各种操作来看查看结果。其实 java.nio.file 有一个更好的解决方案：PathMatcher。
 * 可以通过在 FileSystem 对象上调用 getPathMatcher() 来获得一个 PathMatcher，并传入我们感兴趣的模式。
 * 模式有两个选项：glob 和 regex。glob 更简单，但实际上非常强大，可以解决很多问题。
 * 如果问题更为复杂，可以使用 regex，下一章会解释。这里使用 glob 来查找所有文件名以 .tmp 或 .txt 结尾的 Path。
 */
public class Find {
    public static void main(String[] args) throws IOException {
        Path test = Paths.get("test");
        Directories.refreshTestDir();
        Directories.populateTestDir();
        // Creating a *directory*, not a file:
        Files.createDirectory(test.resolve("dir.tmp"));

        // 在 matcher 中，glob 表达式开头的 **/ 表示“所有子目录”，如果你想匹配的不仅仅是以基准目录为结尾的 Path，
        // 那么它是必不可少的，因为它匹配的是完整路径，直到找到你想要的结果。单个的 * 代表的是“任何东西”，
        // 然后是一个英文句点，再后面的花括号表示的是一系列的可能性————我们正在查找任何以 .tmp 或 .txt 结尾的东西。
        // 我们可以在 getPathMatcher() 的文档中找到更多细节。
        PathMatcher matcher = FileSystems.getDefault()
                .getPathMatcher("glob:**/*.{tmp,txt}");
        Files.walk(test)
                .filter(matcher::matches)
                .forEach(System.out::println);
        System.out.println("***************");

        // matcher2 只是使用了 *.tmp，通常不会匹配到任何东西，但添加 map() 操作后会将完整路径减少到只剩最后的名字。
        PathMatcher matcher2 = FileSystems.getDefault()
                .getPathMatcher("glob:*.tmp");
        Files.walk(test)
                .map(Path::getFileName)
                .filter(matcher2::matches)
                .forEach(System.out::println);
        System.out.println("***************");

        // 注意在这两种情况下，dir.tmp 都出现在了输出中。尽管它是目录而非文件。
        // 如果只想寻找文件，必须像最后的 Files.walk() 那样对它们进行过滤。
        Files.walk(test) // Only look for files
                .filter(Files::isRegularFile)
                .map(Path::getFileName)
                .filter(matcher2::matches)
                .forEach(System.out::println);
    }
}
/* Output:
test\bag\foo\bar\baz\1321061743199452688.tmp
test\bag\foo\bar\baz\File.txt
test\bar\baz\bag\foo\11603977514048298207.tmp
test\bar\baz\bag\foo\File.txt
test\baz\bag\foo\bar\11363710122536915798.tmp
test\baz\bag\foo\bar\File.txt
test\dir.tmp
test\foo\bar\baz\bag\17603367882525409760.tmp
test\foo\bar\baz\bag\File.txt
***************
1321061743199452688.tmp
11603977514048298207.tmp
11363710122536915798.tmp
dir.tmp
17603367882525409760.tmp
***************
1321061743199452688.tmp
11603977514048298207.tmp
11363710122536915798.tmp
17603367882525409760.tmp
 */
