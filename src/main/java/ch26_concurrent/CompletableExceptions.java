package ch26_concurrent;

import java.util.concurrent.CompletableFuture;

/**
 * @author runningpig66
 * @date 3月5日 周四
 * @time 16:44
 * P.254 §5.10 CompletableFuture §5.10.5 异常
 * <p>
 * 在随后的 test() 方法中，work() 被多次应用于 Breakable，所以如果 failcount 在范围内，则会抛出异常。
 * 不过，在从 A 到 E 的测试中，你可以从输出看到异常被抛出，但并不会显露出来：
 */
public class CompletableExceptions {
    static CompletableFuture<Breakable> test(String id, int failcount) {
        return CompletableFuture.completedFuture(new Breakable(id, failcount))
                .thenApply(Breakable::work)
                .thenApply(Breakable::work)
                .thenApply(Breakable::work)
                .thenApply(Breakable::work);
    }

    public static void main(String[] args) {
        // Exceptions don't appear ...
        test("A", 1);
        test("B", 2);
        test("C", 3);
        test("D", 4);
        test("E", 5);
        // ... until you try to fetch the value:
        try {
            // 从 A 到 E 的测试执行到抛出异常的节点时，什么都没有发生。只有在测试 F 中调用 get() 时，才会看到抛出的异常。
            test("F", 2).get(); // or join()

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // Test for exceptions:
        System.out.println(test("G", 2).isCompletedExceptionally());
        // Counts as "done":
        System.out.println(test("H", 2).isDone());
        // Force an exception:
        CompletableFuture<Integer> cfi = new CompletableFuture<>();
        System.out.println("done? " + cfi.isDone());
        // boolean completeExceptionally(Throwable ex)
        // 即使后台没有真的发生任何故障导致任务失败，你也可以人为地向一个（尚未完成的）CompletableFuture 中强行插入一个异常（完成）状态。
        // 如果当前任务尚未完成，调用此方法会立即使其进入异常结束状态并返回 true。
        // 此后任何试图获取该任务结果的操作（如 get() 或 join()）都会抛出此处传入的异常。
        // 如果返回 false，意味着该任务在此之前就已经完成了（无论是正常计算完毕，还是已经被标记过其他异常）；
        // 在这种情况下，当前方法调用无效，新传入的异常会被直接丢弃，Future 依然保持它原来的完成状态，不会被二次覆盖。
        cfi.completeExceptionally(new RuntimeException("forced"));
        try {
            cfi.get();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
/* Output:
Throwing Exception for A
Breakable_B [1]
Throwing Exception for B
Breakable_C [2]
Breakable_C [1]
Throwing Exception for C
Breakable_D [3]
Breakable_D [2]
Breakable_D [1]
Throwing Exception for D
Breakable_E [4]
Breakable_E [3]
Breakable_E [2]
Breakable_E [1]
Breakable_F [1]
Throwing Exception for F
java.lang.RuntimeException: Breakable_F failed
Breakable_G [1]
Throwing Exception for G
true
Breakable_H [1]
Throwing Exception for H
true
done? false
java.lang.RuntimeException: forced
 */
