package ch13_functional;

import java.util.function.IntSupplier;

/**
 * @author runningpig66
 * @date 2025/10/11 周六
 * @time 21:05
 * 代码清单 P.365
 * 闭包 - 当然，如果我们对同一个对象调用多次 makeFun()，最终就会有多个函数全部共享同祥的 i 的存储空间：
 */
public class SharedStorage {
    public static void main(String[] args) {
        Closure1 c1 = new Closure1();
        IntSupplier f1 = c1.makeFun(0);
        IntSupplier f2 = c1.makeFun(0);
        IntSupplier f3 = c1.makeFun(0);
        System.out.println(f1.getAsInt());
        System.out.println(f2.getAsInt());
        System.out.println(f3.getAsInt());
    }
}
/* Output:
0
1
2
 */
