package ch18_strings;

import java.util.Scanner;

/**
 * @author runningpig66
 * @date 2025/12/16 周二
 * @time 18:12
 * P.586 §18.8 扫描输入 §18.8.1 Scanner 分隔符
 * <p>
 * 默认情况下，Scanner 通过空格分割输入数据，但也可以用正则表达式的形式来指定自己的分隔符模式：
 * <p>
 * 此示例使用逗号（由任意数量的空白符包围）作为分隔符，来处理读取的给定字符串。同样的技术也可以用来读取逗号分隔的文件。
 * 除了用于设置分隔符模式的 useDelimiter()，还有 delimiter() 方法，它返回了当前用作分隔符的 Pattern 对象。
 * public Scanner useDelimiter(String pattern)
 * public Pattern delimiter()
 */
public class ScannerDelimiter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner("12, 42, 78, 99, 42");
        // 设置 Scanner 的分隔符为正则表达式 "\\s*,\\s*" 匹配一个逗号，逗号前后可以有零个或多个空白字符（包括空格、制表符等）
        scanner.useDelimiter("\\s*,\\s*");
        // hasNextInt() 方法会检查下一个 token，但不会跳过非整数 token
        // 注意：如果下一个 token 不是整数，hasNextInt() 返回 false，循环会终止
        while (scanner.hasNextInt()) {
            // 读取下一个整数 token 并将其作为 int 类型打印
            System.out.println(scanner.nextInt());
        }
    }
}
/* Output:
12
42
78
99
42
 */
