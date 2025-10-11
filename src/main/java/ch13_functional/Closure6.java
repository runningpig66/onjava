package ch13_functional;

import java.util.function.IntSupplier;

/**
 * @author runningpig66
 * @date 2025/10/11 周六
 * @time 23:47
 * 代码清单 P.367
 * 闭包 - 在 lambda 表达式中使用的变量应为 final 类型或实际上为 final 类型的。
 * 实际上我们可以这样修复 Closure5.java 中的问题：在闭包中使用 × 和 i 之前，先将其赋值给最终变量。
 */
public class Closure6 {
    IntSupplier makeFun(int x) {
        int i = 0;
        i++;
        x++;
        // 因为在赋值之后我们并不会修改 iFinal 和 xFinal，所以这里使用 final 是多余的。
        final int iFinal = i;
        final int xFinal = x;
        return () -> xFinal + iFinal;
    }
}
