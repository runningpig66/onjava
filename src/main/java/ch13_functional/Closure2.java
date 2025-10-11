package ch13_functional;

import java.util.function.IntSupplier;

/**
 * @author runningpig66
 * @date 2025/10/11 周六
 * @time 21:14
 * 代码清单 P.366
 * 闭包 - 如果 i 是 makeFun() 中的局部变量，又会怎么样呢？
 * 正常情况下，makeFun() 执行完毕，i 也就消失了。然而这时仍然可以编译：
 */
public class Closure2 {
    IntSupplier makeFun(int x) {
        int i = 0;
        return () -> x + i;
    }
}
