package ch26_concurrent.notes;

import ch26_concurrent.Baked;
import onjava.Nap;
import onjava.Timer;

import java.util.concurrent.CompletableFuture;

/**
 * @author runningpig66
 * @date 3月5日 周四
 * @time 11:43
 * P.252 §5.10 CompletableFuture §5.10.4 模拟场景应用
 * <p>
 * notes: completablefuture_stream_demo_notes.md
 */
final class Frosting {
    private Frosting() {
    }

    // 1. 使用 supplyAsync 将耗时操作真正移交到底层线程池
    static CompletableFuture<Frosting> make() {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("currentThread: [" + Thread.currentThread().getName() + "], " +
                    "prepare: " + Frosting.class.getSimpleName());
            new Nap(1.1);
            return new Frosting();
        });
    }
}

public class FrostedCake2 {
    public FrostedCake2(Baked baked, Frosting frosting) {
        System.out.println("currentThread: [" + Thread.currentThread().getName() + "], " +
                "prepare: " + FrostedCake2.class.getSimpleName());
        new Nap(1.1);
    }

    @Override
    public String toString() {
        return "currentThread: [" + Thread.currentThread().getName() + "], " + "FrostedCake2";
    }

    public static void main(String[] args) {
        Timer timer = new Timer();

        // 2 将 forEach 替换为 map，只派发任务，不中途阻塞
        CompletableFuture<?>[] futures = Baked.batch()
                .map(baked -> baked
                        // 此时 Frosting.make() 瞬间返回异步票据，主线程不会卡顿
                        .thenCombineAsync(Frosting.make(), FrostedCake2::new)
                        .thenAcceptAsync(System.out::println))
                // 将所有流水线的末端凭证收集到数组中
                .toArray(CompletableFuture[]::new);
        // 3：在主线程快速派发完 4 条流水线后，统一设立全局并发屏障
        CompletableFuture.allOf(futures).join();

        System.out.println(timer.duration());
    }
}
/* Output:
currentThread: [ForkJoinPool.commonPool-worker-2], prepare: Milk
currentThread: [ForkJoinPool.commonPool-worker-1], prepare: Eggs
currentThread: [ForkJoinPool.commonPool-worker-3], prepare: Sugar
currentThread: [ForkJoinPool.commonPool-worker-4], prepare: Flour
currentThread: [main], prepare: Batter
currentThread: [ForkJoinPool.commonPool-worker-2], prepare: Pan
currentThread: [ForkJoinPool.commonPool-worker-3], prepare: Pan
currentThread: [ForkJoinPool.commonPool-worker-4], prepare: Pan
currentThread: [ForkJoinPool.commonPool-worker-1], prepare: Pan
currentThread: [ForkJoinPool.commonPool-worker-5], prepare: Frosting
currentThread: [ForkJoinPool.commonPool-worker-6], prepare: Frosting
currentThread: [ForkJoinPool.commonPool-worker-7], prepare: Frosting
currentThread: [ForkJoinPool.commonPool-worker-8], prepare: Frosting
currentThread: [ForkJoinPool.commonPool-worker-4], prepare: Baked
currentThread: [ForkJoinPool.commonPool-worker-5], prepare: Baked
currentThread: [ForkJoinPool.commonPool-worker-6], prepare: Baked
currentThread: [ForkJoinPool.commonPool-worker-7], prepare: Baked
currentThread: [ForkJoinPool.commonPool-worker-6], prepare: FrostedCake2
currentThread: [ForkJoinPool.commonPool-worker-4], prepare: FrostedCake2
currentThread: [ForkJoinPool.commonPool-worker-7], prepare: FrostedCake2
currentThread: [ForkJoinPool.commonPool-worker-5], prepare: FrostedCake2
currentThread: [ForkJoinPool.commonPool-worker-6], FrostedCake2
currentThread: [ForkJoinPool.commonPool-worker-4], FrostedCake2
currentThread: [ForkJoinPool.commonPool-worker-9], FrostedCake2
currentThread: [ForkJoinPool.commonPool-worker-7], FrostedCake2
5572
 */
