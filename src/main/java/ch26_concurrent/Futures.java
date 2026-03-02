package ch26_concurrent;

import onjava.Timer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author runningpig66
 * @date 3月2日 周一
 * @time 22:04
 * P.235 §5.8 创建和运行任务 §5.8.3 生成结果
 * <p>
 * 本类演示了 ExecutorService.submit() 的异步提交机制，以及 Future 接口在结果获取阶段的阻塞局限性。
 * 1. 异步非阻塞提交 (submit 机制)
 * 与 invokeAll() 在提交阶段便产生阻塞的行为不同（参照 CachedThreadPool3.java），submit() 实现了真正的异步任务派发。
 * 当主线程提交任务后，会瞬间获得一个 Future 实例作为异步结果的凭证对象。主线程在此过程不会发生停滞，能够立即向下推进执行其他业务逻辑。
 * 2. 延迟的阻塞 (get 机制的悖论)
 * Future 接口被设计为尚未完成任务的占位符。然而，当业务流程必须提取实际结果而调用 future.get() 时，
 * 若底层任务仍在运行，调用方的线程依然会被底层锁机制强制挂起（持续阻塞），直到计算完成并返回结果。
 * 3. 架构局限性与技术演进结论
 * 结合原书观点，原生 Future API 仅仅是将“等待”这件事情从任务的【提交阶段】推迟到了【获取阶段】。
 * 虽然可以通过 isDone() 方法进行非阻塞的状态查询，但这种方式往往需要编写消耗 CPU 资源的轮询代码。
 * 鉴于其无法实现真正意义上基于事件通知的异步编程，原生 Future 目前被视为一种存在缺陷的过渡方案。
 * 现代 Java 并发开发通常更推荐使用支持响应式回调的 CompletableFuture。
 */
public class Futures {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService exec = Executors.newSingleThreadExecutor();

        System.out.println("[main] 准备调用 ExecutorService.submit() 提交任务...");
        Timer timer = new Timer();

        // <T> Future<T> submit(Callable<T> task);
        // 异步派发：主线程瞬间完成任务的入队操作，并立即得到一个状态为“未完成”的 Future 凭证。
        // 此处的调用耗时极短（通常在 1 毫秒内），主线程维持非阻塞状态。
        Future<Integer> f = exec.submit(new CountingTask(99));

        System.out.println("[main] ExecutorService.submit() 返回了结果。耗时：" + timer.duration() + " 毫秒");
        System.out.println("[main] 准备调用 Future.get() 获取结果...");

        // V get() throws InterruptedException, ExecutionException;
        // 同步提取：主线程在此处强制等待。如果 CountingTask 的后台运行尚未结束（例如处于 5 秒的休眠期），
        // 主线程将在此行代码持续阻塞，直至拿到最终的 Integer 返回值。
        System.out.println(f.get()); // [1] 当你对尚未完成任务的 Future 调用 get() 方法时，调用会持续阻塞（等待），直到结果可用。
        System.out.println("[main] Future.get() 返回了结果。耗时：" + timer.duration() + " 毫秒");
        exec.shutdown();
    }
}
/* Output: （注意观察实际运行时输出时序）
[main] 准备调用 ExecutorService.submit() 提交任务...
[main] ExecutorService.submit() 返回了结果。耗时：0 毫秒
[main] 准备调用 Future.get() 获取结果...
99 pool-1-thread-1 100
100
[main] Future.get() 返回了结果。耗时：3983 毫秒
 */
