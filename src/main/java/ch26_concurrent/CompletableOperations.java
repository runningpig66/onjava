package ch26_concurrent;

import java.util.concurrent.CompletableFuture;

import static ch26_concurrent.CompletableUtilities.showr;
import static ch26_concurrent.CompletableUtilities.voidr;

/**
 * @author runningpig66
 * @date 3月4日 周三
 * @time 5:17
 * P.245 §5.10 CompletableFuture §5.10.2 其他操作
 */
public class CompletableOperations {
    // 简单起见，下面的 CompletableFuture 仅仅包装了 Integer 类型。
    // cfi() 则是一个简化的方法，其在一个完整的 CompletableFuture<Integer> 内部包装了一个 int 类型。
    static CompletableFuture<Integer> cfi(int i) {
        // return CompletableFuture.completedFuture(Integer.valueOf(i));
        return CompletableFuture.completedFuture(i);
    }

    public static void main(String[] args) {
        showr(cfi(1)); // Basic test
        voidr(cfi(2).runAsync(() -> System.out.println("runAsync")));
        // CompletableFuture<Void> thenRunAsync(Runnable action)
        // 当当前 CompletableFuture 完成时，在后台线程执行指定的 Runnable。
        // 它既不关心上一步的结果（无入参），也不产生新的结果（无返回值），仅用于在任务链的特定阶段触发一个独立的后续动作。
        voidr(cfi(3).thenRunAsync(() -> System.out.println("thenRunAsync")));
        voidr(CompletableFuture.runAsync(() -> System.out.println("runAsync is static")));
        // static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier)
        // 提交一个带有返回值的异步任务。接收一个 Supplier，在后台线程池中异步执行，
        // 并返回一个包含该任务执行结果的 CompletableFuture<U> 对象。
        showr(CompletableFuture.supplyAsync(() -> 99));
        // CompletableFuture<Void> thenAcceptAsync(Consumer<? super T> action)
        // 对上一步的异步结果进行消费。接收一个 Consumer，仅处理当前 CompletableFuture 产出的数据，
        // 不产生任何向下游传递的新结果（返回 CompletableFuture<Void>）。通常用于任务链末端执行打印或入库等操作。
        voidr(cfi(4).thenAcceptAsync(i -> System.out.println("thenAcceptAsync: " + i)));
        // <U> CompletableFuture<U> thenApplyAsync(Function<? super T,? extends U> fn)
        showr(cfi(5).thenApplyAsync(i -> i + 42));
        // <U> CompletableFuture<U> thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> fn)
        // 对异步任务结果进行扁平化转换（类似 Stream 的 flatMap）。当后续 Function 的返回值本身也是一个 CompletableFuture 时，
        // 使用此方法可以避免结果产生多层嵌套（如 CompletableFuture<CompletableFuture<T>>），
        // 它会自动将内部的 CompletableFuture 展平，最终向外暴露一个单一的 CompletableFuture 对象。
        showr(cfi(6).thenComposeAsync(i -> cfi(i + 99)));
        CompletableFuture<Integer> c = cfi(7);
        // void obtrudeValue(T value) 强制替换或设置 CompletableFuture 的结果。
        // 无论该任务当前是未完成、已完成还是异常结束，调用此方法都会无条件将其状态标记为完成，
        // 并以传入的 value 覆盖作为最终结果。极少使用，通常仅用于系统故障时的强制错误恢复。
        c.obtrudeValue(111);
        showr(c);
        // CompletableFuture<T> toCompletableFuture()
        // 将当前的 CompletionStage 接口实例向下转型，转换为标准的 CompletableFuture 类实例。
        // 常用于 API 返回值为 CompletionStage 时，需要调用完整 CompletableFuture 特有方法的场景。
        showr(cfi(8).toCompletableFuture());
        c = new CompletableFuture<>();
        // boolean complete(T value) 尝试手动完成一个尚未结束的 CompletableFuture。
        // 若此时任务尚未完成，则将其状态置为“已完成”，以传入的 value 作为结果，并返回 true。
        // 若任务已经完成（正常或异常完成），则此调用完全无效，原有结果保持不变，并返回 false。
        c.complete(9);
        showr(c);
        c = new CompletableFuture<>();
        // boolean cancel(boolean mayInterruptIfRunning)
        // 尝试取消此任务。调用后，任务将强制以 CancellationException 异常状态结束。
        // 注意：参数 mayInterruptIfRunning 仅仅是为了实现 Future 接口而保留的签名。
        // 在 CompletableFuture 的底层实现中，该布尔值没有任何实际效果，它绝对不会中断或干预正在运行的底层后台线程。
        c.cancel(true);
        // boolean isCancelled()
        // 判断该 CompletableFuture 是否在正常完成之前被手动调用 cancel() 取消。
        System.out.println("cancelled: " + c.isCancelled());
        // boolean isCompletedExceptionally()
        // 判断该 CompletableFuture 是否以异常形式结束（包括被手动 cancel 取消，或在执行逻辑中抛出了未被捕获的异常）。
        System.out.println("completed exceptionally: " + c.isCompletedExceptionally());
        // boolean isDone()
        // 判断该 CompletableFuture 是否已结束生命周期。无论是因为正常执行完毕、抛出异常还是被取消，只要任务不再处于执行状态，均返回 true。
        System.out.println("done: " + c.isDone());
        System.out.println(c);
        c = new CompletableFuture<>();
        // T getNow(T valueIfAbsent)
        // 非阻塞地尝试获取当前任务的结果。如果该 CompletableFuture 已经计算完成，则立即返回真实的运行结果；
        // 如果尚未完成，则绝不阻塞当前线程，直接返回传入的替代参数 (valueIfAbsent)。
        System.out.println(c.getNow(777));
        c = new CompletableFuture<>();
        c.thenApplyAsync(i -> i + 42)
                .thenApplyAsync(i -> i * 12);
        // int getNumberOfDependents()
        // 返回当前 CompletableFuture 对象所注册的直接依赖任务（即挂接在它上面的下游回调函数）的预估数量。
        // 主要用于系统监控或复杂并发流程的调试诊断。
        System.out.println("dependents: " + c.getNumberOfDependents());
        c.thenApplyAsync(i -> i / 2);
        System.out.println("dependents: " + c.getNumberOfDependents());
    }
}
/* Output:
1
runAsync
thenRunAsync
runAsync is static
99
thenAcceptAsync: 4
47
105
111
8
9
cancelled: true
completed exceptionally: true
done: true
java.util.concurrent.CompletableFuture@4dd8dc3[Completed exceptionally: java.util.concurrent.CancellationException]
777
dependents: 1
dependents: 2
 */
