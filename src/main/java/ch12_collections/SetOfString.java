package ch12_collections;

import java.util.HashSet;
import java.util.Set;

/**
 * @author runningpig66
 * @date 2025-07-28
 * @time 上午 7:12
 */
public class SetOfString {
    public static void main(String[] args) {
        Set<String> colors = new HashSet<>();
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
[Red, Yellow, Blue, Purple, Orange]
*/
