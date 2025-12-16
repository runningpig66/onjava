package ch18_strings;

import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/12/13 周六
 * @time 15:00
 * P.567 §18.7 正则表达式 §18.7.1 基础
 * <p>
 * 正则表达式用通用术语来描述字符串，因此你可以这样说：“如果字符串中包含这些内容，那么它就符合我的搜索条件。”
 * 例如，要表示一个数前面可能有也可能没有减号，可以在减号后面加上一个问号：-?。
 * <p>
 * 如果要在表达式里表示“前面有一个或多个”，请使用 +。
 * 因此，如果想说“前面可能有一个减号，后面跟着一个或多个数字”，对应表达式是这样的：-?\\d+。
 * <p>
 * 使用正则表达式的最简单方式，就是直接使用内嵌在 String 类中的功能。例如，我们可以查看一个 String 是否与上面的这个正则表达式匹配：
 * public boolean matches(String regex)
 */
public class IntegerMatch {
    public static void main(String[] args) {
        String possiblyMinus = "-?\\d+";
        Stream.of(
                "-1234".matches(possiblyMinus),
                "5678".matches(possiblyMinus),
                "+911".matches(possiblyMinus),
                // 前两个字符串匹配，但第三个字符串以 + 开头，这是一个合法的符号。但这样一来，字符串里的数字就与正则表达式不匹配了。
                // 所以我们需要一种方式来表达“可以以 + 或 - 开头”。在正则表达式中，括号可以将表达式分组，竖线 | 表示“或”操作。
                // 因此有：(-|\\+)?。这个正则表达式表示对应部分的字符串可以是 -、+ 或什么都没有（这是因为后面跟着 ?）。
                // + 字符在正则表达式中具有特殊意义，所以在表达式中必须用 \\ 转义成普通字符。
                "+911".matches("(-|\\+)?\\d+"),
                "+911".matches("([-+])?\\d+")
        ).forEach(System.out::println);
    }
}
/* Output:
true
true
false
true
true
 */
