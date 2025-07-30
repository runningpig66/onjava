package ch12_collections;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 * @author runningpig66
 * @date 2025-07-30
 * @time 下午 14:13
 */
public class ForInCollections {
    public static void main(String[] args) {
        Collection<String> cs = new LinkedList<>();
        Collections.addAll(cs, "Take the long way home".split(" "));
        for (String s : cs) {
            System.out.print("'" + s + "' ");
        }
    }
}
/* Output:
'Take' 'the' 'long' 'way' 'home'
 */
