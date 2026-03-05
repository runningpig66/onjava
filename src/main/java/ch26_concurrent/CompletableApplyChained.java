package ch26_concurrent;

import onjava.Timer;

import java.util.concurrent.CompletableFuture;

/**
 * @author runningpig66
 * @date 3月4日 周三
 * @time 3:25
 * P.243 §5.10 CompletableFuture §5.10.1 基本用法
 * <p>
 * 我们可以消除中间变量，将多个操作串联起来，就像我们使用 Stream 时那样（使用 CompletableFuture）：
 */
public class CompletableApplyChained {
    public static void main(String[] args) {
        Timer timer = new Timer();
        CompletableFuture<Machina> cf = CompletableFuture.completedFuture(new Machina(0))
                .thenApply(Machina::work)
                .thenApply(Machina::work)
                .thenApply(Machina::work)
                .thenApply(Machina::work);
        System.out.println(timer.duration());
    }
}
/* Output:
currentThread-main: Machina0: ONE
currentThread-main: Machina0: TWO
currentThread-main: Machina0: THREE
currentThread-main: Machina0: complete
4062
 */
