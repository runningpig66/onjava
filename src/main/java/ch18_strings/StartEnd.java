package ch18_strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author runningpig66
 * @date 2025/12/15 周一
 * @time 16:55
 * P.576 §18.7 正则表达式 §18.7.4 Pattern 和 Matcher - 3. start() 和 end()
 * <p>
 * 在匹配成功之后，start() 返回本次匹配结果的起始索引，而把本次匹配结果最后一个字符的索引加 1，就是 end() 的返回值。
 * 如果匹配不成功（或在尝试匹配操作之前），这时调用 start() 或 end() 会产生一个 IllegalStateException。
 * 下面的示例同时展示了 matches() 和 lookingAt() 的用法：
 * <p>
 * 如果模式能匹配整个输入字符串，则 matches() 方法返回匹配成功；
 * 如果输入字符串的起始部分与模式匹配，则 lookingAt() 方法返回匹配成功。
 * <p>
 * find() 会在输入字符串中的任何位置匹配正则表达式。
 * 但是对 lookingAt() 和 matches() 来说，只有正则表达式和输入字符串的开始位置匹配时它们才会成功。
 * matches() 仅在整个输入字符串都与正则表达式匹配时才会成功，而 lookingAt() 则仅在输入字符串的开始部分匹配时才成功。
 * <p>
 * public boolean matches()
 * public boolean lookingAt()
 */
public class StartEnd {
    public static String input =
            "As long as there is injustice, whenever a\n" +
                    "Targathian baby cries out, wherever a distress\n" +
                    "signal sounds among the stars " +
                    "... We'll be there.\n" +
                    "This fine ship, and this fine crew ...\n" +
                    "Never give up! Never surrender!";

    private static class Display {
        private boolean regexPrinted = false;
        private String regex;

        Display(String regex) {
            this.regex = regex;
        }

        void display(String message) {
            if (!regexPrinted) {
                System.out.println(regex);
                regexPrinted = true;
            }
            System.out.println(message);
        }
    }

    static void examine(String s, String regex) {
        Display d = new Display(regex);
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(s);
        // find(): 在 s 中“向后扫描”找下一个匹配子串（非重叠）；可能找到多个
        while (m.find()) {
            // group() 是本次匹配到的子串；start/end 是本次匹配的区间（end 为开区间）
            d.display("find() '" + m.group() + "' start = " + m.start() + " end = " + m.end());
        }
        // lookingAt(): 只尝试从索引 0 开始匹配一个“前缀”，不要求覆盖整个字符串
        // 注意：即使前面 find() 扫描到了末尾，这里也仍可工作（内部会从开头尝试“头部匹配”）
        if (m.lookingAt()) { // No reset() necessary
            d.display("lookingAt() start = " + m.start() + " end = " + m.end());
        }
        // matches(): 要求“整个字符串”完全匹配 regex（等价于隐式加了 ^ 和 $）
        // 同样：这里也会从头进行整体匹配判断（不依赖 find() 之后的当前位置）
        if (m.matches()) { // No reset() necessary
            d.display("matches() start = " + m.start() + " end = " + m.end());
        }
    }

    public static void main(String[] args) {
        for (String in : input.split("\n")) {
            System.out.println("input : " + in);
            // "\\w*ere\\w*" : 匹配“包含 ere 的单词”（\w* + "ere" + \w*，可在单词两侧有任意字母/数字/下划线）
            // "\\w*ever"    : 匹配“以 ever 结尾的单词/片段”（前面可有任意 \w，末尾必须是 ever）
            // "T\\w+"       : 匹配以大写 T 开头、后面跟 1 个或多个 \w 的单词（如 Targathian、This）
            // "Never.*?!"   : 匹配以 Never 开头，后面跟任意字符（.*）采用勉强/最短匹配，直到遇到第一个 '!' 为止
            for (String regex : new String[]{"\\w*ere\\w*", "\\w*ever", "T\\w+", "Never.*?!"}) {
                examine(in, regex);
            }
        }
    }
}
/* Output:
input : As long as there is injustice, whenever a
\w*ere\w*
find() 'there' start = 11 end = 16
\w*ever
find() 'whenever' start = 31 end = 39
input : Targathian baby cries out, wherever a distress
\w*ere\w*
find() 'wherever' start = 27 end = 35
\w*ever
find() 'wherever' start = 27 end = 35
T\w+
find() 'Targathian' start = 0 end = 10
lookingAt() start = 0 end = 10
input : signal sounds among the stars ... We'll be there.
\w*ere\w*
find() 'there' start = 43 end = 48
input : This fine ship, and this fine crew ...
T\w+
find() 'This' start = 0 end = 4
lookingAt() start = 0 end = 4
input : Never give up! Never surrender!
\w*ever
find() 'Never' start = 0 end = 5
find() 'Never' start = 15 end = 20
lookingAt() start = 0 end = 5
Never.*?!
find() 'Never give up!' start = 0 end = 14
find() 'Never surrender!' start = 15 end = 31
lookingAt() start = 0 end = 14
matches() start = 0 end = 31
 */
