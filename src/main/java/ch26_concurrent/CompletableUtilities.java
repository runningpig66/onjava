package ch26_concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author runningpig66
 * @date 3月4日 周三
 * @time 5:06
 * P.245 §5.10 CompletableFuture §5.10.2 其他操作
 * <p>
 * 下面这两个工具，用以简化代码并增加便利性：showr() 调用了 CompletableFuture<?> 的 get()，并显示了结果，同时对两个可能的异常进行了捕获。
 * voidr() 是 showr() 针对 CompletableFuture<Void> 的实现版本，具体来说，即针对只在任务完成或失败时存在以用来展示的 CompletableFuture。
 */
public class CompletableUtilities {
    // Get and show value stored in a CF:
    public static void showr(CompletableFuture<?> c) {
        try {
            System.out.println(c.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    // For CF operations that have no value:
    public static void voidr(CompletableFuture<Void> c) {
        try {
            c.get(); // Returns Void
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
