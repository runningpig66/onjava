package ch12_collections;

import java.util.Arrays;

/**
 * @author runningpig66
 * @date 2025-07-30
 * @time 下午 14:26
 */
public class ArrayIsNotIterable {
    static <T> void test(Iterable<T> ib) {
        for (T t : ib) {
            System.out.print(t + " ");
        }
    }

    public static void main(String[] args) {
        test(Arrays.asList(1, 2, 3));
        String[] strings = {"A", "B", "C"};
        // An array works in for-in, but it's not Iterable:
        //- test(strings);
        // You must explicitly convert it to an Iterable:
        test(Arrays.asList(strings));
    }
}
/* Output:
1 2 3 A B C
 */
