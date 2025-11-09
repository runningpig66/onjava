package ch14_streams;

import static java.util.stream.IntStream.range;

/**
 * @author runningpig66
 * @date 2025/10/23 周四
 * @time 22:41
 * 代码清单 P.382 IntStream 类提供了一个 range() 方法，
 * 可以生成一个流————由 int 值组成的序列。这在编写循环时非常方便
 */
public class Ranges {
    public static void main(String[] args) {
        // The traditional way:
        int result = 0;
        for (int i = 10; i < 20; i++) {
            result += i;
        }
        System.out.println(result);

        // for-in with a range:
        result = 0;
        for (int i : range(10, 20).toArray()) {
            result += i;
        }
        System.out.println(result);

        // Use streams:
        System.out.println(range(10, 20).sum());
    }
}
/* Output:
145
145
145
 */
