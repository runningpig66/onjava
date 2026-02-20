package ch24_collectiontopics;

import onjava.HTMLColors;

import java.util.*;

/**
 * @author runningpig66
 * @date 2月16日 周一
 * @time 15:36
 * P.099 §3.2 List 的行为
 * Things you can do with Lists
 * <p>
 * ListIterator 核心机制：光标模型与状态机转换
 * 1. 光标（Cursor）模型：
 * 迭代器的光标不指向任何实际元素，而是始终悬停在两个相邻元素之间。
 * 2. 操作上下文（lastReturned 状态）：
 * 迭代器的修改操作（remove / set）不依赖光标本身，而是依赖于最近一次越过的元素（底层标记为 lastReturned）。
 * - next()：光标向后（右）移动，越过紧邻的后继元素，该元素即成为当前的操作上下文。
 * - previous()：光标向前（左）移动，越过紧邻的前驱元素，该元素即成为当前的操作上下文。
 * 3. 方法的副作用与状态重置：
 * - remove()：移除最近一次获取到的上下文元素。执行后，上下文状态被清空。
 * - set()：替换最近一次获取到的上下文元素。此操作不会清空上下文状态，也不会改变光标位置。
 * - add()：在光标当前位置插入新元素。注意：此操作会立即强制清空当前的上下文状态。
 * 4. 状态机调用规范：
 * - 前置定位限制：在调用 remove() 或 set() 之前，必须先调用一次 next() 或 previous() 以确立操作上下文。
 * - 状态不可复用：调用 remove() 后，上下文状态失效。若需连续操作，必须重新移动光标，否则会抛出 IllegalStateException 异常。
 * - 插入也会导致状态丢失：执行 add() 插入元素后，会重置迭代器状态，后续必须重新移动光标方可执行修改或删除操作。
 * <p>
 * listIterator(n) 会把光标精准地插在“索引为 n 的元素”的正前方。（换句话说，如果你紧接着调用 next()，返回的刚好就是索引为 n 的那个元素）。
 * 如果有 N 个元素，就会有 N + 1 个“缝隙”（包含最开头和最末尾）。光标只能在这 N + 1 个位置安家。
 */
public class ListOps {
    // Create a short list for testing:
    static final List<String> LIST = HTMLColors.LIST.subList(0, 10);
    private static boolean b;
    private static String s;
    private static int i;
    private static Iterator<String> it;
    private static ListIterator<String> lit;

    // 每个 List 都能做的事情
    public static void basicTest(List<String> a) {
        a.add(1, "x"); // Add at location 1
        a.add("x"); // Add at end
        // Add a collection:
        a.addAll(LIST);
        // Add a collection starting at location 3:
        a.addAll(3, LIST);
        b = a.contains("1"); // Is it in there?
        // Is the entire collection in there?
        b = a.containsAll(LIST);
        // Lists allow random access, which is cheap for ArrayList, expensive for LinkedList:
        s = a.get(1); // Get (typed) object at location 1
        i = a.indexOf("1"); // Tell index of object
        b = a.isEmpty(); // Any elements inside?
        it = a.iterator(); // Ordinary Iterator
        lit = a.listIterator(); // ListIterator
        lit = a.listIterator(3); // Start at location 3
        i = a.lastIndexOf("1"); // Last match
        a.remove(1); // Remove location 1
        a.remove("3"); // Remove this object
        a.set(1, "y"); // Set location 1 to "y"
        // Keep everything that's in the argument (the intersection of the two sets):
        a.retainAll(LIST);
        // Remove everything that's in the argument:
        a.removeAll(LIST);
        i = a.size(); // How big is it?
        a.clear(); // Remove all elements
    }

    // 在 Iterator 上移动
    public static void iterMotion(List<String> a) {
        ListIterator<String> it = a.listIterator();
        b = it.hasNext();
        b = it.hasPrevious();
        s = it.next();
        i = it.nextIndex();
        s = it.previous();
        i = it.previousIndex();
    }

    // 用 Iterator 修改元素
    public static void iterManipulation(List<String> a) {
        ListIterator<String> it = a.listIterator();
        it.add("47");
        // Must move to an element after add():
        it.next();
        // Remove the element after the new one:
        it.remove();
        // Must move to an element after remove():
        it.next();
        // Change the element after the deleted one:
        it.set("47");
    }

    // 查看 List 操作的效果
    public static void testVisual(List<String> a) {
        System.out.println(a);
        List<String> b = LIST;
        System.out.println("b = " + b);
        a.addAll(b);
        a.addAll(b);
        System.out.println(a);
        // Insert, remove, and replace elements
        // using a ListIterator:
        ListIterator<String> x = a.listIterator(a.size() / 2);
        x.add("one");
        System.out.println(a);
        System.out.println(x.next());
        x.remove();
        System.out.println(x.next());
        x.set("47");
        System.out.println(a);
        // Traverse the list backwards:
        x = a.listIterator(a.size());
        while (x.hasPrevious()) {
            System.out.print(x.previous() + " ");
        }
        System.out.println();
        System.out.println("testVisual finished");
    }

    // There are some things that only LinkedLists can do:
    public static void testLinkedList() {
        LinkedList<String> ll = new LinkedList<>();
        ll.addAll(LIST);
        System.out.println(ll);
        // Treat it like a stack, pushing:
        ll.addFirst("one");
        ll.addFirst("two");
        System.out.println(ll);
        // Like "peeking" at the top of a stack:
        System.out.println(ll.getFirst());
        // Like popping a stack:
        System.out.println(ll.removeFirst());
        System.out.println(ll.removeFirst());
        // Treat it like a queue, pulling elements off the tail end:
        System.out.println(ll.removeLast());
        System.out.println(ll);
    }

    public static void main(String[] args) {
        // Make and fill a new list each time:
        basicTest(new LinkedList<>(LIST));
        basicTest(new ArrayList<>(LIST));
        iterMotion(new LinkedList<>(LIST));
        iterMotion(new ArrayList<>(LIST));
        iterManipulation(new LinkedList<>(LIST));
        iterManipulation(new ArrayList<>(LIST));
        testVisual(new LinkedList<>(LIST));
        testLinkedList();
    }
}
/* Output:
[AliceBlue, AntiqueWhite, Aquamarine, Azure, Beige, Bisque, Black, BlanchedAlmond, Blue, BlueViolet]
b = [AliceBlue, AntiqueWhite, Aquamarine, Azure, Beige, Bisque, Black, BlanchedAlmond, Blue, BlueViolet]
[AliceBlue, AntiqueWhite, Aquamarine, Azure, Beige, Bisque, Black, BlanchedAlmond, Blue, BlueViolet, AliceBlue, AntiqueWhite, Aquamarine, Azure, Beige, Bisque, Black, BlanchedAlmond, Blue, BlueViolet, AliceBlue, AntiqueWhite, Aquamarine, Azure, Beige, Bisque, Black, BlanchedAlmond, Blue, BlueViolet]
[AliceBlue, AntiqueWhite, Aquamarine, Azure, Beige, Bisque, Black, BlanchedAlmond, Blue, BlueViolet, AliceBlue, AntiqueWhite, Aquamarine, Azure, Beige, one, Bisque, Black, BlanchedAlmond, Blue, BlueViolet, AliceBlue, AntiqueWhite, Aquamarine, Azure, Beige, Bisque, Black, BlanchedAlmond, Blue, BlueViolet]
Bisque
Black
[AliceBlue, AntiqueWhite, Aquamarine, Azure, Beige, Bisque, Black, BlanchedAlmond, Blue, BlueViolet, AliceBlue, AntiqueWhite, Aquamarine, Azure, Beige, one, 47, BlanchedAlmond, Blue, BlueViolet, AliceBlue, AntiqueWhite, Aquamarine, Azure, Beige, Bisque, Black, BlanchedAlmond, Blue, BlueViolet]
BlueViolet Blue BlanchedAlmond Black Bisque Beige Azure Aquamarine AntiqueWhite AliceBlue BlueViolet Blue BlanchedAlmond 47 one Beige Azure Aquamarine AntiqueWhite AliceBlue BlueViolet Blue BlanchedAlmond Black Bisque Beige Azure Aquamarine AntiqueWhite AliceBlue
testVisual finished
[AliceBlue, AntiqueWhite, Aquamarine, Azure, Beige, Bisque, Black, BlanchedAlmond, Blue, BlueViolet]
[two, one, AliceBlue, AntiqueWhite, Aquamarine, Azure, Beige, Bisque, Black, BlanchedAlmond, Blue, BlueViolet]
two
two
one
BlueViolet
[AliceBlue, AntiqueWhite, Aquamarine, Azure, Beige, Bisque, Black, BlanchedAlmond, Blue]
 */
