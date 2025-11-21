package ch16_validating;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author runningpig66
 * @date 2025/11/19 周三
 * @time 23:26
 * 代码清单 P.491 前置条件：DbC(Design by Contract) 契约式设计 + 单元测试
 * <p>
 * 为 CircularQueue.java 类创建 JUnit 测试：
 * 注意，通过将 DbC 与单元测试相结合，你不仅可以利用它们各自的优点，而直还有一条迁移路径————
 * 你可以将一些 DbC 测试移动到单元测试中，而不是简单地禁用它们，这样你仍然能保证有某些层次的测试。
 */
public class CircularQueueTest {
    private CircularQueue queue = new CircularQueue(10);
    private int i = 0;

    @BeforeEach
    public void initialize() {
        while (i < 5) { // Pre-load with some data
            queue.put(Integer.toString(i++));
        }
    }

    // Support methods:
    private void showFullness() {
        Assertions.assertTrue(queue.full());
        Assertions.assertFalse(queue.empty());
        System.out.println(queue.dump());
    }

    private void showEmptiness() {
        Assertions.assertFalse(queue.full());
        Assertions.assertTrue(queue.empty());
        System.out.println(queue.dump());
    }

    @Test
    public void full() {
        System.out.println("testFull");
        System.out.println(queue.dump());
        System.out.println(queue.get());
        System.out.println(queue.get());
        while (!queue.full()) {
            queue.put(Integer.toString(i++));
        }
        String msg = "";
        try {
            queue.put("");
        } catch (CircularQueueException e) {
            msg = e.getMessage();
            System.out.println(msg);
        }
        Assertions.assertEquals("put() into full CircularQueue", msg);
        showFullness();
    }

    @Test
    public void empty() {
        System.out.println("testEmpty");
        while (!queue.empty()) {
            System.out.println(queue.get());
        }
        String msg = "";
        try {
            queue.get();
        } catch (CircularQueueException e) {
            msg = e.getMessage();
            System.out.println(msg);
        }
        Assertions.assertEquals("get() from empty CircularQueue", msg);
        showEmptiness();
    }

    @Test
    public void nullPut() {
        System.out.println("testNullPut");
        String msg = "";
        try {
            queue.put(null);
        } catch (CircularQueueException e) {
            msg = e.getMessage();
            System.out.println(msg);
        }
        Assertions.assertEquals("put() null item", msg);
    }

    @Test
    public void circularity() {
        System.out.println("testCircularity");
        while (!queue.full()) {
            queue.put(Integer.toString(i++));
        }
        showFullness();
        Assertions.assertTrue(queue.isWrapped());
        while (!queue.empty()) {
            System.out.println(queue.get());
        }
        showEmptiness();
        while (!queue.full()) {
            queue.put(Integer.toString(i++));
        }
        showFullness();
        while (!queue.empty()) {
            System.out.println(queue.get());
        }
        showEmptiness();
    }
}
/* Output:
testNullPut
put() null item
testCircularity
in = 0, out = 0, full() = true, empty() = false, CircularQueue = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
0
1
2
3
4
5
6
7
8
9
in = 0, out = 0, full() = false, empty() = true, CircularQueue = [null, null, null, null, null, null, null, null, null, null]
in = 0, out = 0, full() = true, empty() = false, CircularQueue = [10, 11, 12, 13, 14, 15, 16, 17, 18, 19]
10
11
12
13
14
15
16
17
18
19
in = 0, out = 0, full() = false, empty() = true, CircularQueue = [null, null, null, null, null, null, null, null, null, null]
testFull
in = 5, out = 0, full() = false, empty() = false, CircularQueue = [0, 1, 2, 3, 4, null, null, null, null, null]
0
1
put() into full CircularQueue
in = 2, out = 2, full() = true, empty() = false, CircularQueue = [10, 11, 2, 3, 4, 5, 6, 7, 8, 9]
testEmpty
0
1
2
3
4
get() from empty CircularQueue
in = 5, out = 5, full() = false, empty() = true, CircularQueue = [null, null, null, null, null, null, null, null, null, null]
 */
