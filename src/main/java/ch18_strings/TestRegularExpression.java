package ch18_strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author runningpig66
 * @date 2025/12/14 周日
 * @time 23:44
 * P.573 §18.7 正则表达式 §18.7.4 Pattern 和 Matcher
 * Simple regular expression demonstration
 * {java TestRegularExpression abcabcabcdefabc "abc+" "(abc)+" "(abc){2,}" "(abc){2,}?" }
 * <p>
 * 通常我们会编译正则表达式对象，而不是使用功能相当有限的 String。为此，只需要导入 java.util.regex 包，
 * 然后使用静态方法 Pattern.compile() 编译正则表达式即可。它会根据自己的字符串参数来生成一个 Pattern 对象。
 * 你可以通过调用 matcher() 方法来使用这个 Pattern，对传递的 String 进行搜索。
 * matcher() 方法会产生一个 Matcher 对象。它有一组可选择的操作。例如，replaceAll() 会用其参数替换所有的匹配项。
 * <p>
 * 作为第一个示例，下面的类可以针对输入字符串来测试正则表达式。第一个命令行参数是要匹配的输入字符串，
 * 然后是一个或多个应用于输入字符串的正则表达式。在 UNIX/Linux 环境下，命令行中的正则表达式必须用引号包围起来。
 * 当构建正则表达式时，可以使用该程序来测试，以查看它是否能够产生预期的匹配行为。
 * <p>
 * public static Pattern compile(String regex)
 * public Matcher matcher(CharSequence input)
 */
public class TestRegularExpression {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage:\njava TestRegularExpression characterSequence regularExpression+");
            System.exit(0);
        }
        System.out.println("Input: \"" + args[0] + "\"");
        for (String arg : args) {
            System.out.println("Regular expression: \"" + arg + "\"");
            // 将字符串形式的正则表达式编译为可重用的 Pattern 对象
            Pattern p = Pattern.compile(arg);
            // 基于 Pattern 创建 Matcher 匹配器，用于在输入字符串中查找匹配
            Matcher m = p.matcher(args[0]);
            // 循环查找所有匹配项。find() 方法会逐次返回下一个匹配结果
            while (m.find()) {
                System.out.println(
                        // m.group(): 返回前一次匹配操作（例如 find()）的第 0 个分组（即整个匹配）。
                        "Match \"" + m.group() + "\" at positions " +
                                // m.start(): 返回在前一次匹配操作中找到的分组的起始索引。
                                // m.end(): 返回在前一次匹配操作中找到的分组的最后一个字符的索引加 1 的值。
                                m.start() + "-" + (m.end() - 1)
                );
            }
        }
    }
}
/* Output:
Input: "abcabcabcdefabc"
Regular expression: "abcabcabcdefabc"
Match "abcabcabcdefabc" at positions 0-14
Regular expression: "abc+"
Match "abc" at positions 0-2
Match "abc" at positions 3-5
Match "abc" at positions 6-8
Match "abc" at positions 12-14
Regular expression: "(abc)+"
Match "abcabcabc" at positions 0-8
Match "abc" at positions 12-14
Regular expression: "(abc){2,}"
Match "abcabcabc" at positions 0-8
Regular expression: "(abc){2,}?"
Match "abcabc" at positions 0-5
 */
