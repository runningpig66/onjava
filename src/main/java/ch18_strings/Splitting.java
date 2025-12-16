package ch18_strings;

import java.util.Arrays;

/**
 * @author runningpig66
 * @date 2025/12/13 周六
 * @time 15:41
 * P.567 §18.7 正则表达式 §18.7.1 基础
 * <p>
 * String 类中内置了一个很有用的正则表达式工具 split()。它可以“围绕给定正则表达式的匹配项来拆分字符串”。
 * public String[] split(String regex)
 * <p>
 * 首先，请注意可以使用普通字符作为正则表达式————正则表达式不必包含特殊字符，如 split() 的第一次调用所示，它只使用空格进行了拆分。
 * 第二次和第三次调用的 split() 使用了 \\W，表示非单词字符（小写版本的 \\w 表示单词字符）。在第二种情况下标点符号被删除了。
 * 第三次调用的 split() 表示“字母 n 后跟一个或多个非单词字符”。用来拆分的模式，即字符串中与正则表达式匹配的部分，并不会出现在结果中。
 */
public class Splitting {
    public static String knights =
            "Then, when you have found the shrubbery, " +
                    "you must cut down the mightiest tree in the " +
                    "forest...with... a herring!";

    public static void split(String regex) {
        System.out.println(Arrays.toString(knights.split(regex)));
    }

    public static void main(String[] args) {
        split(" "); // Doesn't have to contain regex chars
        split("\\W+"); // Non-word characters
        split("n\\W+"); // 'n' followed by non-words
    }
}
/* Output:
[Then,, when, you, have, found, the, shrubbery,, you, must, cut, down, the, mightiest, tree, in, the, forest...with..., a, herring!]
[Then, when, you, have, found, the, shrubbery, you, must, cut, down, the, mightiest, tree, in, the, forest, with, a, herring]
[The, whe, you have found the shrubbery, you must cut dow, the mightiest tree i, the forest...with... a herring!]
 */
