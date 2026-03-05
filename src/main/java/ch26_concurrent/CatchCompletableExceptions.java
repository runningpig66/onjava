package ch26_concurrent;

/**
 * @author runningpig66
 * @date 3月5日 周四
 * @time 19:34
 * P.256 §5.10 CompletableFuture §5.10.5 异常
 * <p>
 * 相比于在合并或获取结果时粗暴地使用 try-catch，我们更倾向于利用 CompletableFuture 所带来的更为先进的机制来自动地响应异常。
 * 你只需照搬在所有 CompletableFuture 中看到的方式即可：在调用链中插入 CompletableFuture 调用。
 * 一共有 3 个选项：exceptionally()、handle() 以及 whenComplete()：
 */
public class CatchCompletableExceptions {
    static void handleException(int failcount) {
        // Call the Function only if there's an exception, must produce same type as came in:
        CompletableExceptions
                .test("exceptionally", failcount)
                // CompletableFuture<T> exceptionally(Function<Throwable, ? extends T> fn)
                // 异步流水线中的专用异常恢复机制（等效于传统的 catch 块）。仅当上游的 CompletableFuture 处于异常完成状态时，
                // 才会触发执行指定的 Function。若上游正常完成，此回调将被完全跳过，原正常结果直接透传。
                // 该方法接收上游抛出的 Throwable 异常对象，并强制要求返回一个与原始数据流泛型（T）类型一致的恢复对象（Fallback Object）。
                // 执行完毕后，它会“消化”并清除数据流中的异常标记，将产出的恢复对象作为正常计算结果传递给下游节点，
                // 使得后续的链式调用（如 thenAccept）能够以正常流程继续顺利执行。
                .exceptionally((ex) -> { // Function
                    // 此分支为不可达代码（Dead Code）：exceptionally 机制仅在上游抛出异常时才会被触发，因此入参 ex 绝对不可能为 null。
                    if (ex == null) {
                        System.out.println("I don't get it yet");
                    }
                    return new Breakable(ex.getMessage(), 0);
                })
                .thenAccept(str -> System.out.println("result: " + str));

        // Create a new result (recover):
        CompletableExceptions
                .test("handle", failcount)
                // <U> CompletableFuture<U> handle(BiFunction<? super T, Throwable, ? extends U> fn)
                // 异步流水线中的全局干预与转换机制（兼具 catch 与 finally 的特性，且具备类型转换能力）。
                // 无论上游任务是正常计算完毕还是因异常中断，该回调均会被无条件触发。它同时接收两个参数：
                // 上游的正常计算结果（若上游抛出异常，则此参数为 null）与异常对象（若上游正常完成，则此参数为 null）。
                // 该方法的核心优势在于允许开发者完全接管当前数据流，并根据成功或失败的状态，统一计算并返回一个全新类型（U）的正常结果向下游传递。
                .handle((result, fail) -> { // BiFunction
                    if (fail != null) {
                        return "Failure recovery object";
                    } else {
                        return result + " is good";
                    }
                })
                .thenAccept(str -> System.out.println("result: " + str));

        // Do something but pass the same result through:
        CompletableExceptions
                .test("whenComplete", failcount)
                // CompletableFuture<T> whenComplete(BiConsumer<? super T, ? super Throwable> action)
                // 异步流水线中的透明旁路监听机制（等效于传统的 finally 块，具备引用透传特性）。
                // 无论上游任务正常完成还是异常中断，该回调均会被无条件触发，并同时接收原始的计算结果与异常对象。
                // 该节点无法像 handle 那样通过 return 替换数据流中的元素，框架底层会将上游传入的“同一个对象引用”或“异常状态”直接向下游转发。
                // 它接收一个无返回值的 BiConsumer，其设计初衷类似于 Java Stream API 中的 peek() 方法，旨在提供一个“只读”的观察点。
                // 注意：由于底层透传的是对象引用，若在此处修改对象内部的可变属性，下游确实会接收到被改变后的状态。
                // 但在函数式编程规范中，不建议在此执行修改数据的操作。这里的常规用途主要是执行记录日志、释放资源等不干扰主数据流的清理动作。
                // 此外，若上游抛出异常，该异常也会被原样透传，导致下游的正常处理逻辑（如 thenAccept）被直接短路跳过。
                .whenComplete((result, fail) -> { // BiFunction
                    if (fail != null) {
                        System.out.println("It failed");
                    } else {
                        System.out.println(result + " OK");
                    }
                })
                .thenAccept(r -> System.out.println("result: " + r));
    }

    public static void main(String[] args) {
        System.out.println("**** Failure Mode ****");
        handleException(2);
        System.out.println("**** Success Mode ****");
        handleException(0);
    }
}
/* Output:
**** Failure Mode ****
Breakable_exceptionally [1]
Throwing Exception for exceptionally
result: Breakable_java.lang.RuntimeException: Breakable_exceptionally failed [0]
Breakable_handle [1]
Throwing Exception for handle
result: Failure recovery object
Breakable_whenComplete [1]
Throwing Exception for whenComplete
It failed
**** Success Mode ****
Breakable_exceptionally [-1]
Breakable_exceptionally [-2]
Breakable_exceptionally [-3]
Breakable_exceptionally [-4]
result: Breakable_exceptionally [-4]
Breakable_handle [-1]
Breakable_handle [-2]
Breakable_handle [-3]
Breakable_handle [-4]
result: Breakable_handle [-4] is good
Breakable_whenComplete [-1]
Breakable_whenComplete [-2]
Breakable_whenComplete [-3]
Breakable_whenComplete [-4]
Breakable_whenComplete [-4] OK
result: Breakable_whenComplete [-4]
 */
