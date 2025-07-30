package ch12_collections;

import onjava.Stack;

/**
 * @author runningpig66
 * @date 2025-07-28
 * @time 上午 6:22
 */
public class StackTest2 {
    public static void main(String[] args) {
        Stack<String> stack = new Stack<>();
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
