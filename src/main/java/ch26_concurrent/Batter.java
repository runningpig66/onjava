package ch26_concurrent;

import onjava.Nap;

import java.util.concurrent.CompletableFuture;

/**
 * @author runningpig66
 * @date 3月4日 周三
 * @time 12:28
 * P.248 §5.10 CompletableFuture §5.10.4 模拟场景应用
 * <p>
 * 为了示范如何通过 CompletableFuture 来将一系列的操作捆绑到一起，我们来模拟制作蛋糕的过程。
 * 第一步是准备配料（ingredient），并将它们混入面糊（batter）中；每种配料都需要一些时间来准备。
 * allOf() 会等待所有的配料准备完毕，然后再花些时间将它们混合并放入面糊中。
 * <p>
 * notes: completablefuture_stream_demo_notes.md
 */
public class Batter {
    static class Eggs {
    }

    static class Milk {
    }

    static class Sugar {
    }

    static class Flour {
    }

    static <T> T prepare(T ingredient) {
        System.out.println("currentThread: [" + Thread.currentThread().getName() + "], " +
                "prepare: " + ingredient.getClass().getSimpleName());
        new Nap(1.1);
        return ingredient;
    }

    static <T> CompletableFuture<T> prep(T ingredient) {
        return CompletableFuture.completedFuture(ingredient)
                .thenApplyAsync(Batter::prepare);
    }

    public static CompletableFuture<Batter> mix() {
        CompletableFuture<Eggs> eggs = prep(new Eggs());
        CompletableFuture<Milk> milk = prep(new Milk());
        CompletableFuture<Sugar> sugar = prep(new Sugar());
        CompletableFuture<Flour> flour = prep(new Flour());
        // 在这里，allOf().join() 的作用并非仅仅为了“防止 JVM 提前关闭（那只是测试代码的副作用）”，
        // 它真正的核心职责是作为一道并发屏障（Synchronization Barrier）：它强制要求当前线程在此处必须挂起，
        // 严格保证“四种原料 100% 准备就绪”这个前置条件达成后，主线程才能放行去执行下一步的 Nap(0.1) 搅拌动作。
        CompletableFuture.allOf(eggs, milk, sugar, flour).join();
        System.out.println("currentThread: [" + Thread.currentThread().getName() + "], " +
                "prepare: " + Batter.class.getSimpleName());
        new Nap(1.1); // Mixing time
        return CompletableFuture.completedFuture(new Batter());
    }
}
