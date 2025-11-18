package ch16_validating;

/**
 * @author runningpig66
 * @date 2025/11/18 周二
 * @time 23:32
 * 代码清单 P.483 前置条件：断言：1. Java断言语法
 * Assert with an information-expression
 * {java Assert2 -ea}
 * {ThrowsException}
 * <p>
 * 以上示例 (Assert1.java) 的输出中没有太多有用的信息。相较而言，如果使用 information-expression 形式的断言，
 * 就可以在异常栈里生成一个有用的消息。最有用的 information-expression 通常是给程序员看的字符串文本：
 * information-expression 可以生成任何类型的对象，因此我们通常会构造一个更复杂的字符串，其中包含与失败断言有关的对象的值。
 */
public class Assert2 {
    public static void main(String[] args) {
        assert false : "Here's a message saying what happened";
    }
}
/* Output:
Exception in thread "main" java.lang.AssertionError: Here's a message saying what happened
	at ch16_validating.Assert2.main(Assert2.java:18)
 */
