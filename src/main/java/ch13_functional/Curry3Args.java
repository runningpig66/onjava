package ch13_functional;

import java.util.function.Function;

/**
 * @author runningpig66
 * @date 2025/10/13 周一
 * @time 3:46
 * 代码清单 P.373
 * 柯里化和部分求值 - 对接受三个参数的函数进行柯里化：
 */
public class Curry3Args {
    public static void main(String[] args) {
        Function<String,
                Function<String,
                        Function<String, String>>> sum =
                a -> b -> c -> a + b + c;
        Function<String,
                Function<String, String>> hi = sum.apply("Hi ");
        Function<String, String> ho = hi.apply("Ho ");
        System.out.println(ho.apply("Hup"));
    }
}
/* Output:
Hi Ho Hup
 */
