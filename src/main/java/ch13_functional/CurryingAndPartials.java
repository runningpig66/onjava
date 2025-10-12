package ch13_functional;

import java.util.function.Function;

/**
 * @author runningpig66
 * @date 2025/10/13 周一
 * @time 1:27
 * 代码清单 P.372
 * 柯里化和部分求值 - 对接受两个参数的函数进行柯里化：
 */
public class CurryingAndPartials {
    // Uncurried:
    static String uncurried(String a, String b) {
        return a + b;
    }

    public static void main(String[] args) {
        // Curried function:
        Function<String, Function<String, String>> sum =
                a -> b -> a + b; // [1]

        System.out.println(uncurried("Hi ", "Ho"));

        Function<String, String> hi = sum.apply("Hi ");
        System.out.println(hi.apply("Ho"));

        // Partial application:
        Function<String, String> sumHi = sum.apply("Hup ");
        System.out.println(sumHi.apply("Ho"));
        System.out.println(sumHi.apply("Hey"));
    }
}
/* Output:
Hi Ho
Hi Ho
Hup Ho
Hup Hey
 */
