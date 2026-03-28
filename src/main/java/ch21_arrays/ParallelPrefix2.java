package ch21_arrays;

import onjava.ArrayShow;
import onjava.Rand;

import java.util.Arrays;

/**
 * @author runningpig66
 * @date 2026-03-28
 * @time 19:03
 * P.805 §21.19 用 parallelPrefix() 进行累积计算
 * <p>
 * 并不存在 prefix() 方法；只有一个 parallelPrefix() 方法。这很像 Stream 类中的 reduce() 方法：
 * 它会对前一个元素和当前元素进行操作，将结果放入当前元素的位置；用字符串来举例可能会更清楚一些：
 */
public class ParallelPrefix2 {
    public static void main(String[] args) {
        String[] strings = new Rand.String(1).array(8);
        ArrayShow.show(strings);
        // static <T> void parallelPrefix(T[] array, BinaryOperator<T> op)
        Arrays.parallelPrefix(strings, (a, b) -> a + b);
        ArrayShow.show(strings);
    }
}
/* Output:
[b, t, p, e, n, p, c, c]
[b, bt, btp, btpe, btpen, btpenp, btpenpc, btpenpcc]
 */
