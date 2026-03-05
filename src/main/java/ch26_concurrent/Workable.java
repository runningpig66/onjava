package ch26_concurrent;

import onjava.Nap;

import java.util.concurrent.CompletableFuture;

/**
 * @author runningpig66
 * @date 3月4日 周三
 * @time 8:34
 * P.248 §5.10 CompletableFuture §5.10.3 合并多个 CompletableFuture
 * <p>
 * 为了测试上述方法，我们会创建一个任务，该任务的参数之一是完成该任务所需的时长，由此我们可以控制首先完成哪个 CompletableFuture：
 */
public class Workable {
    String id;
    final double duration;

    public Workable(String id, double duration) {
        this.id = id;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Workable[" + id + "]";
    }

    public static Workable work(Workable tt) {
        new Nap(tt.duration); // Seconds
        tt.id = tt.id + "W";
        System.out.println(tt);
        return tt;
    }

    public static CompletableFuture<Workable> make(String id, double duration) {
        return CompletableFuture.completedFuture(new Workable(id, duration))
                .thenApplyAsync(Workable::work);
    }
}
