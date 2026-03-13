package ch27_lowlevel;

import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 3月13日 周五
 * @time 13:28
 * P.311 §6.7 库组件 §6.7.1 延迟队列 DelayQueue
 * <p>
 * 这是一种由实现了 Delayed 接口的对象组成的无边界 BlockingQueue（阻塞队列）。一个对象只有在其延迟时间到期后才能从队列中取出。
 * 队列是有序的，因此头部对象是逾期（过期）时间最久的。如果没有到达延迟时间，那么头部元素就相当于不存在，同时 poll() 会返回 null（因此不能将 null 放入队列）。
 * 下面这个示例中，Delayed 对象自身就是任务，而 DelayedTaskConsumer 将最“紧急”的任务（即过期时间最长的任务）从队列中取出并执行。
 * 因此，要注意 DelayQueue 是优先级队列的变体。从输出可以看出，任务创建的顺序对执行顺序没有任何影响。相反，任务按照预期以延迟的顺序执行了。
 */
class DelayedTask implements Runnable, Delayed {
    private static int counter = 0;
    private final int id = counter++;
    private final int delta;
    private final long trigger;
    // DelayedTask 含有一个称为 sequence 的 List<DelayedTask>，它维护了创建任务的顺序，因此我们可以看到该排序确实发生了。
    protected static List<DelayedTask> sequence = new ArrayList<>();

    DelayedTask(int delayInMilliseconds) {
        delta = delayInMilliseconds;
        /* TODO: TimeUnit.java 枚举工具类：用数据状态驱动行为的架构设计。
         * long convert(long sourceDuration, TimeUnit sourceUnit): 将给定单位的时间长度转换为当前枚举实例所代表的时间单位。
         * 精度处理规则：从高精度单位向低精度单位转换时，结果会被直接截断，从而丢失精度（例如：将 999 毫秒转换为秒，结果为 0）。
         * 溢出处理规则：从低精度单位向高精度单位转换时，如果数值计算发生越界溢出，正数将饱和取值为 Long.MAX_VALUE，负数将饱和取值为 Long.MIN_VALUE。
         */
        // TimeUnit 这个类非常好用，因为无须进行任何计算就可以转换单位。举例来说，delta 的值保存为毫秒，但是 System.nanoTime() 生成的是纳秒。
        // 通过指定 delta 值的当前单位和想要转换成的目标单位（convert() 方法的接收者 TimeUnit.NANOSECONDS），就可以转换它的值了：
        trigger = System.nanoTime() + TimeUnit.NANOSECONDS.convert(delta, TimeUnit.MILLISECONDS);
        sequence.add(this);
    }

    /* long getDelay(TimeUnit unit); 计算并返回任务距离执行还剩余的延迟时间（延迟时间还有多久，或延迟时间已经过期了多久）。
     * 该方法会被 DelayQueue 底层逻辑调用，用来判断是否可以放行当前任务。
     * 计算逻辑：将任务初始化时锁定的“绝对触发时间（trigger）”减去“系统当前时间”，得出还需等待的底层纳秒数。
     * 随后，直接利用传入的时间单位（unit）将纳秒转换为底层队列所请求的单位并返回。
     * 如果返回的值小于等于 0，代表时间已到或已超时，该任务将获准出列。
     */
    // 在 getDelay() 中，将目标单位作为 unit 参数传入方法，你可以使用该参数，在甚至不知道它是什么时间单位的情况下，
    // 就可以将触发时间的时差转换为调用者所请求的单位（这是个策略设计模式的简单示例，将算法的一部分作为参数传入，从而提升了灵活性）。
    @Override
    public long getDelay(@NonNull TimeUnit unit) {
        return unit.convert(trigger - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    /* public int compareTo(T o); 比较此任务与指定延迟任务的执行顺序。
     * 此方法主要用于 DelayQueue 内部的优先级排序。为了最小化并发场景下的计算开销，
     * 该实现不依赖系统当前时间进行实时差值计算，而是直接比较两个任务在实例化时确定的绝对触发时间戳（trigger）。
     * 如果当前任务的绝对触发时间早于指定任务，则返回负整数；
     * 如果当前任务的绝对触发时间晚于指定任务，则返回正整数；如果两者的绝对触发时间完全相同，则返回 0。
     */
    @Override
    public int compareTo(@NonNull Delayed arg) {
        DelayedTask that = (DelayedTask) arg;
        /*if (trigger < that.trigger) {
            return -1;
        }
        if (trigger > that.trigger) {
            return 1;
        }
        return 0;*/
        return Long.compare(trigger, that.trigger);
    }

    @Override
    public void run() {
        System.out.print(this + " ");
    }

    @Override
    public String toString() {
        return String.format("[%d] Task %d", delta, id);
    }

    public String summary() {
        return String.format("(%d:%d)", id, delta);
    }

    public static class EndTask extends DelayedTask {
        EndTask(int delay) {
            super(delay);
        }

        @Override
        public void run() {
            super.run();
            System.out.println();
            sequence.forEach(dt -> System.out.println(dt.summary()));
        }
    }
}

public class DelayQueueDemo {
    public static void main(String[] args) throws InterruptedException {
        /* static <T> Stream<T> concat(Stream<? extends T> a, Stream<? extends T> b)
         * 创建一个惰性连接的流，其元素由第一个输入流（a）的所有元素及随后的第二个输入流（b）的所有元素组成。
         * 状态传递规则：如果两个输入流都是有序的，则生成的合并流也是有序的；如果任一输入流是并行的，则生成的合并流也是并行的。
         * 当合并后的流被关闭时，两个底层输入流的关闭处理器（close handlers）都将被调用。
         * 架构与优化注意：为保留内部评估大小的优化机制，此方法被严格设计为仅接受两个流作为参数。
         * 若需合并多个流，应避免嵌套调用此方法，建议采取创建流的流并扁平化映射的方式（例如：Stream.of(s1, s2, s3).flatMap(s -> s)）。
         * 风险提示：应极力避免通过循环进行深度嵌套拼接。访问深度拼接流中的元素会导致调用链过深，极端情况下会引发 StackOverflowError。
         */
        DelayQueue<DelayedTask> tasks = Stream.concat(
                new Random(47).ints(20, 0, 4000)
                        .mapToObj(DelayedTask::new),
                Stream.of(new DelayedTask.EndTask(4000))
        ).collect(Collectors.toCollection(DelayQueue::new));
        while (!tasks.isEmpty()) {
            /* public E take() throws InterruptedException: 获取并移除此队列的头部元素（即延迟时间最先到期的任务）。
             * take() 为阻塞方法。如果当前队列为空，或头部任务的延迟时间尚未到期，
             * 调用此方法的线程将被挂起并进入等待状态，直到有任务到期可用时才唤醒并返回该任务。
             * 关联对比：public E poll(): poll() 为非阻塞方法。
             * 如果在调用 poll() 时队列为空，或者头部任务尚未到期，该方法不会阻塞当前线程，而是立即返回 null。
             * 基于此机制，DelayQueue 严格禁止存入 null 元素，以确保调用者能通过 null 返回值准确判断队列当前没有可用（已到期）的任务。
             */
            tasks.take().run();
        }
        System.out.println(tasks.poll());
    }
}
/* Output:
[128] Task 12 [429] Task 6 [551] Task 13 [555] Task 2 [693] Task 3 [809] Task 15 [961] Task 5 [1258] Task 1 [1258] Task 20 [1520] Task 19 [1861] Task 4 [1998] Task 17 [2200] Task 8 [2207] Task 10 [2288] Task 11 [2522] Task 9 [2589] Task 14 [2861] Task 18 [2868] Task 7 [3278] Task 16 [4000] Task 0
(0:4000)
(1:1258)
(2:555)
(3:693)
(4:1861)
(5:961)
(6:429)
(7:2868)
(8:2200)
(9:2522)
(10:2207)
(11:2288)
(12:128)
(13:551)
(14:2589)
(15:809)
(16:3278)
(17:1998)
(18:2861)
(19:1520)
(20:1258)
null
 */
