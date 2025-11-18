package ch16_validating;

/**
 * @author runningpig66
 * @date 2025/11/18 周二
 * @time 23:14
 * 代码清单 P.482 前置条件：断言：1. Java断言语法
 * Non-informative style of assert
 * Must run using -ea flag: 你必须在运行程序时显示地启用断言。
 * {java -ea Assert1} or
 * {java -enableassertions Assert1}
 * {ThrowsException}
 * <p>
 * 你可以使用其他编程结构来模拟断言的效果，而对于 Java 直接提供的断言来说，它的亮点是易于编写。
 * 断言语句有两种形式：
 * assert boolean-expression;
 * assert boolean-expression: information-expression;
 * 两者都表示“我断言这个 boolean-expression 的值是 true”。如果不是这种情况，则断言会产生一个 AssertionError 异常。
 * 不幸的是，第一种断言形式产生的异常不包含 boolean-expression 的任何信息（这与大多数其他语言的断言机制相反）。
 * 下面是使用第一种形式断言的示例：
 */
public class Assert1 {
    public static void main(String[] args) {
        assert false;
    }
}
/* Error Output:
Exception in thread "main" java.lang.AssertionError
	at ch16_validating.Assert1.main(Assert1.java:24)
 */
