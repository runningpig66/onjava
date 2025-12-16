package ch18_strings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author runningpig66
 * @date 2025/12/16 周二
 * @time 16:46
 * P.583 §18.7 正则表达式 §18.7.8 正则表达式和 Java I/O
 * A very simple version of the "grep" program
 * {java JGrep WhitherStringBuilder.java "return|for|String"}
 * {Working directory: E:\IdeaProjects\onjava\src\main\java\ch18_strings}
 * <p>
 * 到目前为止，大多数示例将正则表达式应用于静态的字符串。下面这个示例演示了如何用正则表达式来搜索文件中的匹配项。
 * 受 UNIX 的 grep 启发，JGrep.java 接受了两个参数：文件名和用来匹配的正则表达式。输出的是每个匹配项及其在该行中的位置。
 * 这里的测试参数是 WhitherStringBuilder.java，程序打开并读取该文件作为输入，然后搜索单词 return、for 或 String。
 */
public class JGrep {
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage: java JGrep file regex");
            System.exit(0);
        }
        Pattern p = Pattern.compile(args[1]);
        // 虽然可以为每一行创建一个新的 Matcher 对象，但最好还是创建一个空的 Matcher 对象，
        // 然后在遍历时使用 reset() 方法来为 Matcher 对象加载对应的行。最后用 find() 来查找结果。
        Matcher m = p.matcher("");
        // Iterate through the lines of the input file:
        Files.readAllLines(Paths.get(args[0])).forEach(
                line -> {
                    m.reset(line);
                    while (m.find()) {
                        System.out.println(m.group() + ": " + m.start());
                    }
                }
        );
    }
}
/* Output:
String: 22
String: 20
String: 11
String: 27
String: 8
for: 8
String: 13
return: 8
String: 11
String: 27
String: 8
String: 35
for: 8
String: 13
return: 8
String: 24
 */
