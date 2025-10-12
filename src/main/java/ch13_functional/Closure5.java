package ch13_functional;

import java.util.function.IntSupplier;

/**
 * @author runningpig66
 * @date 2025/10/11 周六
 * @time 23:43
 * 代码清单 P.367
 * 闭包 - 在 lambda 表达式中使用的变量应为 final 类型或实际上为 final 类型的。
 * {WillNotCompile}
 */
public class Closure5 {
    IntSupplier makeFun(int x) {
        int i = 0;
        i++;
        x++;
        // Errors: Variable used in lambda expression should be final or effectively final
        return () -> x + i;
    }
}
