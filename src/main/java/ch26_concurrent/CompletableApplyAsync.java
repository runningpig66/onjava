package ch26_concurrent;

import onjava.Timer;

import java.util.concurrent.CompletableFuture;

/**
 * @author runningpig66
 * @date 3月4日 周三
 * @time 4:20
 * P.243 §5.10 CompletableFuture §5.10.1 基本用法
 */
public class CompletableApplyAsync {
    public static void main(String[] args) {
        Timer timer = new Timer();
        CompletableFuture<Machina> cf = CompletableFuture.completedFuture(new Machina(0))
                // <U> CompletableFuture<U> thenApplyAsync(Function<? super T,? extends U> fn)
                // thenApplyAsync 也是 CompletableFuture 的转换方法，用于对异步任务的结果进行链式转换。
                // 作用：与 thenApply 相同，用于对异步任务的结果进行链式转换。核心机制：强制异步。
                // 无论当前的 CompletableFuture 是否已经完成，都不会使用当前线程（如 main 线程）去执行后续的 Function。
                // 它会将指定的函数任务强制提交到后台线程池（默认是 ForkJoinPool.commonPool()）中异步执行。
                // 这允许主线程瞬间完成任务的组装和分发（如本例仅耗时 9 毫秒），然后立即去做其他事情，实现真正的并发。
                .thenApplyAsync(Machina::work)
                .thenApplyAsync(Machina::work)
                .thenApplyAsync(Machina::work)
                .thenApplyAsync(Machina::work);
        System.out.println(timer.duration());
        // 注意：由于是强行抛给后台守护线程执行，如果不调用 join() 阻塞 main 线程，
        // main 线程跑完最后一行代码后会导致 JVM 关闭，后台任务会被强行中断。
        System.out.println(cf.join());
        System.out.println(timer.duration());
    }
}
/* Output:
9
currentThread-ForkJoinPool.commonPool-worker-1: Machina0: ONE
currentThread-ForkJoinPool.commonPool-worker-1: Machina0: TWO
currentThread-ForkJoinPool.commonPool-worker-1: Machina0: THREE
currentThread-ForkJoinPool.commonPool-worker-1: Machina0: complete
Machina0: complete
4050
 */
