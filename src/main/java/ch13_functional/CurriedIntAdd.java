package ch13_functional;

import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;

/**
 * @author runningpig66
 * @date 2025/10/13 周一
 * @time 3:53
 * 代码清单 P.373
 * 柯里化和部分求值 - 当处理基本类型和装箱时，可以使用适当的函数式接口：
 */
public class CurriedIntAdd {
    public static void main(String[] args) {
        IntFunction<IntUnaryOperator> curriedIntAdd =
                a -> b -> a + b;
        IntUnaryOperator add4 = curriedIntAdd.apply(4);
        System.out.println(add4.applyAsInt(5));
    }
}
/* Output:
9
 */
