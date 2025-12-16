package ch18_strings;

/**
 * @author runningpig66
 * @date 2025/12/13 周六
 * @time 14:30
 * P.566 §18.7 正则表达式 §18.7.1 基础
 * <p>
 * 为了显示普通字符串反斜杠和正则表达式反斜杠之间的区别，我们将使用简单的 String.matches() 函数。
 * matches() 的参数是一个正则表达式，它会作用于调用 matches() 的字符串。
 * 我们首先定义普通的字符串反斜杠 one、two 和 three。可以看到在一个普通的字符串中你需要两个反斜杠来生成一个反斜杠：
 * 而在正则表达式中，我们需要使用四个反斜杠才能与单个反斜杠匹配。
 * 因此，要匹配字符串中的 3 个反斜杠，我们需要在正则表达式中使用 12 个反斜杠。
 * public boolean matches(String regex)
 * <p>
 * notes: BackSlashes.md
 * Java 源码字面量 →（编译/运行得到）运行时 String →（传参）正则解析/匹配
 */
public class BackSlashes {
    public static void main(String[] args) {
        String one = "\\";
        String two = "\\\\";
        String three = "\\\\\\";
        System.out.println(one);
        System.out.println(two);
        System.out.println(three);
        System.out.println(one.matches("\\\\"));
        System.out.println(two.matches("\\\\\\\\"));
        System.out.println(three.matches("\\\\\\\\\\\\"));
    }
}
/* Output:
\
\\
\\\
true
true
true
 */
