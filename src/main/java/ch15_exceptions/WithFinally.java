package ch15_exceptions;

/**
 * @author runningpig66
 * @date 2025/11/15 周六
 * @time 16:21
 * 代码清单 P.445 使用 finally 执行清理：finally 是干什么用的
 * Finally  cleanup
 * <p>
 * 然而有了 finally, try 块中的清理代码只需要放到一个地方即可：
 */
public class WithFinally {
    static Switch sw = new Switch();

    public static void main(String[] args) {
        try {
            sw.on();
            // Code that can throw exception...
            OnOffSwitch.f();
        } catch (OnOffException1 e) {
            System.out.println("OnOffException1");
        } catch (OnOffException2 e) {
            System.out.println("OnOffException2");
        } finally {
            // 不管发生什么，这里的 sw.off() 都会确保运行。
            sw.off();
        }
    }
}
/* Output:
on
off
 */
