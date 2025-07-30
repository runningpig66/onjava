package ch12_collections;

import java.util.ArrayList;

/**
 * P.295
 *
 * @author runningpig66
 * @date 25/07/24/周四
 * @time 上午 0:13
 */
public class ApplesAndOrangesWithGenerics {
    public static void main(String[] args) {
        ArrayList<Apple> apples = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            apples.add(new Apple());
            // Compile-time error:
            // apples.add(new Orange());
        }
        for (Apple apple : apples) {
            System.out.println(apple.id());
        }
    }
}
/* Output:
0
1
2
*/
