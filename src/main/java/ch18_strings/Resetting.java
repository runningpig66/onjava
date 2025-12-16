package ch18_strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author runningpig66
 * @date 2025/12/16 周二
 * @time 16:37
 * P.582 §18.7 正则表达式 §18.7.7 reset()
 * <p>
 * 可以使用 reset() 方法将现有的 Matcher 对象应用于新的字符序列：
 * 没有任何参数的 reset() 会将 Matcher 对象设置到当前序列的起始位置。
 * public Matcher reset()
 * public Matcher reset(CharSequence input)
 */
public class Resetting {
    public static void main(String[] args) {
        // 编译正则表达式：匹配一个包含 f/r/b 之一 + a/i/u 之一 + g/x 之一的三个字母单词
        Matcher m = Pattern.compile("[frb][aiu][gx]")
                .matcher("fix the rug with bags");
        while (m.find()) {
            System.out.print(m.group() + " ");
        }
        System.out.println();
        // 使用 Matcher reset(CharSequence input) 方法：将匹配器的输入序列重置为新的字符串
        // 注意：这里复用同一个 Matcher 对象，避免重新编译 Pattern，提高效率
        m.reset("fix the rig with rags");
        while (m.find()) {
            System.out.print(m.group() + " ");
        }
    }
}
/* Output:
fix rug bag
fix rig rag
 */
