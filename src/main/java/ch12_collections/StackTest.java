package ch12_collections;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author runningpig66
 * @date 2025-07-28
 * @time 上午 6:05
 */
public class StackTest {
    public static void main(String[] args) {
        Deque<String> stack = new ArrayDeque<>();
        for (String s : "My dog has fleas".split(" ")) {
            stack.push(s);
        }
        while (!stack.isEmpty()) {
            System.out.print(stack.pop() + " ");
        }
    }
}
/* Output:
fleas has dog My
*/
