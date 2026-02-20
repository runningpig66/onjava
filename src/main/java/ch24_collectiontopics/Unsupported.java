package ch24_collectiontopics;

import java.util.*;

/**
 * @author runningpig66
 * @date 2月18日 周三
 * @time 22:22
 * P.127 §3.9 可选的操作 §不支持的操作
 * Unsupported operations in Java collections
 * <p>
 * notes: Java 集合中的复制、视图与不可变机制笔记.md
 */
public class Unsupported {
    static void check(String description, Runnable tst) {
        try {
            tst.run();
        } catch (Exception e) {
            System.out.println(description + "(): " + e);
        }
    }

    static void test(String msg, List<String> list) {
        System.out.println("--- " + msg + " ---");
        Collection<String> c = list;
        Collection<String> subList = list.subList(1, 8);
        // Copy of the sublist:
        Collection<String> c2 = new ArrayList<>(subList);
        check("retainAll", () -> c.retainAll(c2));
        check("removeAll", () -> c.removeAll(c2));
        check("clear", () -> c.clear());
        check("add", () -> c.add("X"));
        check("addAll", () -> c.addAll(c2));
        check("remove", () -> c.remove("C"));
        // The List.set() method modifies the value but doesn't change the size of the data structure:
        check("List.set", () -> list.set(0, "X"));
    }

    public static void main(String[] args) {
        List<String> list = Arrays.asList("A B C D E F G H I J K L".split(" "));
        test("Modifiable Copy", new ArrayList<>(list));
        test("Arrays.asList()", list);
        test("unmodifiableList()", Collections.unmodifiableList(new ArrayList<>(list)));
        // test("unmodifiableList()", List.copyOf(list));
    }
}
/* Output:
--- Modifiable Copy ---
--- Arrays.asList() ---
retainAll(): java.lang.UnsupportedOperationException: remove
removeAll(): java.lang.UnsupportedOperationException: remove
clear(): java.lang.UnsupportedOperationException
add(): java.lang.UnsupportedOperationException
addAll(): java.lang.UnsupportedOperationException
remove(): java.lang.UnsupportedOperationException: remove
--- unmodifiableList() ---
retainAll(): java.lang.UnsupportedOperationException
removeAll(): java.lang.UnsupportedOperationException
clear(): java.lang.UnsupportedOperationException
add(): java.lang.UnsupportedOperationException
addAll(): java.lang.UnsupportedOperationException
remove(): java.lang.UnsupportedOperationException
List.set(): java.lang.UnsupportedOperationException
 */
