package ch21_arrays;

import onjava.ArrayShow;

import java.util.stream.LongStream;

/**
 * @author runningpig66
 * @date 2026-03-24
 * @time 22:57
 * P.789 §21.12 关于数组并行 §21.12.2 parallelSetAll()
 * <p>
 * 用流式计算（Stream）可以使代码更优雅。举例来说，假设我们要创建一个 long 型数组，并用从 0 开始递增的数据来填充它：
 */
public class CountUpward {
    static long[] fillCounted(int size) {
        // static LongStream iterate(final long seed, final LongUnaryOperator f)
        return LongStream.iterate(0, i -> i + 1)
                .limit(size)
                .toArray();
    }

    public static void main(String[] args) {
        long[] l1 = fillCounted(20);
        ArrayShow.show(l1);
        // On my machine, this runs out of heap space:
        //- long[] l2 = fillCounted(1_000_000_000);
    }
}
/* Output:
[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19]
 */
