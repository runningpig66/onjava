package ch16_validating;

import java.util.Arrays;

/**
 * @author runningpig66
 * @date 2025/11/19 周三
 * @time 15:16
 * 代码清单 P.489 前置条件：DbC(Design by Contract) 契约式设计 + 单元测试
 * Demonstration of Design by Contract (DbC)
 * <p>
 * 以下示例演示了将契约式设计中的概念与单元测试相结合的效力。它通过“循环”数组实现了一个小型的先进先出 FIFO 队列。
 * 所谓的“循环”数组，就是以循环方式使用的数组，当到达数组末尾时，继续访问就又回到了数组的开头。
 * 我们可以为这个队列做一些契约性的定义。
 * 1. 前置条件（对 put() 来说）：不允许将空元素添加到队列中。
 * 2. 前置条件（对 put() 来说）：将元素放人已满的队列是非法的。
 * 3. 前置条件（对 get() 来说）：尝试从空队列中获取元素是非法的。
 * 4. 后置条件（对 get() 来说）：不能从数组中获取空元素。
 * 5. 不变项：队列中包含对象的区域不能有任何空元素。
 * 6. 不变项：队列中不包含对象的区域必须只能有空值。
 * 下面是实现这些规则的一种方式：通过显式方法调用来实现每种类型的 DbC 元素。
 * <p>
 * notes: CircularQueue1.md & CircularQueue2.md
 * 按照不变项 5, 是否需要实现 [满元素区间检查每个存在]
 * 按照不变项 6, 是否需要实现 [零元素区间检查每个为空]
 */
public class CircularQueue {
    private Object[] data;
    private int
            in = 0, // Next available storage space 表示在数组中存储下一个对象时的位置
            out = 0; // Next gettable object 表示要获取的下一个对象的位置
    // Has it wrapped around the circular queue?
    private boolean wrapped = false;

    public CircularQueue(int size) {
        data = new Object[size];
        // Must be true after construction:
        assert invariant();
    }

    public boolean empty() {
        // 当 out “绕一圈回来追上” in 时，wrapped = false;
        return !wrapped && in == out;
    }

    public boolean full() {
        // 当 in “绕一圈回来追上” out 时，wrapped = true;
        return wrapped && in == out;
    }

    public boolean isWrapped() {
        return wrapped;
    }

    public void put(Object item) {
        precondition(item != null, "put() null item");
        precondition(!full(), "put() into full CircularQueue");
        assert invariant();
        data[in++] = item;
        if (in >= data.length) {
            in = 0;
            // 当 in “绕一圈回来追上” out 时，wrapped = true;
            wrapped = true;
        }
        assert invariant();
    }

    public Object get() {
        precondition(!empty(), "get() from empty CircularQueue");
        assert invariant();
        Object returnVal = data[out];
        data[out] = null;
        out++;
        if (out >= data.length) {
            out = 0;
            // 当 out “绕一圈回来追上” in 时，wrapped = false;
            wrapped = false;
        }
        assert postcondition(returnVal != null, "Null item in CircularQueue");
        assert invariant();
        return returnVal;
    }

    // Design-by-contract support methods:
    // 注意 precondition() 返回 void. 因为它不与 assert 一起使用。如前所述，你通常要在代码中保留前置条件。
    // 通过将它们包装在一个 precondition() 方法调用中，可以很方便地减少或关闭这些前置条件。
    private static void precondition(boolean cond, String msg) {
        if (!cond) {
            throw new CircularQueueException(msg);
        }
    }

    // 后置条件 postcondition() 和 不变式 invariant() 都返回一个布尔值，因此它们可以在 assert 语句中使用。
    // 之后如果出于性能原因禁用断言，那么就根本不会有方法调用。
    private static boolean postcondition(boolean cond, String msg) {
        if (!cond) {
            throw new CircularQueueException(msg);
        }
        return true;
    }

    private boolean invariant() {
        // Guarantee that no null values are in the region of 'data' that holds objects:
        for (int i = out; i != in; i = (i + 1) % data.length) {
            if (data[i] == null) {
                throw new CircularQueueException("null in CircularQueue");
            }
        }
        // Guarantee that only null values are outside the region of 'data' that holds objects:
        if (full()) {
            return true;
        }
        for (int i = in; i != out; i = (i + 1) % data.length) {
            if (data[i] != null) {
                throw new CircularQueueException("non-null outside of CircularQueue range: " + dump());
            }
        }
        return true;
    }

    public String dump() {
        return "in = " + in +
                ", out = " + out +
                ", full() = " + full() +
                ", empty() = " + empty() +
                ", CircularQueue = " + Arrays.asList(data);
    }
}
