package ch27_lowlevel;

import onjava.Nap;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Queue;
import java.util.SplittableRandom;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author runningpig66
 * @date 3月13日 周五
 * @time 18:47
 * P.313 §6.7 库组件 §6.7.2 优先级阻塞队列 PriorityBlockingQueue
 * <p>
 * 这基本上是一种能够阻塞读取操作的优先队列。下面的示例中为 Prioritized 指定了优先级数值。
 * 一些 Producer 任务的实例将 Prioritized 对象插入 PriorityBlockingQueue，不过在插入操作之间加入了随机的延迟。然后，
 * 单个 Consumer 任务在执行 take() 时会呈现出多个选项，PriorityBlockingQueue 则将此时具有最高优先级的 Prioritized 对象传给它。
 */
class Prioritized implements Comparable<Prioritized> {
    // Prioritized 中的 static counter 是 AtomicInteger 类型。这是必要的，因为同时有多个 Producer 在并行运行，
    // 如果不使用 AtomicInteger，就会看到重复的 id 数值，这个问题在本书第 5 章 5.12 节中介绍过。
    private static AtomicInteger counter = new AtomicInteger();
    private final int id = counter.getAndIncrement();
    private final int priority;
    /* CopyOnWriteArrayList: 线程安全的 ArrayList 变体，底层基于写时复制（Copy-On-Write）机制实现。
     * 当执行 add、set 或 remove 等修改操作时，容器不会直接修改当前数组，而是先获取内部锁，
     * 将现有数组完整复制出一份新副本。所有的写入逻辑均在新副本上独立完成，最后再将底层数组引用指向该副本并释放锁。
     * 这种设计实现了彻底的读写分离。其读取操作（如 get 和迭代遍历）完全无锁，因为线程读取的始终是数组在某一时刻的历史快照。
     * 得益于此，它的迭代器永远不会抛出 ConcurrentModificationException 异常。但也正因如此，它只能提供弱一致性保证，
     * 迭代器无法实时感知到在其创建之后发生的任何修改。这是一种典型的用空间换取时间的策略，每次写入都会产生较高的内存复制开销，
     * 因此仅适用于读操作远多于写操作、且对数据的绝对实时性要求不高的并发场景。
     * 在本示例中，使用该容器能够确保多个 Producer 线程在并发创建 Prioritized 对象时，
     * 安全无冲突地将实例引用依次写入 sequence 集合，避免了普通集合在多线程并发扩容时可能引发的元素覆盖或数组越界问题。
     */
    // 和上一个示例一样，List sequence 记住了 Prioritized 对象的创建顺序，以便与实际的执行顺序进行比较。
    private static List<Prioritized> sequence = new CopyOnWriteArrayList<>();

    Prioritized(int priority) {
        this.priority = priority;
        sequence.add(this);
    }

    @Override
    public int compareTo(@NonNull Prioritized arg) {
        // return priority < arg.priority ? 1 : (priority > arg.priority ? -1 : 0);
        return Integer.compare(arg.priority, priority);
    }

    @Override
    public String toString() {
        return String.format("[%d] Prioritized %d", priority, id);
    }

    public void displaySequence() {
        int count = 0;
        for (Prioritized pt : sequence) {
            System.out.printf("(%d:%d)", pt.id, pt.priority);
            if (++count % 5 == 0) {
                System.out.println();
            }
        }
    }

    // EndSentinel 是一种特殊类型，它会告诉 Consumer 进行关闭。
    public static class EndSentinel extends Prioritized {
        EndSentinel() {
            super(-1);
        }
    }
}

class Producer implements Runnable {
    /* 全局安全的随机数种子分配器。作用与机制：由于本类将被实例化为多个独立的 Producer 并在多线程并发执行，
     * 必须确保每个实例内部的随机数生成器拥有不同的初始种子，否则它们将生成相同的任务序列。声明为 static 使得该变量在所有 Producer 实例间共享，
     * 使用 AtomicInteger 则利用其底层的 CAS 机制保证了多线程并发获取并增加种子时的原子性和可见性，确保每个 Producer 获取到的种子绝对唯一。
     */
    // Producer 用 AtomicInteger 来为 SplittableRandom 提供种子（seed），这样不同的 Producer 就可以生成不同的序列。
    // 因为多个 Producer 是并行创建的，所以这很有必要，否则构造过程就不是线程安全的了。
    private static AtomicInteger seed = new AtomicInteger(47);
    /* SplittableRandom: 高性能、非线程安全的伪随机数生成器。为什么不使用 java.util.Random?
     * 1. 算法独立性：普通的 Random 在初始种子相近时（如本例中的 47, 57, 67），早期生成的随机数序列可能具有强相关性。
     * 而 SplittableRandom 采用 SplitMix 算法，即使种子极其接近，也能保证各实例生成的随机数流在统计学上绝对独立。
     * 2. 线程隔离设计：SplittableRandom 内部无任何锁机制（非线程安全），专为单一线程内的高速 Stream 流计算设计。
     * 注意：由于它是作为实例变量（非 static）存在的，每个 Producer 线程都独享一个实例，因此在此上下文中不存在并发竞争问题，完美契合了该类的设计初衷。
     */
    private SplittableRandom rand = new SplittableRandom(seed.getAndAdd(10));
    private Queue<Prioritized> queue;

    Producer(Queue<Prioritized> q) {
        queue = q;
    }

    @Override
    public void run() {
        rand.ints(10, 0, 20)
                .mapToObj(Prioritized::new)
                .peek(p -> new Nap(rand.nextDouble() / 10)) // 使当前生产者线程，随机休眠 0 到 100 毫秒。
                .forEach(queue::add);
        queue.add(new Prioritized.EndSentinel());
    }
}

/* 消费者任务逻辑（多生产者 - 单消费者模型核心组件）
 * 这是一个典型的“削峰填谷”架构模型，在真实工业场景中，该模型隐藏着以下三个极其重要的并发与架构设计考量：
 * 1. 产能过剩与优先级漏斗（吞吐量设计）
 * 在本例中，3 个生产者的并发生产速度远大于 1 个消费者的处理速度。这并非设计缺陷，而是 PriorityBlockingQueue 能够发挥作用的“先决条件”。
 * 如果消费速度大于生产速度（供不应求），队列中将始终处于空置或极少元素状态，所谓的“按优先级排序”将毫无意义。
 * 只有人为制造“供大于求”的任务积压（拥堵），消费者才能在每次 take() 时，从大量堆积的任务中挑选出优先级最高的那一个。
 * 2. 无界阻塞队列的压力转移（背压缺失）
 * PriorityBlockingQueue 属于无界队列。这意味着无论积压多少任务，生产者的 add() 操作永远不会被阻塞，它们可以毫无顾忌地倾泻任务。
 * 系统的所有内存压力和处理压力全部分摊到了消费者这一端，这要求消费者必须具备极高的稳定性和异常处理能力，否则容易引发 OOM（内存溢出）。
 * 3. 毒丸模式（Poison Pill）在多生产者环境下的致命陷阱
 * 本例采用在数据流末尾插入特殊标志（EndSentinel，优先级为 -1）的方式来通知消费者停机。
 * 但在多生产者并行且耗时极度不均的极端场景下，存在严重的架构漏洞：由于各线程因随机休眠产生了严重的“时序漂移（Thread Drift）”，
 * 假设 P1 极快而 P2、P3 极慢，P1 瞬间完成生产并投入了 -1 标志。当消费者快速消费完 P1 的正常任务后，P2、P3 的正常任务还未入队，
 * 队列中只剩下 P1 投入的 -1 标志。消费者会提前取出该标志并结束循环（死亡），直接导致主线程放行，JVM 强制关闭，P2 和 P3 尚未执行的任务瞬间丢失。
 * 工业级修正方案：在多生产者模型中，生产者绝不负责发送停机信号。必须由主线程通过 CountDownLatch 等同步工具，
 * 死死监控所有生产者线程。只有确信所有生产者均已彻底完工并退出后，才由主线程统一向队列中投入唯一的一颗“毒丸”，安全关闭消费者。
 */
class Consumer implements Runnable {
    private PriorityBlockingQueue<Prioritized> q;
    private SplittableRandom rand = new SplittableRandom(47);

    Consumer(PriorityBlockingQueue<Prioritized> q) {
        this.q = q;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Prioritized pt = q.take();
                System.out.println(pt);
                if (pt instanceof Prioritized.EndSentinel) {
                    pt.displaySequence();
                    break;
                }
                new Nap(rand.nextDouble() / 10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

public class PriorityBlockingQueueDemo {
    public static void main(String[] args) {
        /* 全局共享核心组件：优先级阻塞队列 (PriorityBlockingQueue) 底层数据结构与并发特性：
         * 1. 优先级调度（非 FIFO 结构）：
         * 底层基于数组实现的二叉堆（Binary Heap）。它不保证元素按插入的先后顺序出队，
         * 而是严格依赖元素实现的 Comparable 接口（或外部 Comparator）进行动态排序。
         * 每次执行出队操作时，内部会进行树结构微调（下沉），始终确保返回当前优先级最高的元素。
         * 2. 无界容量（Unbounded Capacity）：
         * 理论最大容量仅受限于 JVM 堆内存（接近 Integer.MAX_VALUE）。
         * 关键物理约束：由于队列无界，生产者的写入操作（add/put/offer）永远不会发生阻塞。
         * 在极高并发下，必须从系统架构层面监控消费端的吞吐能力，否则极易引发严重的内存积压甚至 OOM。
         * 3. 线程安全与阻塞唤醒（Blocking Mechanism）：
         * 内部封装了独占锁（ReentrantLock）与条件变量（Condition）以保证多线程读写的原子性。
         * 当且仅当队列为空时，消费者的读取操作（take）会被底层主动挂起，让出 CPU 执行权，直到生产者向队列中成功插入新元素并发出唤醒信号。
         */
        // Producer 和 Consumer 通过 PriorityBlockingQueue 相互关联起来。由于队列的阻塞特性提供了所有必要的同步控制，所以请注意，
        // 这里不需要任何显式的同步控制——不用考虑在读取该队列的时候队列中是否含有元素，因为该队列在没有元素的时候会阻塞住读取者。
        PriorityBlockingQueue<Prioritized> queue = new PriorityBlockingQueue<>();
        CompletableFuture.runAsync(new Producer(queue));
        CompletableFuture.runAsync(new Producer(queue));
        CompletableFuture.runAsync(new Producer(queue));
        CompletableFuture.runAsync(new Consumer(queue)).join();
    }
}
/* Output:
[15] Prioritized 1
[17] Prioritized 2
[17] Prioritized 5
[16] Prioritized 6
[14] Prioritized 9
[12] Prioritized 0
[11] Prioritized 12
[11] Prioritized 4
[13] Prioritized 13
[14] Prioritized 18
[12] Prioritized 17
[15] Prioritized 23
[18] Prioritized 26
[16] Prioritized 29
[12] Prioritized 16
[11] Prioritized 30
[11] Prioritized 24
[10] Prioritized 15
[10] Prioritized 22
[8] Prioritized 25
[8] Prioritized 11
[8] Prioritized 10
[6] Prioritized 31
[3] Prioritized 7
[2] Prioritized 20
[1] Prioritized 3
[0] Prioritized 19
[0] Prioritized 8
[0] Prioritized 14
[0] Prioritized 21
[-1] Prioritized 28
(0:12)(1:15)(2:17)(3:1)(4:11)
(5:17)(6:16)(7:3)(8:0)(9:14)
(10:8)(11:8)(12:11)(13:13)(14:0)
(15:10)(16:12)(17:12)(18:14)(19:0)
(20:2)(21:0)(22:10)(23:15)(24:11)
(25:8)(26:18)(27:-1)(28:-1)(29:16)
(30:11)(31:6)(32:-1)
 */
