package ch24_collectiontopics;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Supplier;

/**
 * @author runningpig66
 * @date 2月20日 周五
 * @time 7:04
 * P.136 §3.11 Queue §3.11.2 Deque
 * Very basic test of Deques
 * <p>
 * Deque（双端队列）像一个队列，但是可以在任意一端添加和移除元素。
 * Java 6 为 Deque 添加了一个显式的接口。下面通过实现了该接口的类来测试最基本的 Deque 方法：
 * 注意，当 LinkedBlockingDeque 添加元素时，它会在达到其大小限制时停止，然后忽略后续的 offer。
 */
class CountString implements Supplier<String> {
    private int n = 0;

    CountString() {
    }

    CountString(int start) {
        n = start;
    }

    @Override
    public String get() {
        return Integer.toString(n++);
    }
}

// ...First：操作队列的头部（左边/前端），...Last：操作队列的尾部（右边/后端）。
// 这组 offer / peek / poll 方法家族在 Java 集合体系中的共同设计特征是：
// 通过特殊的返回值（false 或 null）来处理边界情况，而不是像 add / get / remove 那样直接抛出异常。
public class SimpleDeques {
    static void test(Deque<String> deque) {
        CountString s1 = new CountString(), s2 = new CountString(20);
        for (int n = 0; n < 8; n++) {
            // 将指定元素插入双端队列的头部（若受容量限制无法插入则返回 false，不抛出异常）
            deque.offerFirst(s1.get());
            // 将指定元素插入双端队列的尾部（行为等同于常规的 offer()，受容量限制时返回 false）
            deque.offerLast(s2.get()); // Same as offer()
        }
        System.out.println(deque);
        String result = "";
        while (deque.size() > 0) {
            // 获取但不移除双端队列的第一个元素（若队列为空则返回 null，不抛出异常）
            System.out.print(deque.peekFirst() + " ");
            // 获取并移除双端队列的第一个元素（若队列为空则返回 null，不抛出异常）
            result += deque.pollFirst() + " ";
            // 获取但不移除双端队列的最后一个元素（若队列为空则返回 null，不抛出异常）
            System.out.print(deque.peekLast() + " ");
            // 获取并移除双端队列的最后一个元素（若队列为空则返回 null，不抛出异常）
            result += deque.pollLast() + " ";
        }
        System.out.println("\n" + result);
    }

    public static void main(String[] args) {
        int count = 10;
        System.out.println("LinkedList");
        // 基于双向链表实现的基础双端队列，适用于单线程下的常规双端操作。
        // LinkedList：底层是标准的双向链表。每次插入元素都需要 new 一个节点对象，存在一定的内存碎片和对象分配开销。在单线程下表现中规中矩。
        test(new LinkedList<>());
        System.out.println("ArrayDeque");
        // 基于动态连续数组实现的基础双端队列，内存与访问效率极高，推荐用于单线程替代 Stack。
        // ArrayDeque：底层是动态数组。由于数组在内存中是连续的，它的 CPU 缓存命中率极高，且不需要频繁分配节点对象。
        // 在单线程环境下，ArrayDeque 的入队/出队性能通常全面碾压 LinkedList。官方文档明确推荐使用它来替代古老的 Stack 类。
        test(new ArrayDeque<>());
        System.out.println("LinkedBlockingDeque");
        // 支持设定固定容量（有界）的线程安全阻塞双端队列，常用于需控制内存占用的生产者-消费者模型。
        // LinkedBlockingDeque：这是一个重量级的并发阻塞队列。它内部使用了独占锁（ReentrantLock）来保证线程安全。
        // 它的核心特性是支持有界（设定最大容量）。当队列满了，插入线程可以被阻塞等待；队列空了，读取线程可以被阻塞等待。
        test(new LinkedBlockingDeque<>(count));
        System.out.println("ConcurrentLinkedDeque");
        // 基于无锁算法（CAS）实现的无界线程安全双端队列，适用于追求极致性能的超高并发读写场景。
        // ConcurrentLinkedDeque：这是一个极度追求性能的无锁并发队列。它底层使用了极其复杂的 CAS（Compare-And-Swap）
        // 算法来保证线程安全，完全不加锁。它通常是无界的，适用于读写极度频繁的超高并发场景。
        test(new ConcurrentLinkedDeque<>());
    }
}
/* Output
LinkedList
[7, 6, 5, 4, 3, 2, 1, 0, 20, 21, 22, 23, 24, 25, 26, 27]
7 27 6 26 5 25 4 24 3 23 2 22 1 21 0 20
7 27 6 26 5 25 4 24 3 23 2 22 1 21 0 20
ArrayDeque
[7, 6, 5, 4, 3, 2, 1, 0, 20, 21, 22, 23, 24, 25, 26, 27]
7 27 6 26 5 25 4 24 3 23 2 22 1 21 0 20
7 27 6 26 5 25 4 24 3 23 2 22 1 21 0 20
LinkedBlockingDeque
[4, 3, 2, 1, 0, 20, 21, 22, 23, 24]
4 24 3 23 2 22 1 21 0 20
4 24 3 23 2 22 1 21 0 20
ConcurrentLinkedDeque
[7, 6, 5, 4, 3, 2, 1, 0, 20, 21, 22, 23, 24, 25, 26, 27]
7 27 6 26 5 25 4 24 3 23 2 22 1 21 0 20
7 27 6 26 5 25 4 24 3 23 2 22 1 21 0 20
 */
