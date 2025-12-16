package ch18_strings;

/**
 * @author runningpig66
 * @date 2025/12/13 周六
 * @time 16:00
 * P.568 §18.7 正则表达式 §18.7.1 基础
 * <p>
 * String.split() 有一个重载版本可以限制拆分发生的次数。还可以使用正则表达式进行替换。你可以只替换第一个匹配的项，也可以全部替换：
 * public String replaceFirst(String regex, String replacement)
 * public String replaceAll(String regex, String replacement)
 * <p>
 * 第一个表达式要匹配的是，字母 f 后跟一个或多个单词字符（注意这次 w 是小写的），
 * 它只替换找到的第一个匹配项，因此单词 “found” 被替换为 “located”。
 * 第二个表达式是竖线分隔的三个单词，竖线表示“或”操作，因此它会匹配这三个单词中的任意一个，并替换找到的所有匹配项。
 */
public class Replacing {
    static String s = Splitting.knights;

    public static void main(String[] args) {
        System.out.println(s.replaceFirst("f\\w+", "located"));
        System.out.println(s.replaceAll("shrubbery|tree|herring", "banana"));
    }
}
/* Output:
Then, when you have located the shrubbery, you must cut down the mightiest tree in the forest...with... a herring!
Then, when you have found the banana, you must cut down the mightiest banana in the forest...with... a banana!
 */
