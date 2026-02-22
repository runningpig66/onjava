package ch24_collectiontopics;

import java.util.LinkedList;
import java.util.Stack;

import static onjava.HTMLColors.border;

/**
 * @author runningpig66
 * @date 2月23日 周一
 * @time 1:51
 * P.157 §3.15 Java 1.0/1.1 的集合类 §3.15.3 Stack
 * Demonstration of Stack Class
 */
enum Month {
    JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE,
    JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER
}

public class Stacks {
    public static void main(String[] args) {
        Stack<String> stack = new Stack<>();
        for (Month m : Month.values()) {
            stack.push(m.toString());
        }
        System.out.println("stack = " + stack);
        // Treating a stack as a Vector:
        stack.addElement("The last line");
        System.out.println("element 5 = " + stack.elementAt(5));
        System.out.print("popping elements: ");
        while (!stack.empty()) {
            System.out.print(stack.pop() + " ");
        }
        System.out.println();
        border();

        // Using a LinkedList as a Stack:
        LinkedList<String> lstack = new LinkedList<>();
        for (Month m : Month.values()) {
            lstack.addFirst(m.toString());
        }
        System.out.println("lstack = " + lstack);
        while (!lstack.isEmpty()) {
            System.out.print(lstack.removeFirst() + " ");
        }
        System.out.println();
        border();

        // Using the Stack class from the Collections Chapter:
        onjava.Stack<String> stack2 = new onjava.Stack<>();
        for (Month m : Month.values()) {
            stack2.push(m.toString());
        }
        System.out.println("stack2 = " + stack2);
        while (!stack2.isEmpty()) {
            System.out.print(stack2.pop() + " ");
        }
    }
}
/* Output:
stack = [JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER]
elements 5 = JUNE
popping elements: The last line NOVEMBER OCTOBER SEPTEMBER AUGUST JULY JUNE MAY APRIL MARCH FEBRUARY JANUARY
******************************
lstack = [NOVEMBER, OCTOBER, SEPTEMBER, AUGUST, JULY, JUNE, MAY, APRIL, MARCH, FEBRUARY, JANUARY]
NOVEMBER OCTOBER SEPTEMBER AUGUST JULY JUNE MAY APRIL MARCH FEBRUARY JANUARY
******************************
stack2 = [NOVEMBER, OCTOBER, SEPTEMBER, AUGUST, JULY, JUNE, MAY, APRIL, MARCH, FEBRUARY, JANUARY]
NOVEMBER OCTOBER SEPTEMBER AUGUST JULY JUNE MAY APRIL MARCH FEBRUARY JANUARY
 */
