package ch13_functional;

/**
 * @author runningpig66
 * @date 2025/9/26 周五
 * @time 5:56
 * 代码清单 P.361
 */
public class TriFunctionTest {
    static int f(int i, long l, double d) {
        return 99;
    }

    public static void main(String[] args) {
        TriFunction<Integer, Long, Double, Integer> tf = TriFunctionTest::f;
        tf = (i, l, d) -> 12;
    }
}
