package ch13_functional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
/**
 * @author runningpig66
 * @date 2025/10/12 周日
 * @time 9:00
 * 代码清单 P.368
 * 闭包 - 在 lambda 表达式中使用的变量应为 final 类型或实际上为 final 类型的。
 */
public class Closure8 {
    Supplier<List<Integer>> makeFun() {
        final List<Integer> ai = new ArrayList<>();
        ai.add(1);
        return () -> ai;
    }

    public static void main(String[] args) {
        Closure8 c7 = new Closure8();
        List<Integer>
                l1 = c7.makeFun().get(),
                l2 = c7.makeFun().get();
        System.out.println(l1);
        System.out.println(l2);
        l1.add(42);
        l2.add(96);
        System.out.println(l1);
        System.out.println(l2);
    }
}
/* Output:
[1]
[1]
[1, 42]
[1, 96]
 */
