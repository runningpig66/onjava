package ch18_strings;

import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * @author runningpig66
 * @date 2025/12/16 周二
 * @time 20:49
 * P.587 §18.9 StringTokenizer
 * <p>
 * 在正则表达式（Java 1.4 引入）或 Scanner 类（Java 5 引入）之前，对字符串进行拆分的方式是使用 StringTokenizer 对其分词。
 * 但是现在有了正则表达式和 Scanner 类，对于同样的功能，它们实现起来更容易，也更简洁。下面是 StringTokenizer 与其他两种技术的简单比较：
 * <p>
 * 使用正则表达式或 Scanner 对象，你还可以使用更复杂的模式来拆分字符串，
 * 而这对 StringTokenizer 来说就很困难了。我们应该可以放心地说，StringTokenizer 已经过时了。
 */
public class ReplacingStringTokenizer {
    public static void main(String[] args) {
        String input = "But I'm not dead yet! I feel happy!";
        // 1. 使用传统的 StringTokenizer（Java 1.0 遗留类，现在不推荐使用）
        // 默认使用空白字符（空格、制表符、换行等）作为分隔符
        StringTokenizer stoke = new StringTokenizer(input);
        while (stoke.hasMoreElements()) {
            // nextToken() 返回下一个 token
            System.out.print(stoke.nextToken() + " ");
        }
        System.out.println();
        // 2. 使用 String.split() 方法
        System.out.println(Arrays.toString(input.split(" ")));
        // 3. 使用 Scanner
        Scanner scanner = new Scanner(input);
        while (scanner.hasNext()) {
            System.out.print(scanner.next() + " ");
        }
    }
}
/* Output:
But I'm not dead yet! I feel happy!
[But, I'm, not, dead, yet!, I, feel, happy!]
But I'm not dead yet! I feel happy!
 */
