package ch24_collectiontopics;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2月20日 周五
 * @time 6:03
 * P.134 §3.11 Queue [IMP]
 * Compares basic behavior
 * <p>
 * 【Java Queue 兵器谱：11 种队列的宏观分类与核心使用场景】
 * 核心契约：Queue 通常表现为先进先出（FIFO），通过 offer() 放入，peek() 查看，remove() 取出。
 * 1. 基础单线程队列（最普通，无并发保护）
 * - LinkedList (测试 1): 最标准的 FIFO 队列。底层是双向链表。
 * 2. 优先级队列（破坏 FIFO，按元素大小出队）
 * - PriorityQueue (测试 2): 底层是堆结构（树）。每次取出的都是最小/最大的元素，依赖 Comparable。
 * 3. 阻塞队列 (BlockingQueue) —— 【多线程/生产者-消费者模型的神器】
 * 特点：队列满了，生产者线程被阻塞；队列空了，消费者线程被阻塞。
 * - ArrayBlockingQueue (测试 3): 底层是数组，【有界队列】（必须指定固定大小）。
 * - LinkedBlockingQueue (测试 5): 底层是链表，通常作为【无界队列】（也可指定大小）。
 * - PriorityBlockingQueue (测试 6): 具备阻塞功能的 PriorityQueue。
 * 4. 无锁并发队列 (Concurrent) —— 【追求极致并发性能】
 * 特点：基于 CAS 无锁算法实现，性能极高，不会阻塞线程，适合极高并发读写的场景。
 * - ConcurrentLinkedQueue (测试 4): 高并发环境下的标准无锁 FIFO 队列。
 * 5. 双端队列 (Deque) —— 【两头都能进出，可做栈，也可做队列】
 * - ArrayDeque (测试 7): 基于数组的双端队列，单线程下性能极高，【推荐用来替代 Stack】。
 * - ConcurrentLinkedDeque (测试 8): 无锁并发版的双端队列。
 * - LinkedBlockingDeque (测试 9): 阻塞版的双端队列。
 * 6. 极度特殊的“交接”队列
 * - LinkedTransferQueue (测试 10): 生产者放入元素后，可以一直等待，直到消费者把元素拿走才返回。
 * - SynchronousQueue (测试 11): 【容量为 0 的队列】！它本身不存储任何元素。
 * (解释了为什么测试 11 没有任何输出：单线程下，没人接手，offer() 直接失败返回 false，数据根本进不去)。
 */
public class QueueBehavior {
    static Stream<String> strings() {
        return Arrays.stream("one two three four five six seven eight nine ten".split(" "));
    }

    static void test(int id, Queue<String> queue) {
        System.out.print(id + ": ");
        strings().forEach(queue::offer);
        while (queue.peek() != null) {
            System.out.print(queue.remove() + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int count = 10;
        test(1, new LinkedList<>());
        test(2, new PriorityQueue<>());
        test(3, new ArrayBlockingQueue<>(count));
        test(4, new ConcurrentLinkedQueue<>());
        test(5, new LinkedBlockingQueue<>());
        test(6, new PriorityBlockingQueue<>());
        test(7, new ArrayDeque<>());
        test(8, new ConcurrentLinkedDeque<>());
        test(9, new LinkedBlockingDeque<>());
        test(10, new LinkedTransferQueue<>());
        test(11, new SynchronousQueue<>());
    }
}
/* Output:
1: one two three four five six seven eight nine ten
2: eight five four nine one seven six ten three two
3: one two three four five six seven eight nine ten
4: one two three four five six seven eight nine ten
5: one two three four five six seven eight nine ten
6: eight five four nine one seven six ten three two
7: one two three four five six seven eight nine ten
8: one two three four five six seven eight nine ten
9: one two three four five six seven eight nine ten
10: one two three four five six seven eight nine ten
11:
 */
