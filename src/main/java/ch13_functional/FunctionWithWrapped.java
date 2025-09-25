package ch13_functional;

import java.util.function.Function;
import java.util.function.IntToDoubleFunction;

/**
 * @author runningpig66
 * @date 2025/9/26 周五
 * @time 6:20
 * 代码清单 P.362
 */
public class FunctionWithWrapped {
    public static void main(String[] args) {
        Function<Integer, Double> fid = i -> (double) i;
        IntToDoubleFunction fid2 = i -> i;
    }
}
