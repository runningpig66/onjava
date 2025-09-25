package ch13_functional;

import java.util.function.BiConsumer;

/**
 * @author runningpig66
 * @date 2025/9/26 周五
 * @time 6:03
 * 代码清单 P.361
 */
public class BiConsumerPermutations {
    static BiConsumer<Integer, Double> bicid = (i, d) ->
            System.out.format("%d, %f%n", i, d);
    static BiConsumer<Double, Integer> bicdi = (d, i) ->
            System.out.format("%d, %f%n", i, d);
    static BiConsumer<Integer, Long> bicil = (i, l) ->
            System.out.format("%d, %d%n", i, l);

    public static void main(String[] args) {
        bicid.accept(47, 11.34);
        bicdi.accept(22.45, 92);
        bicil.accept(1, 11L);
    }
}
/* Output:
47, 11.340000
92, 22.450000
1, 11
 */
