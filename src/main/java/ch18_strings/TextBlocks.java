package ch18_strings;

/**
 * @author runningpig66
 * @date 2025/12/13 周六
 * @time 10:35
 * P.563 §18.6 新特性：文本块
 * {NewFeature} Since JDK 15
 * Poem: Antigonish by Hughes Mearns
 * <p>
 * JDK 15 最终添加了文本块 (text block), 这是从 Python 语言借鉴而来的一个特性。
 * 我们使用三引号来表示包含换行符的文本块。文本块可以让我们更轻松地创建多行文本：
 * <p>
 * OLD 展示了处理多行字符串的传统方式，里面有很多换行符 \n 和符号 + 。NEW 消除了这些符号，提供了更好、更易读的语法。
 * 注意开头的 """ 后面的换行符会被自动去掉，块中的公用缩进也会被去掉，所以 NEW 的结果没有缩进。
 * 如果想要保留缩进，那就移动最后的 """ 来产生所需的缩进，如 Indentation.java 所示。
 */
public class TextBlocks {
    public static final String OLD =
            "Yesterday, upon the stair,\n" +
                    "I met a man who wasn't there\n" +
                    "He wasn't there again today\n" +
                    "I wish, I wish he'd go away...\n" +
                    "\n" +
                    "When I came home last night at three\n" +
                    "The man was waiting there for me\n" +
                    "But when I looked around the hall\n" +
                    "I couldn't see him there at all!\n";

    public static final String NEW = """
            Yesterday, upon the stair,
            I met a man who wasn't there
            He wasn't there again today
            I wish, I wish he'd go away...
            
            When I came home last night at three
            The man was waiting there for me
            But when I looked around the hall
            I couldn't see him there at all!
            """;

    public static void main(String[] args) {
        System.out.println(OLD.equals(NEW));
        System.out.println(NEW);
    }
}
/* Output:
true
Yesterday, upon the stair,
I met a man who wasn't there
He wasn't there again today
I wish, I wish he'd go away...

When I came home last night at three
The man was waiting there for me
But when I looked around the hall
I couldn't see him there at all!

 */
