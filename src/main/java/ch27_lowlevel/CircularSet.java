package ch27_lowlevel;

import java.util.Arrays;

/**
 * @author runningpig66
 * @date 3月12日 周四
 * @time 6:56
 * P.301 §6.5 原子性 §6.5.1 Josh 的序列号
 * Reuses storage so we don't run out of memory
 * <p>
 * add() 方法和 contains() 方法是 synchronized 的，以防止线程冲突。
 * <p>
 * 该类实现了一个固定大小的循环数组，用于存储整数序列号，主要目的是在并发测试中控制内存消耗——通过循环覆盖旧元素的方式，
 * 避免无限增长导致内存溢出。在并发环境下，该类必须保证两个核心方法的线程安全性：add 负责写入，contains 负责读取。
 * 这里的关键在于，即便 contains 只是读取操作，也必须使用 synchronized 进行同步。原因有两点。
 * 第一，内存可见性是双向的。add 方法加锁后，在退出时会触发写屏障（Release 语义），将修改后的数组元素刷新到主内存，并使其他核心中的缓存行失效。
 * 如果 contains 不加锁，调用时可能直接从当前核心的 L1 缓存中读取陈旧数据——即便主内存中的数据早已更新，读线程也无法感知。
 * 给 contains 加上相同的锁，可以在进入方法时触发读屏障（Acquire 语义），强制清空失效缓存队列，
 * 从主内存重新加载数组的最新状态。只有读写两端使用同一把锁，写端的推送才能被读端真正接收。
 * 第二，Java 语言中，volatile 关键字无法解决数组元素的可见性问题。如果将数组声明为 private volatile int[] array;，
 * volatile 仅能保证数组引用本身（即 array 这个变量）的可见性，而无法保证其内部元素 array[i] 的修改对其他线程立即可见。
 * 换句话说，当执行 array[index] = i; 时，这个赋值操作对无锁的读线程而言，依然可能看到过期的值。
 * 因此，仅靠 volatile 修饰数组引用是无效的，必须使用同步机制来确保数组内所有元素的内存可见性。
 * 这个设计遵循了 Brian 的同步法则：当多个线程共享并修改同一个变量（此处是整个数组的内容）时，
 * 所有读取或写入该变量的方法都必须使用相同的锁进行同步，从而同时实现原子性与可见性的闭环。
 */
public class CircularSet {
    private int[] array;
    private int size;
    private int index = 0;

    public CircularSet(int size) {
        this.size = size;
        array = new int[size];
        // Initialize to a value not produced by SerialNumbers:
        Arrays.fill(array, -1);
    }

    public synchronized void add(int i) {
        array[index] = i;
        // Wrap index and write over old elements:
        index = ++index % size;
    }

    public synchronized boolean contains(int val) {
        for (int i = 0; i < size; i++) {
            if (array[i] == val) {
                return true;
            }
        }
        return false;
    }
}
