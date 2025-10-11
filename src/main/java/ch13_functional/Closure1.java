package ch13_functional;

import java.util.function.IntSupplier;

/**
 * @author runningpig66
 * @date 2025/10/11 周六
 * @time 20:55
 * 代码清单 P.365
 * 闭包 - 首先来看一个会返回函数的方法。而该函数会访问一个对象字段和一个方法参数：
 */
public class Closure1 {
    int i;

    IntSupplier makeFun(int x) {
        return () -> x + i++;
    }
}
