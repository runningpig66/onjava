package ch13_functional;

import java.util.function.Function;

/**
 * @author runningpig66
 * @date 2025/10/11 周六
 * @time 13:39
 * 代码清单 P.363 高阶函数 - 把函数当作返回值
 */
interface FuncSS extends Function<String, String> { // [1]
}

public class ProduceFunction {
    static FuncSS produce() {
//        return s -> s.toLowerCase(); // [2]
        return String::toLowerCase;
    }

    public static void main(String[] args) {
        FuncSS f = produce();
        System.out.println(f.apply("YELLING"));
    }
}
/* Output:
yelling
 */
