package ch18_strings;

import java.util.Scanner;
import java.util.regex.MatchResult;

/**
 * @author runningpig66
 * @date 2025/12/16 周二
 * @time 20:18
 * P.586 §18.8 扫描输入 §18.8.2 使用正则表达式扫描
 * <p>
 * 除了扫描预定义的基本类型，你还可以用自己定义的正则表达式模式来扫描，这在扫描更复杂的数据时非常有用。下面这个示例扫描防火墙日志中的威胁数据：
 * <p>
 * next() 与特定模式一起使用时，该模式会和下一个输入分词进行匹配，结果由 match() 方法提供，它的工作方式与之前看到的正则表达式匹配相似。
 * <p>
 * 使用正则表达式扫描时，有一点要注意：该模式仅与下一个输入的分词进行匹配，因此，如果你的模式里包含了分隔符，那就永远不会匹配成功。
 * <p>
 * public boolean hasNext(String pattern)
 * public String next(String pattern)
 * public MatchResult match()
 */
public class ThreatAnalyzer {
    static String threatData =
            "58.27.82.161@08/10/2015\n" +
                    "204.45.234.40@08/11/2015\n" +
                    "58.27.82.161@08/11/2015\n" +
                    "58.27.82.161@08/12/2015\n" +
                    "58.27.82.161@08/12/2015\n" +
                    "[Next log section with different data format]";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(threatData);
        // 定义正则表达式模式：匹配IP地址@日期格式
        // 第一个捕获组 (\\d+[.]\\d+[.]\\d+[.]\\d+)：匹配 IP 地址（如 58.27.82.161）
        // 第二个捕获组 (\\d{2}/\\d{2}/\\d{4})：匹配日期（如 08/10/2015）
        String pattern = "(\\d+[.]\\d+[.]\\d+[.]\\d+)@" +
                "(\\d{2}/\\d{2}/\\d{4})";
        // scanner.hasNext(pattern)：检查下一个 token 是否匹配指定模式（不会消耗 token）
        while (scanner.hasNext(pattern)) {
            // 获取匹配模式的 token（如 "58.27.82.161@08/10/2015"）
            String next = scanner.next(pattern);
            System.out.println("next: " + next);
            // 获取上次匹配的详细信息（MatchResult 对象）
            MatchResult match = scanner.match();
            // 从 MatchResult 中提取捕获组
            String ip = match.group(1);
            String date = match.group(2);
            System.out.printf("Threat on %s from %s%n", date, ip);
            System.out.println();
        }
    }
}
/* Output:
next: 58.27.82.161@08/10/2015
Threat on 08/10/2015 from 58.27.82.161

next: 204.45.234.40@08/11/2015
Threat on 08/11/2015 from 204.45.234.40

next: 58.27.82.161@08/11/2015
Threat on 08/11/2015 from 58.27.82.161

next: 58.27.82.161@08/12/2015
Threat on 08/12/2015 from 58.27.82.161

next: 58.27.82.161@08/12/2015
Threat on 08/12/2015 from 58.27.82.161

 */
