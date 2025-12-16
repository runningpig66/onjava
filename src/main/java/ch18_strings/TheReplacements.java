package ch18_strings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author runningpig66
 * @date 2025/12/15 周一
 * @time 22:24
 * P.581 §18.7 正则表达式 §18.7.6 替换操作
 * {Working directory: E:\IdeaProjects\onjava\src\main\java\ch18_strings}
 * <pre>
 * 正则表达式对于替换文本特别有用。下面是一些可用的方法。
 * - String replaceFirst(String replacement) 用参数 replacement 替换输入字符串的第一个匹配的部分。
 * - String replaceAll(String replacement) 用参数 replacement 替换输入字符串的每个匹配的部分。
 * - Matcher appendReplacement(StringBuffer sbuf, String replacement) 执行逐步替换，并保存到 sbuf 中，
 *   而不是像 replaceFirst() 和 replaceAll() 那样分别替换第一个匹配和全部匹配。这是一个非常重要的方法，
 *   因为你可以调用其他方法来处理或生成 replacement（replaceFirst() 和 replaceAll() 只能放入固定的字符串）。
 *   使用此方法，你能够以编程的方式进行分组，从而创建更强大的替换功能。
 * - 在调用了一次或多次 appendReplacement() 方法后，可以再调用 StringBuffer appendTail(StringBuffer sbuf) 方法，
 *   将输入字符串的剩余部分复制到 sbuf。
 * </pre>
 * 下面是一个包含了以上所有替换操作的示例。开头的注释文本块就是示例中要被正则表达式提取并处理的输入字符串：
 */
/*! Here's a block of text to use as input to
    the regular expression matcher. Note that we
    first extract the block of text by looking for
    the special delimiters, then process the
    extracted block. !*/
public class TheReplacements {
    public static void main(String[] args) throws IOException {
        String s = Files.lines(Paths.get("TheReplacements.java"))
                .collect(Collectors.joining("\n"));
        // Match specially commented block of text above:
        Matcher mInput = Pattern.compile("/\\*!(.*)!\\*/", Pattern.DOTALL).matcher(s);
        if (mInput.find()) {
            s = mInput.group(1); // Captured by parentheses
        }
        // Replace two or more spaces with a single space:
        s = s.replaceAll(" {2,}", " ");
        // Replace 1+ spaces at the beginning of each
        // line with no spaces. Must enable MULTILINE mode:
        s = s.replaceAll("(?m)^ +", "");
        System.out.println(s);

        s = s.replaceFirst("[aeiou]", "(VOWEL1)");
        // 创建一个 StringBuffer 作为“结果收集器”，用于逐步构建最终的替换后字符串
        StringBuffer sbuf = new StringBuffer();
        // 编译一个匹配所有小写元音字母的正则表达式
        Pattern p = Pattern.compile("[aeiou]");
        Matcher m = p.matcher(s);
        // Process the find information as you
        // perform the replacements:
        while (m.find()) {
            // 1. 将“上次匹配结束”到“本次匹配开始”之间的原始文本追加到 sbuf
            // 2. 然后将指定的替换内容（这里将匹配到的元音变为大写）追加到 sbuf
            m.appendReplacement(sbuf, m.group().toUpperCase());
        }
        // Put in the remainder of the text:
        // 循环结束后，将“最后一次匹配结束”到“字符串末尾”之间剩余的原始文本追加到 sbuf
        m.appendTail(sbuf);
        System.out.println(sbuf);
    }
}
/* Output:
Here's a block of text to use as input to
the regular expression matcher. Note that we
first extract the block of text by looking for
the special delimiters, then process the
extracted block.
H(VOWEL1)rE's A blOck Of tExt tO UsE As InpUt tO
thE rEgUlAr ExprEssIOn mAtchEr. NOtE thAt wE
fIrst ExtrAct thE blOck Of tExt by lOOkIng fOr
thE spEcIAl dElImItErs, thEn prOcEss thE
ExtrActEd blOck.
 */
