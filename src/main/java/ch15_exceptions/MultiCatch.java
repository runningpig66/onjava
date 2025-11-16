package ch15_exceptions;

/**
 * @author runningpig66
 * @date 2025/11/11 周二
 * @time 17:45
 * 代码清单 P.431 捕捉任何异常：多重捕捉
 * <p>
 * 利用 Java7 提供的多重捕捉 (multi-catch) 处理程序，
 * 我们可以在一个 catch 子句中用 "|" 操作符把不同类型的异常连接起来：
 */
public class MultiCatch {
    void x() throws Except1, Except2, Except3, Except4 {
    }

    void process() {
    }

    void f() {
        try {
            x();
        } catch (Except1 | Except2 | Except3 | Except4 e) {
            process();
        }
    }
}
