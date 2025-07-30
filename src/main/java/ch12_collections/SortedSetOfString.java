package ch12_collections;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author runningpig66
 * @date 2025-07-28
 * @time 上午 7:14
 */
public class SortedSetOfString {
    public static void main(String[] args) {
        Set<String> colors = new TreeSet<>();
        for (int i = 0; i < 100; i++) {
            colors.add("Yellow");
            colors.add("Blue");
            colors.add("Red");
            colors.add("Red");
            colors.add("Orange");
            colors.add("Yellow");
            colors.add("Blue");
            colors.add("Purple");
        }
        System.out.println(colors);
    }
}
/* Output:
[Blue, Orange, Purple, Red, Yellow]
*/
