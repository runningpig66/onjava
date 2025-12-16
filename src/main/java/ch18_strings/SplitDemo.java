package ch18_strings;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * @author runningpig66
 * @date 2025/12/15 周一
 * @time 21:54
 * P.580 §18.7 正则表达式 §18.7.5 split()
 * <p>
 * split() 根据输入的正则表达式来拆分字符串，然后返回拆分后的字符串对象数组。split() 还提供了另外一种形式，可以限制拆分的次数。
 * public String[] split(CharSequence input)
 * public String[] split(CharSequence input, int limit)
 * <p>
 * 这是一种在通用边界上拆分输入文本的便捷方法：
 */
public class SplitDemo {
    public static void main(String[] args) {
        String input = "This!!unusual use!!of exclamation!!points";
        // 使用正则表达式 "!!" 拆分字符串，无限制拆分次数；会将字符串在每次出现 "!!" 的地方拆开，并丢弃 "!!" 分隔符
        System.out.println(Arrays.toString(
                Pattern.compile("!!").split(input)
        ));
        // Only do the first three:
        // 注意：limit 参数控制拆分后的数组最大长度，拆分次数 = limit - 1
        // 当 limit = 3 时，字符串最多被拆分成 3 部分，即进行 2 次拆分
        System.out.println(Arrays.toString(
                Pattern.compile("!!").split(input, 3)
        ));
    }
}
/* Output:
[This, unusual use, of exclamation, points]
[This, unusual use, of exclamation!!points]
 */
