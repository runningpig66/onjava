package ch26_concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author runningpig66
 * @date 3月4日 周三
 * @time 2:36
 * P.242 §5.10 CompletableFuture §5.10.1 基本用法
 * <p>
 * 有趣的是，一旦将 Machina 用 CompletableFuture 包装起来，我们就会发现，可以通过在 CompletableFuture 上增加操作来控制其包含的对象：
 */
public class CompletableApply {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Machina> cf = CompletableFuture.completedFuture(new Machina(0));
        // <U> CompletableFuture<U> thenApply(Function<? super T,? extends U> fn)
        // thenApply 是 CompletableFuture 的转换方法，用于对异步任务的结果进行链式转换。
        // 作用：当当前的 CompletableFuture 完成时，将其结果传递给指定的 Function (如 Machina::work) 进行处理，
        // 并将 Function 返回的新结果，包装成一个全新的 CompletableFuture 返回。执行机制：复用线程。
        // 若当前的 CompletableFuture 已完成，则后续的 Function 会立即在“调用所在线程”（如当前的 main 线程）中同步执行。
        // 若当前的 CompletableFuture 未完成，则将该转换操作放入队列，未来由“完成当前任务的那个后台子线程”继续异步执行。
        CompletableFuture<Machina> cf2 = cf.thenApply(Machina::work);
        CompletableFuture<Machina> cf3 = cf2.thenApply(Machina::work);
        CompletableFuture<Machina> cf4 = cf3.thenApply(Machina::work);
        CompletableFuture<Machina> cf5 = cf4.thenApply(Machina::work);
    }
}
/* Output:
currentThread-main: Machina0: ONE
currentThread-main: Machina0: TWO
currentThread-main: Machina0: THREE
currentThread-main: Machina0: complete
 */
