package ch13_functional;

import java.util.function.IntSupplier;

/**
 * @author runningpig66
 * @date 2025/10/11 周六
 * @time 23:53
 * 代码清单 P.368
 * 闭包 - 在 lambda 表达式中使用的变量应为 final 类型或实际上为 final 类型的。
 * {WillNotCompile}
 */
public class Closure7 {
    IntSupplier makeFun(int x) {
        Integer i = 0;
        i = i + 1;
        // Error: Variable used in lambda expression should be final or effectively final
        return () -> x + i;
    }
}
