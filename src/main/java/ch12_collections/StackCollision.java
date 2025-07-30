package ch12_collections;

/**
 * @author runningpig66
 * @date 2025-07-28
 * @time 上午 6:25
 */
public class StackCollision {
    public static void main(String[] args) {
        onjava.Stack<String> stack = new onjava.Stack<>();
        for (String s : "My dog has fleas".split(" ")) {
            stack.push(s);
        }
        while (!stack.isEmpty()) {
            System.out.print(stack.pop() + " ");
        }
        System.out.println();
        java.util.Stack<String> stack2 = new java.util.Stack<>();
        for (String s : "My dog has fleas".split(" ")) {
            stack2.push(s);
        }
        while (!stack2.isEmpty()) {
            System.out.print(stack2.pop() + " ");
        }
    }
}
/* Output:
fleas has dog My
fleas has dog My
*/
