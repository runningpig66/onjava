package ch14_streams;

import static ch14_streams.RandInts.rands;

/**
 * @author runningpig66
 * @date 2025/11/6 周四
 * @time 1:13
 * 代码清单 P.406 终结操作：在每个流元素上应用某个终结操作
 * <p>
 * 这个版本确保 forEach 对元素的操作顺序是原始的流的顺序。
 * 对于非 parallel() 的流，使用 forEachOrdered() 不会有任何影响。
 * void forEachOrdered(Consumer);
 */
public class ForEach {
    static final int SZ = 14;

    public static void main(String[] args) {
        rands().limit(SZ)
                .forEach(n -> System.out.format("%d ", n));
        System.out.println();
        rands().limit(SZ)
                .parallel()
                .forEach(n -> System.out.format("%d ", n));
        System.out.println();
        rands().limit(SZ)
                .parallel()
                .forEachOrdered(n -> System.out.format("%d ", n));
    }
}
/* Output:
258 555 693 861 961 429 868 200 522 207 288 128 551 589
551 589 861 429 961 868 207 288 128 693 200 555 258 522
258 555 693 861 961 429 868 200 522 207 288 128 551 589
 */
