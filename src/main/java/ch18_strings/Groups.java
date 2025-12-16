package ch18_strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author runningpig66
 * @date 2025/12/15 周一
 * @time 15:20
 * P.575 §18.7 正则表达式 §18.7.4 Pattern 和 Matcher - 2. 分组
 * <p>
 * 分组（group）是用括号括起来的正则表达式，后续代码里可以用分组号来调用它们。
 * 分组 0 表示整个表达式，分组 1 是第一个带括号的分组，以此类推。因此，下面这个表达式中共有 3 个分组：
 * A(B(C))D: 分组 0 是 ABCD，分组 1 是 BC，分组 2 是 C。
 * <p>
 * Matcher 对象提供了一些方法，可以获取与分组相关的信息。
 * public int groupCount() 返回该匹配器模式中的分组数目：分组 0 不包括在此计数中。
 * public String group() 返回前一次匹配操作（例如 find()）的第 0 个分组（即整个匹配）。
 * public String group(int i) 返回前一次匹配操作的第 i 个分组。如果匹配成功，但指定的分组未能匹配输入字符串的任何部分，则返回 null。
 * public int start(int group) 返回在前一次匹配操作中找到的分组的起始索引。
 * public int end(int group) 返回在前一次匹配操作中找到的分组的最后一个字符的索引加 1 的值。
 */
public class Groups {
    public static final String POEM =
            "Twas brillig, and the slithy toves\n" +
                    "Did gyre and gimble in the wabe.\n" +
                    "All mimsy were the borogoves,\n" +
                    "And the mome raths outgrabe.\n\n" +
                    "Beware the Jabberwock, my son,\n" +
                    "The jaws that bite, the claws that catch.\n" +
                    "Beware the Jubjub bird, and shun\n" +
                    "The frumious Bandersnatch.";

    public static void main(String[] args) {
        // 正则表达式模式里有几个带括号的分组，由任意数量的非空白字符（\\S+）和任意数量的空白字符（\\s+）组成。
        // 目标是捕获每行的最后三个单词，行尾由 $ 分隔。但在正常情况下是将 $ 与整个输入序列的结尾进行匹配，
        // 因此我们必须明确告诉正则表达式注意输入中的换行符。这是通过序列开头的模式标记 (?m) 来完成的（模式标记很快就会介绍）。
        // 补充：Pattern.MULTILINE (?m)
        //      在多行模式下，表达式 ^ 和 $ 分别匹配每一行的开头和结尾。此外，^ 匹配输入字符串
        //      的开头，$ 匹配输入字符串的结尾。默认情况下，这些表达式仅匹配整个输入字符串的开头和结尾
        Matcher m = Pattern.compile("(?m)(\\S+)\\s+((\\S+)\\s+(\\S+))$").matcher(POEM);
//        Matcher m = Pattern.compile("(\\S+)\\s+((\\S+)\\s+(\\S+))$", Pattern.MULTILINE).matcher(POEM); // 等价
        while (m.find()) {
            for (int j = 0; j <= m.groupCount(); j++) {
                System.out.print("[" + m.group(j) + "]");
            }
            System.out.println();
        }
    }
}
/* Output:
[the slithy toves][the][slithy toves][slithy][toves]
[in the wabe.][in][the wabe.][the][wabe.]
[were the borogoves,][were][the borogoves,][the][borogoves,]
[mome raths outgrabe.][mome][raths outgrabe.][raths][outgrabe.]
[Jabberwock, my son,][Jabberwock,][my son,][my][son,]
[claws that catch.][claws][that catch.][that][catch.]
[bird, and shun][bird,][and shun][and][shun]
[The frumious Bandersnatch.][The][frumious Bandersnatch.][frumious][Bandersnatch.]
 */
