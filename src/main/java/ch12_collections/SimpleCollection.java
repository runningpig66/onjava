package ch12_collections;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author runningpig66
 * @date 25/07/24/周四
 * @time 上午 1:08
 */
public class SimpleCollection {
    public static void main(String[] args) {
        Collection<Integer> c = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            c.add(i); // 自动装箱
        }
        for (Integer i : c) {
            System.out.print(i + ", ");
        }
    }
}
/* Output:
0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
 */
