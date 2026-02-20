package ch24_collectiontopics;

import org.jspecify.annotations.NonNull;

import java.util.PriorityQueue;

/**
 * @author runningpig66
 * @date 2月20日 周五
 * @time 6:37
 * P.135 §3.11 Queue §3.11.1 优先级队列
 * A more complex use of PriorityQueue
 * <p>
 * 考虑一个待办清单（to-do list），其中的每个对象都包含一个 String，以及分别表示主要优先级和次要优先级的值。
 * 清单的顺序通过实现 Comparable 来控制；这里演示了如何通过优先级队列实现对列表条自的自动排序。
 */
class ToDoItem implements Comparable<ToDoItem> {
    private char primary;
    private int secondary;
    private String item;

    ToDoItem(String td, char pri, int sec) {
        primary = pri;
        secondary = sec;
        item = td;
    }

    @Override
    public int compareTo(@NonNull ToDoItem arg) {
        if (primary > arg.primary)
            return +1;
        if (primary == arg.primary)
            if (secondary > arg.secondary)
                return +1;
            else if (secondary == arg.secondary)
                return 0;
        return -1;
    }

    @Override
    public String toString() {
        return Character.toString(primary) + secondary + ": " + item;
    }
}

public class ToDoList {
    public static void main(String[] args) {
        PriorityQueue<ToDoItem> toDo = new PriorityQueue<>();
        toDo.add(new ToDoItem("Empty trash", 'C', 4));
        toDo.add(new ToDoItem("Feed dog", 'A', 2));
        toDo.add(new ToDoItem("Feed bird", 'B', 7));
        toDo.add(new ToDoItem("Mow lawn", 'C', 3));
        toDo.add(new ToDoItem("Water lawn", 'A', 1));
        toDo.add(new ToDoItem("Feed cat", 'B', 1));
        while (!toDo.isEmpty()) {
            System.out.println(toDo.remove());
        }
    }
}
/* Output:
A1: Water lawn
A2: Feed dog
B1: Feed cat
B7: Feed bird
C3: Mow lawn
C4: Empty trash
 */
