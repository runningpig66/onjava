package ch26_concurrent;

import onjava.Nap;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 3月4日 周三
 * @time 14:34
 * P.252 §5.10 CompletableFuture §5.10.4 模拟场景应用
 * <p>
 * 下一步是将一份面糊分摊到 4 个平底锅中，然后开始烘焙。成品会以 CompletableFuture 类型的 Stream 形式返回：
 * <p>
 * notes: completablefuture_stream_demo_notes.md
 */
public class Baked {
    static class Pan {
    }

    static Pan pan(Batter b) {
        System.out.println("currentThread: [" + Thread.currentThread().getName() + "], " +
                "prepare: " + Pan.class.getSimpleName());
        new Nap(1.1);
        return new Pan();
    }

    static Baked heat(Pan p) {
        System.out.println("currentThread: [" + Thread.currentThread().getName() + "], " +
                "prepare: " + Baked.class.getSimpleName());
        new Nap(1.1);
        return new Baked();
    }

    static CompletableFuture<Baked> bake(CompletableFuture<Batter> cfb) {
        return cfb.thenApplyAsync(Baked::pan)
                .thenApplyAsync(Baked::heat);
    }

    public static Stream<CompletableFuture<Baked>> batch() {
        // 作者在这里其实是在演示并发编程中非常核心的一个设计模式：Fan-Out（一对多广播 / 数据分发）。
        // 场景本质：一个耗时的上游前置任务（准备面糊）完成后，它的结果需要作为输入，同时触发多个完全独立的下游并行任务。
        // 实际应用：在真实开发中，这通常用于不可变数据（Immutable Data）的并行分发。
        // 比如：上游任务从数据库拉取了一份“基础配置对象”（对应这里的 Batter），然后把它同时分发给 4 个不同的业务处理器
        // （比如一个发邮件、一个写日志、一个推消息）。因为配置对象是只读的、不可变的，所以不会被“消耗”，4 个线程共用一份数据非常安全。
        // 这里你觉得别扭，是因为物理世界的“消耗”与函数式编程的“不可变性”产生了冲突：
        // 在现实中，面糊是可变状态（Mutable State），倒进一个锅里，就少了一份，它被“消耗”了。
        // 而 CompletableFuture 推崇的是无状态的纯函数式编程（即前文的“自私儿童原则”）。
        // 在这里，正确的比喻不应该是“面糊”，而应该是一份“烤蛋糕的电子版图纸”。
        // 上游画好了图纸，下游 4 个厨师可以同时拿着这份图纸去各自的平底锅上烤蛋糕，图纸看多少遍都不会被消耗。
        CompletableFuture<Batter> batter = Batter.mix();
        return Stream.of(bake(batter), bake(batter), bake(batter), bake(batter));
    }
}
