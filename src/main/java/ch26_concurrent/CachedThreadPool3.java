package ch26_concurrent;

import onjava.Timer;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author runningpig66
 * @date 3月2日 周一
 * @time 16:56
 * P.235 §5.8 创建和运行任务 §5.8.3 生成结果
 * <p>
 * ExecutorService 允许在集合中通过 invokeAll() 来启动所有的 Callable：
 */
public class CachedThreadPool3 {
    public static Integer extractResult(Future<Integer> f) {
        try {
            // V get() throws InterruptedException, ExecutionException;
            return f.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        List<CountingTask> tasks = IntStream.range(0, 10)
                .mapToObj(CountingTask::new)
                .collect(Collectors.toList());

        System.out.println("[main] 准备调用 ExecutorService.invokeAll() 提交任务...");
        Timer timer = new Timer();

        // <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
        // invokeAll 方法用于批量提交并执行一组 Callable 任务。当主线程调用此方法时，会将传入的任务集合一次性提交给底层的线程池，由多个工作线程并发处理。
        // 同步阻塞：需要注意的是，这是一个典型的同步阻塞方法。主线程在调用该方法后会被强制挂起（阻塞），停止向下执行，直到提交的所有任务都在后台执行完毕，该方法才会返回结果并解除阻塞。
        // 并发提速：虽然主线程被阻塞了，但该方法依然充分发挥了多线程并发的性能优势。主线程被阻塞的总时长并不等于所有任务耗时的总和，而是严格取决于这批任务中耗时最长的那个（即“短板效应”）。
        // 例如，10 个最高耗时 5 秒的任务并发执行，主线程的总阻塞时长约为 5 秒。这体现了底层多线程并发的性能优势。
        // 这种行为在并发编程中被称为“分散-聚集 (Scatter-Gather)”模式，它的核心设计目的就是为了满足特定的业务场景：
        // 即主线程的下一步计算或后续业务逻辑强依赖于所有并行子任务的全部完成，必须等待全量数据齐备后才能继续向下执行。
        // invokeAll 方法在所有任务执行完毕后结束阻塞，并返回一个包含对应任务状态和结果的 List<Future<T>>。
        // 当主线程拿到这个返回值列表时，集合中每一个 Future 元素的 isDone() 方法必定返回 true，代表所有任务均已处于确定的“完成状态”。
        // 由于结果已经被计算完毕并预存在 Future 内部，后续在遍历列表调用 future.get() 提取真实结果时，不会发生任何二次等待或阻塞，数据可被瞬间读取。
        // 然而，结合原书作者的观点来看，这种 API 设计存在明显的语义冗余与不够优雅之处。
        // 在 Java 并发设计中，Future 接口的本意是作为一个非阻塞的凭证，代表一个“未来将要发生、但当前尚未完成”的异步结果。
        // 但 invokeAll 方法却硬生生地将主线程卡住，直到所有的任务都变成了“过去式”（彻底完成）才交出结果。
        // 既然方法被设计为强阻塞直到任务的全部完成，那么直接返回一个包含纯结果的 List<T> 显然更符合直觉。
        // 但现有的设计却强行用代表“未来”的 Future 外壳把已经确定的结果包裹起来，不仅未能体现出异步非阻塞的优势，
        // 反而迫使开发者必须额外多写一层 get() 调用的循环代码去进行手动“拆壳”以获取早已存在的真实数据。
        // 它将阻塞的缺点和返回 Future 包装的繁琐全占了，这也是该方法在现代并发设计中经常被探讨的局限性所在。
        List<Future<Integer>> futures = exec.invokeAll(tasks);

        System.out.println("[main] ExecutorService.invokeAll() 返回了结果。耗时：" + timer.duration() + " 毫秒");

        Integer sum = futures.stream()
                .map(CachedThreadPool3::extractResult)
                .reduce(0, Integer::sum);
        System.out.println("sum = " + sum);
        exec.shutdown();
    }
}
/* Output: （注意观察实际运行时输出时序）
[main] 准备调用 ExecutorService.invokeAll() 提交任务...
5 pool-1-thread-6 100
3 pool-1-thread-4 100
0 pool-1-thread-1 100
4 pool-1-thread-5 100
8 pool-1-thread-9 100
1 pool-1-thread-2 100
7 pool-1-thread-8 100
2 pool-1-thread-3 100
9 pool-1-thread-10 100
6 pool-1-thread-7 100
[main] ExecutorService.invokeAll() 返回了结果。耗时：4615 毫秒
sum = 1000
 */
