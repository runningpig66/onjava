package ch26_concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author runningpig66
 * @date 3月6日 周五
 * @time 0:35
 * P.260 §5.11 死锁
 * <p>
 * 下面这个 StickHolder（筷子持有者）类将一个 Chopstick（筷子）类保存在一个长度为 1 的 BlockingQueue 中，以进行管理。
 * BlockingQueue 是一种线程安全的集合，专门用于并发程序，如果调用 take() 且队列为空，它就会阻塞（等待）。
 * 一旦新的元素被放入队列，阻塞就会被解除，并会返回该元素值：
 * <p>
 * 简单起见，Chopstick 不会由 StickHolder 实际生成，而仅在类中是私有的。
 * 如果你调用 pickUp()（拿起）且筷子当前不可用，pickUp() 会一直阻塞，
 * 直到筷子被其他 Philosopher 通过调用 putDown()（放下）被返回。
 * 注意本类中所有的线程安全性都是由 BlockingQueue 来保证的。
 */
public class StickHolder {
    private static class Chopstick {
    }

    private Chopstick stick = new Chopstick();
    private BlockingQueue<Chopstick> holder = new ArrayBlockingQueue<>(1);

    public StickHolder() {
        putDown();
    }

    public void pickUp() {
        try {
            holder.take(); // Blocks if unavailable
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void putDown() {
        try {
            holder.put(stick);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
