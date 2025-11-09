package ch14_streams;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * @author runningpig66
 * @date 2025/11/6 周四
 * @time 1:01
 * 代码清单 P.405 终结操作：将流转换为一个数组
 * <p>
 * 由 100 个 0~1000 范围内的 int 类型随机数组成的流，被转换成了一个数组。
 * 并存储在 rints 中，这样每次调用 rands() 就能重复获得相同的流了。
 */
public class RandInts {
    private static int[] rints = new Random(47)
            .ints(0, 1000)
            .limit(100)
            .toArray(); // 将流元素转换到适当类型的数组中

    public static IntStream rands() {
        return Arrays.stream(rints);
    }
}
