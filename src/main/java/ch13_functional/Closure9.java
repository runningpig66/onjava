package ch13_functional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author runningpig66
 * @date 2025/10/12 周日
 * @time 9:12
 * 代码清单 P.369
 * 闭包 - 在 lambda 表达式中使用的变量应为 final 类型或实际上为 final 类型的。
 * {WillNotCompile}
 */
public class Closure9 {
    Supplier<List<Integer>> makeFun() {
        List<Integer> ai = new ArrayList<>();
        ai = new ArrayList<>();
        // Error: Variable used in lambda expression should be final or effectively final
        return () -> ai;
    }
}
