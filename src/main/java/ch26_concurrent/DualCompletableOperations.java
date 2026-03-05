package ch26_concurrent;

import java.util.concurrent.CompletableFuture;

import static ch26_concurrent.CompletableUtilities.showr;
import static ch26_concurrent.CompletableUtilities.voidr;

/**
 * @author runningpig66
 * @date 3月4日 周三
 * @time 9:23
 * P.248 §5.10 CompletableFuture §5.10.3 合并多个 CompletableFuture
 * <p>
 * CompletableFuture 中的第二类方法接收两个 CompletableFuture 作为参数，并以多种方式将其合并。
 * 一般来说一个 CompletableFuture 会先于另一个执行完成，两者看起来就像在彼此竞争。这些方法使你可以用不同的方式处理结果。
 */
public class DualCompletableOperations {
    static CompletableFuture<Workable> cfA, cfB;

    static void init() {
        cfA = Workable.make("A", 4.40);
        cfB = Workable.make("B", 2.20); // Always wins
    }

    static void join() {
        cfA.join();
        cfB.join();
        System.out.println("*****************");
    }

    public static void main(String[] args) {
        init();
        // CompletableFuture<Void> runAfterEitherAsync(CompletionStage<?> other, Runnable action)
        // 两任务 OR 逻辑（无入参，无返回值）。当当前任务 (cfA) 或参数任务 (cfB) 中的任意一个率先完成时，
        // 立刻在后台线程执行指定的 Runnable。不读取任何任务的实际执行结果。
        voidr(cfA.runAfterEitherAsync(cfB, () -> System.out.println("runAfterEither")));
        // 关于测试用例末尾 join() 方法与 showr/voidr 工具方法的联合阻塞机制说明：表面上看，
        // 代码末尾显式调用 cfA.join() 和 cfB.join() 似乎是多余的，但实际上它们与 showr/voidr 内部隐式的 get() 阻塞
        // 共同维持了并发测试日志的准确性。showr 和 voidr 内部调用了 get()，会强制主线程死死等待最终的合并任务执行完毕。
        // 对于 AND 逻辑（Both/Combine/allOf），合并任务本身就必须等待所有前置任务完成，所以当主线程退出 showr/voidr 时，
        // A 和 B 必然已经彻底结束，此时的 join() 仅作为纯粹的流程占位和打印分割线。
        // 真正的核心价值体现在 OR 逻辑（Either/anyOf）中。在 OR 逻辑下，只要较快的前置任务（如 cfB）执行完毕，
        // 后续的合并任务就会立刻触发并迅速结束，导致 showr/voidr 提前解除对主线程的阻塞。此时，
        // 较慢的前置任务（如 cfA）仍在后台线程中继续运行。如果没有显式调用的 join() 强制主线程在此处兜底，
        // 等待慢速任务完全结束，该慢速任务的延迟打印就会在几秒后突然插入并严重污染下一个独立测试用例的控制台输出。
        // 因此，这里的 join() 实际上是充当了强制清理并发环境的物理隔离屏障。
        join();

        init();
        // CompletableFuture<Void> runAfterBothAsync(CompletionStage<?> other, Runnable action)
        // 两任务 AND 逻辑（无入参，无返回值）。必须等待当前任务 (cfA) 和参数任务 (cfB) 全部完成之后，
        // 才在后台线程执行指定的 Runnable。同样不读取任何任务的实际执行结果。
        voidr(cfA.runAfterBothAsync(cfB, () -> System.out.println("runAfterBoth")));
        join();

        init();
        // <U> CompletableFuture<U> applyToEitherAsync(
        //        CompletionStage<? extends T> other, Function<? super T, U> fn)
        // 两任务 OR 逻辑（有入参，有返回值）。当当前任务或参数任务中任意一个率先完成时，将其真实运算结果传递给指定的 Function 进行数据转换。
        // 并返回一个包含该转换后新结果的全新 CompletableFuture。要求两个前置任务的返回值类型必须一致。
        showr(cfA.applyToEitherAsync(cfB, w -> {
            System.out.println("applyToEither: " + w);
            return w;
        }));
        join();

        init();
        // CompletableFuture<Void> acceptEitherAsync(
        //        CompletionStage<? extends T> other, Consumer<? super T> action)
        // 两任务 OR 逻辑（有入参，无返回值）。当当前任务或参数任务中任意一个率先完成时，
        // 将其运算结果传递给指定的 Consumer 进行消费（如打印、入库）。消费完毕后不再向下游产出新结果。
        voidr(cfA.acceptEitherAsync(cfB, w -> System.out.println("acceptEither: " + w)));
        join();

        init();
        // <U> CompletableFuture<Void> thenAcceptBothAsync(
        //        CompletionStage<? extends U> other,
        //        BiConsumer<? super T, ? super U> action)
        // 两任务 AND 逻辑（双入参，无返回值）。必须等待当前任务和参数任务全部完成，将两者的运算结果（w1 和 w2）同时
        // 传递给指定的 BiConsumer 进行联合消费。消费完毕后不再向下游产出新结果。
        voidr(cfA.thenAcceptBothAsync(cfB, (w1, w2) ->
                System.out.println("thenAcceptBoth: " + w1 + ", " + w2)));
        join();

        init();
        // <U,V> CompletableFuture<V> thenCombineAsync(
        //        CompletionStage<? extends U> other,
        //        BiFunction<? super T,? super U,? extends V> fn)
        // 两任务 AND 逻辑（双入参，有返回值）。必须等待当前任务和参数任务全部完成，将两者的运算结果同时传递给指定的 BiFunction 进行合并或计算。
        // 将 BiFunction 最终产出的新数据包装成一个全新的 CompletableFuture 并返回。
        showr(cfA.thenCombineAsync(cfB, (w1, w2) -> {
            System.out.println("thenCombine: " + w1 + ", " + w2);
            return w1;
        }));
        join();

        init();
        CompletableFuture<Workable>
                cfC = Workable.make("C", 0.05),
                cfD = Workable.make("D", 1.10);
        // static CompletableFuture<Object> anyOf(CompletableFuture<?>... cfs)
        // 多任务并发全局 OR 逻辑。接收任意数量（数组/可变参数）的 CompletableFuture，只要其中有任意一个任务完成，
        // anyOf 返回的全局 CompletableFuture 就会立刻被标记为完成，并且内部包裹的是那个率先完成的子任务的计算结果。
        // 由于各个子任务的返回值类型可能不同，所以 anyOf 返回的泛型类型只能是宽泛的 Object，若下游需要使用该结果，通常需要进行类型转换。
        CompletableFuture.anyOf(cfA, cfB, cfC, cfD)
                .thenRunAsync(() -> System.out.println("anyOf"));
        join();

        init();
        cfC = Workable.make("C", 0.05);
        cfD = Workable.make("D", 1.10);
        // static CompletableFuture<Void> allOf(CompletableFuture<?>... cfs)
        // 多任务并发全局 AND 逻辑（即并发屏障 / 栅栏）。接收任意数量（数组/可变参数）的 CompletableFuture，必须等待所有子任务全部执行完毕，
        // allOf 返回的全局 CompletableFuture 才会标记为完成。它返回的是 CompletableFuture<Void>，
        // 它仅用于统一监控多个任务的执行进度，不负责收集每个子任务的具体返回值。若需获取结果，需自行从原有的独立 future 实例中提取。
        CompletableFuture.allOf(cfA, cfB, cfC, cfD)
                .thenRunAsync(() -> System.out.println("allOf"));
        join();
    }
}
/* Output:
Workable[BW]
runAfterEither
Workable[AW]
*****************
Workable[BW]
Workable[AW]
runAfterBoth
*****************
Workable[BW]
applyToEither: Workable[BW]
Workable[BW]
Workable[AW]
*****************
Workable[BW]
acceptEither: Workable[BW]
Workable[AW]
*****************
Workable[BW]
Workable[AW]
thenAcceptBoth: Workable[AW], Workable[BW]
*****************
Workable[BW]
Workable[AW]
thenCombine: Workable[AW], Workable[BW]
Workable[AW]
*****************
Workable[CW]
anyOf
Workable[DW]
Workable[BW]
Workable[AW]
*****************
Workable[CW]
Workable[DW]
Workable[BW]
Workable[AW]
allOf
*****************
 */
