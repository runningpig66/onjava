package ch13_functional;

import java.util.function.Function;

/**
 * @author runningpig66
 * @date 2025/10/12 周日
 * @time 11:07
 * 代码清单 P.370
 * 函数组合 - 以下示例使用了来自 Function 的 compose() 和 andThen()：
 */
public class FunctionComposition {
    static Function<String, String>
            f1 = s -> {
        System.out.println(s);
        return s.replace('A', '_');
    },
            f2 = s -> s.substring(3),
            f3 = String::toLowerCase,
            f4 = f1.compose(f2).andThen(f3);

    public static void main(String[] args) {
        System.out.println(f4.apply("GO AFTER ALL AMBULANCES"));
    }
}
/* Output:
AFTER ALL AMBULANCES
_fter _ll _mbul_nces
 */
