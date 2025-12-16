package ch18_strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author runningpig66
 * @date 2025/12/15 周一
 * @time 20:23
 * P.579 §18.7 正则表达式 §18.7.4 Pattern 和 Matcher - 4. Pattern 标记
 * <p>
 * Pattern 类的 compile() 方法还有一个重载版本，它可以接受一个标记参数，来影响匹配行为：
 * public static Pattern compile(String regex, int flags)
 * 其中，flag 来自 Pattern 类中的常量（见表 18-8）。
 * <pre>
 * =============================== 表 18-8 Pattern Flags ===============================
 * 编译标记                          效 果
 * ------------------------------------------------------------------------------------
 * Pattern.CANON_EQ
 *     当且仅当两个字符的完全正则分解匹配时，才认为它们匹配。例如，当指定此标记时，
 *     表达式 \ u003F 将匹配字符串 ?。默认情况下，匹配不考虑正则的等价性
 * Pattern.CASE_INSENSITIVE (?i)
 *     默认情况下，匹配仅在 US-ASCII 字符集中进行时才不区分大小写。这个标记允许
 *     模式匹配时不考虑大小写。可以通过指定 UNICODE_CASE 标记，并结合这个标记来在
 *     Unicode 字符集里启用不区分大小写的匹配
 * Pattern.COMMENTS (?x)
 *     在这种模式下，空白符被忽略，并且以 # 开头的嵌入注释也会被忽略，直到行尾。
 *     UNIX 的行模式也可以通过嵌入的标记表达式来启用
 * Pattern.DOTALL (?s)
 *     在 dotall 模式下，表达式 . 匹配任何字符，包括换行符。默认情况下，. 表达式不匹配换行符
 * Pattern.MULTILINE (?m)
 *     在多行模式下，表达式 ^ 和 $ 分别匹配每一行的开头和结尾。此外，^ 匹配输入字符串
 *     的开头，$ 匹配输入字符串的结尾。默认情况下，这些表达式仅匹配整个输入字符串的开头和结尾
 * Pattern.UNICODE_CASE (?u)
 *     当指定了这个标记，并且同时启用了 CASE_INSENSITIVE 标记时，不区分大小写的匹配
 *     将以符合 Unicode 标准的方式完成。默认情况下，匹配仅在 US-ASCII 字符集中进行时才不区分大小写
 * Pattern.UNIX_LINES (?d)
 *     这种模式下，在 .、^ 和 $ 的行为里，只有换行符 \n 被识别
 * ====================================================================================
 * </pre>
 * 上表中列举的标记中，特别有用的是以下几种：
 * - Pattern.CASE_INSENSITIVE
 * - Pattern.MULTILINE
 * - Pattern.COMMENTS（有助于清晰度和/或文档记录）
 * 注意，你也可以在正则表达式中直接使用上表中的大多数标记，只需要将左栏括号中的字符插人希望模式生效的位置之前即可。
 * 可以通过“或”操作 (|) 来组合这些标记，实现多种效果：
 */
public class ReFlags {
    public static void main(String[] args) {
        // 编译正则表达式，并设置两个标志：
        // Pattern.CASE_INSENSITIVE: 忽略大小写，使 "^java" 能匹配 "java"、"Java"、"JAVA" 等
        // Pattern.MULTILINE: 多行模式，使 "^" 能匹配每一行的开头，而不仅仅是整个输入的开头
        Pattern p = Pattern.compile("^java", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher m = p.matcher("java has regex\nJava has regex\n" +
                "JAVA has pretty good regular expressions\n" +
                "Regular expressions are in Java");
        while (m.find()) {
            System.out.println("m.group(): " + m.group() + ", m.start(): " + m.start() + ", m.end(): " + m.end());
        }
    }
}
/* Output:
m.group(): java, m.start(): 0, m.end(): 4
m.group(): Java, m.start(): 15, m.end(): 19
m.group(): JAVA, m.start(): 30, m.end(): 34
 */
