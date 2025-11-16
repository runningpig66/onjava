package ch15_exceptions;

/**
 * @author runningpig66
 * @date 2025/11/14 周五
 * @time 22:27
 * 代码清单 P.445 使用 finally 执行清理：finally 是干什么用的
 * Why use finally?
 * <p>
 * 要清理内存之外的某些东西时，finally 子句是必要的。
 * 例子包括打开的文件或网络连接，画在屏幕上的东西，甚至是现实世界中的一个开关：
 * <p>
 * 此处的目标是确保当 main() 完成的时候，这个开关处于关闭状态，
 * 因此 sw.off() 被放在了 try 块和每个异常处理程序的末尾。
 * 但是程序有可能抛出某个没有在这里被捕获的异常，sw.off() 也就有可能被漏掉。
 * 然而有了 finally, try 块中的清理代码只需要放到一个地方即可：WithFinally.java
 */
public class OnOffSwitch {
    private static final Switch sw = new Switch();

    public static void f() throws OnOffException1, OnOffException2 {
    }

    public static void main(String[] args) {
        try {
            sw.on();
            // Code that can throw exceptions...
            f();
            sw.off();
        } catch (OnOffException1 e) {
            System.out.println("OnOffException1");
            sw.off();
        } catch (OnOffException2 e) {
            System.out.println("OnOffException2");
            sw.off();
        }
    }
}
/* Output:
on
off
 */
