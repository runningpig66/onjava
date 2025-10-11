package ch13_functional;

import java.util.function.IntSupplier;

/**
 * @author runningpig66
 * @date 2025/10/11 周六
 * @time 23:37
 * 代码清单 P.366
 * 闭包 - 在 lambda 表达式中使用的变量应为 final 类型或实际上为 final 类型的。
 */
public class Closure4 {
    IntSupplier makeFun(final int x) {
        final int i = 0;
        return () -> x + i;
    }
}
