package ch26_concurrent;

import onjava.Nap;
import onjava.Timer;

import java.util.concurrent.CompletableFuture;

/**
 * @author runningpig66
 * @date 3月4日 周三
 * @time 15:22
 * P.252 §5.10 CompletableFuture §5.10.4 模拟场景应用
 * <p>
 * 最后，我们创建了一份 Frosting（糖霜），并将其洒在蛋糕上：
 * <p>
 * notes: completablefuture_stream_demo_notes.md
 */
final class Frosting {
    private Frosting() {
    }

    static CompletableFuture<Frosting> make() {
        System.out.println("currentThread: [" + Thread.currentThread().getName() + "], " +
                "prepare: " + Frosting.class.getSimpleName());
        new Nap(1.1);
        return CompletableFuture.completedFuture(new Frosting());
    }
}

public class FrostedCake {
    public FrostedCake(Baked baked, Frosting frosting) {
        System.out.println("currentThread: [" + Thread.currentThread().getName() + "], " +
                "prepare: " + FrostedCake.class.getSimpleName());
        new Nap(1.1);
    }

    @Override
    public String toString() {
        return "currentThread: [" + Thread.currentThread().getName() + "], " +
                "FrostedCake";
    }

    public static void main(String[] args) {
        Timer timer = new Timer();

        Baked.batch().forEach(baked -> baked
                .thenCombineAsync(Frosting.make(), FrostedCake::new)
                .thenAcceptAsync(System.out::println)
                .join());

        /*CompletableFuture<?>[] futures = Baked.batch()
                .map(baked -> baked
                        .thenCombineAsync(Frosting.make(), FrostedCake::new)
                        .thenAcceptAsync(System.out::println))
                .parallel()
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures).join();*/

        System.out.println(timer.duration());
    }
}
/* Output 1: (have join())
currentThread: [ForkJoinPool.commonPool-worker-1], prepare: Eggs
currentThread: [ForkJoinPool.commonPool-worker-3], prepare: Sugar
currentThread: [ForkJoinPool.commonPool-worker-2], prepare: Milk
currentThread: [ForkJoinPool.commonPool-worker-4], prepare: Flour
currentThread: [main], prepare: Batter
currentThread: [ForkJoinPool.commonPool-worker-3], prepare: Pan
currentThread: [ForkJoinPool.commonPool-worker-2], prepare: Pan
currentThread: [ForkJoinPool.commonPool-worker-4], prepare: Pan
currentThread: [ForkJoinPool.commonPool-worker-1], prepare: Pan
currentThread: [main], prepare: Frosting
currentThread: [ForkJoinPool.commonPool-worker-1], prepare: Baked
currentThread: [ForkJoinPool.commonPool-worker-4], prepare: Baked
currentThread: [ForkJoinPool.commonPool-worker-3], prepare: Baked
currentThread: [ForkJoinPool.commonPool-worker-2], prepare: Baked
currentThread: [ForkJoinPool.commonPool-worker-3], prepare: FrostedCake
currentThread: [ForkJoinPool.commonPool-worker-3], FrostedCake
currentThread: [main], prepare: Frosting
currentThread: [ForkJoinPool.commonPool-worker-3], prepare: FrostedCake
currentThread: [ForkJoinPool.commonPool-worker-3], FrostedCake
currentThread: [main], prepare: Frosting
currentThread: [ForkJoinPool.commonPool-worker-3], prepare: FrostedCake
currentThread: [ForkJoinPool.commonPool-worker-3], FrostedCake
currentThread: [main], prepare: Frosting
currentThread: [ForkJoinPool.commonPool-worker-3], prepare: FrostedCake
currentThread: [ForkJoinPool.commonPool-worker-3], FrostedCake
12197
 */
