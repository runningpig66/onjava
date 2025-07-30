package ch12_collections;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author runningpig66
 * @date 2025-07-28
 * @time 上午 7:02
 */
public class SetOfInteger {
    public static void main(String[] args) {
        Random rand = new Random();
        Set<Integer> intset = new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            intset.add(rand.nextInt(30));
        }
        System.out.println(intset);
    }
}
/* Output:
[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29]
*/
