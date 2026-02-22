package ch24_collectiontopics;

import java.lang.ref.*;
import java.util.LinkedList;

import static onjava.HTMLColors.border;

/**
 * @author runningpig66
 * @date 2月22日 周日
 * @time 16:31
 * P.152 §3.14 持有引用
 * Demonstrates Reference objects
 * <p>
 * 我们是这么做的：使用一个 Reference 对象作为我们和普通引用之间的中介（一个代理）。另外，不能存在指向该对象的其他普通引用
 * （就是没有包在 Reference 对象中的那种）。如果垃圾收集器发现某个对象可以通过一个普通引用访问到，它就不会释放这个对象。
 */
class VeryBig {
    private static final int SIZE = 100000000;
    private long[] la = new long[SIZE];
    private String ident;

    VeryBig(String id) {
        ident = id;
    }

    @Override
    public String toString() {
        return ident;
    }

    @SuppressWarnings("removal")
    @Override
    protected void finalize() {
        System.out.println("Finalizing " + ident);
    }
}

public class References {
    // ReferenceQueue 用于接收那些其内部引用的实际对象已被垃圾回收器（GC）回收的 Reference 包装对象。
    private static ReferenceQueue<VeryBig> rq = new ReferenceQueue<>();

    // 检查引用队列中是否有已被加入的 Reference 对象。当 Reference 包装的实际对象（referent）被 GC 回收后，
    // 该 Reference 本身会被加入此队列。此时调用 inq.get() 将返回 null，因为原对象已被销毁。
    public static void checkQueue() {
        Reference<? extends VeryBig> inq = rq.poll();
        if (inq != null) {
            System.out.println("In queue: " + inq.get());
        }
    }

    public static void main(String[] args) {
        int size = 10;
        // Or, choose size via the command line:
        if (args.length > 0) {
            size = Integer.valueOf(args[0]);
        }

        // 测试 SoftReference（软引用）：
        // List 集合持有的是对 SoftReference 对象的强引用，因此 Reference 包装层本身不会被回收。
        // 但 SoftReference 内部引用的 VeryBig 对象是“软可达（Softly Reachable）”的。
        // 当 JVM 遭遇内存不足（即将抛出 OOM）时，垃圾回收器会自动清除这些软引用，回收 VeryBig 对象。
        LinkedList<SoftReference<VeryBig>> sa = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            // 在构造函数中传入 ReferenceQueue (rq)，是为了在 VeryBig 对象被回收后，JVM 会将对应的 SoftReference 对象自动加入到该队列中。
            // 实际工程中，通常会开辟后台线程监控该队列，并从集合（如 LinkedList）中移除这些失效的引用对象，以防内存泄漏。
            sa.add(new SoftReference<>(new VeryBig("Soft " + i), rq));
            System.out.println("Just created: " + sa.getLast());
            checkQueue();
        }
        border();

        // 测试 WeakReference（弱引用）：
        // 弱引用的生命周期比软引用更短。当垃圾回收器运行时，无论当前可用内存空间是否足够，只要发现对象仅具有弱引用，就会立即将其回收。
        // 在日志中表现为对象被频繁回收，其对应的 Reference 对象快速进入引用队列。
        LinkedList<WeakReference<VeryBig>> wa = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            wa.add(new WeakReference<>(new VeryBig("Weak " + i), rq));
            System.out.println("Just created: " + wa.getLast());
            checkQueue();
        }
        SoftReference<VeryBig> s = new SoftReference<>(new VeryBig("Soft"));
        WeakReference<VeryBig> w = new WeakReference<>(new VeryBig("Weak"));
        // 建议 JVM 执行一次全局垃圾回收，清理上一阶段残留的弱引用对象。
        System.gc();
        border();

        // 测试 PhantomReference（虚引用）：
        // 虚引用是最弱的引用类型。调用其 get() 方法始终返回 null，代码中无法通过虚引用获取到对象实例。
        // 虚引用必须与 ReferenceQueue 联合使用。它的主要作用在于跟踪对象被垃圾回收的活动。
        // 当对象的 finalize() 方法执行完毕，且内存即将被彻底回收时，虚引用才会被加入队列。
        // 虚引用常被用作 finalize() 方法的一种更安全、可控的替代方案，用于执行堆外内存释放等底层资源清理工作。
        LinkedList<PhantomReference<VeryBig>> pa = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            pa.add(new PhantomReference<>(new VeryBig("Phantom " + i), rq));
            System.out.println("Just created: " + pa.getLast());
            checkQueue();
        }
    }
}
/* Output: （注：created 行末的序号不在输出结果中）
Just created: java.lang.ref.SoftReference@1fb3ebeb 0
Just created: java.lang.ref.SoftReference@1218025c 1
Just created: java.lang.ref.SoftReference@816f27d 2
Just created: java.lang.ref.SoftReference@87aac27 3
Just created: java.lang.ref.SoftReference@3e3abc88 4
Just created: java.lang.ref.SoftReference@6ce253f1 5
Just created: java.lang.ref.SoftReference@53d8d10a 6
Just created: java.lang.ref.SoftReference@e9e54c2 7
Just created: java.lang.ref.SoftReference@65ab7765 8
Finalizing Soft 0
Finalizing Soft 8
Finalizing Soft 1
Finalizing Soft 2
Finalizing Soft 3
Finalizing Soft 4
Finalizing Soft 5
Finalizing Soft 7
Finalizing Soft 6
Just created: java.lang.ref.SoftReference@1b28cdfa 9
In queue: null
******************************
Just created: java.lang.ref.WeakReference@34a245ab 0
In queue: null
Finalizing Weak 0
Just created: java.lang.ref.WeakReference@7cc355be 1
In queue: null
Just created: java.lang.ref.WeakReference@6e8cf4c6 2
In queue: null
Just created: java.lang.ref.WeakReference@12edcd21 3
In queue: null
Just created: java.lang.ref.WeakReference@34c45dca 4
In queue: null
Just created: java.lang.ref.WeakReference@52cc8049 5
In queue: null
Just created: java.lang.ref.WeakReference@5b6f7412 6
In queue: null
Finalizing Weak 1
Finalizing Weak 6
Finalizing Weak 2
Finalizing Weak 3
Finalizing Weak 4
Finalizing Weak 5
Finalizing Soft 9
Just created: java.lang.ref.WeakReference@27973e9b 7
In queue: null
Just created: java.lang.ref.WeakReference@312b1dae 8
Finalizing Weak 7
In queue: null
Just created: java.lang.ref.WeakReference@7530d0a 9
In queue: null
******************************
Finalizing Weak 8
Finalizing Weak
Finalizing Weak 9
Just created: java.lang.ref.PhantomReference@9807454 0
In queue: null
Just created: java.lang.ref.PhantomReference@3d494fbf 1
In queue: null
Just created: java.lang.ref.PhantomReference@1ddc4ec2 2
In queue: null
Just created: java.lang.ref.PhantomReference@133314b 3
In queue: null
Just created: java.lang.ref.PhantomReference@b1bc7ed 4
In queue: null
Just created: java.lang.ref.PhantomReference@7cd84586 5
In queue: null
Just created: java.lang.ref.PhantomReference@30dae81 6
In queue: null
Just created: java.lang.ref.PhantomReference@1b2c6ec2 7
In queue: null
Finalizing Phantom 0
Finalizing Phantom 7
Finalizing Phantom 1
Finalizing Phantom 2
Finalizing Phantom 3
Finalizing Phantom 4
Finalizing Phantom 5
Finalizing Phantom 6
Finalizing Soft
Just created: java.lang.ref.PhantomReference@4edde6e5 8
In queue: null
Finalizing Phantom 8
Just created: java.lang.ref.PhantomReference@70177ecd 9
In queue: null
 */
