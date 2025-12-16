package ch18_strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author runningpig66
 * @date 2025/12/15 周一
 * @time 1:34
 * P.574 §18.7 正则表达式 §18.7.4 Pattern 和 Matcher - 1. find()
 * <p>
 * Matcher.find() 可以在应用它的 CharSequence 中查找多个匹配。
 * <p>
 * 模式 \\w+ 会将输入的字符串拆分为单词。find() 就像一个迭代器，会向前遍历输入的字符串。
 * 而另一个版本的 find() 可以接收一个整数参数，表示搜索开始的字符位置————
 * 这个版本的 find() 会将起始搜索位置重置为参数的值，如输出所示。
 * <p>
 * public boolean find()
 * public boolean find(int start)
 */
public class Finding {
    public static void main(String[] args) {
        Matcher m = Pattern.compile("\\w+")
                .matcher("Evening is full of the linnet's wings");
        while (m.find()) {
            System.out.print(m.group() + " ");
        }
        System.out.println();
        int i = 0;
        // find(i) 不会要求“匹配必须从 i 这个位置开始”。它只是说：“从 i 开始往后找第一个能匹配到的地方”。
        while (m.find(i)) {
            System.out.print(m.group() + " ");
            i++;
        }
    }
}
/* Output:
Evening is full of the linnet s wings
Evening vening ening ning ing ng g is is s full full ull ll l of of f the the he e linnet linnet innet nnet net et t s s wings wings ings ngs gs s
 */
