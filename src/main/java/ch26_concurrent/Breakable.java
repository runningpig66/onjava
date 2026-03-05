package ch26_concurrent;

/**
 * @author runningpig66
 * @date 3月5日 周四
 * @time 16:33
 * P.253 §5.10 CompletableFuture §5.10.5 异常
 * <p>
 * 和 CompletableFuture 对处理链中的对象加以包装的方式一样，它还可以缓存异常。
 * 在处理过程中调用者并不会对此有所感知，这种效果只会在尝试提取结果时体现出来。
 * 为了演示其运作机制，我们先来创建一个会在特定条件下抛出异常的类。
 * <p>
 * 通过正整型的 failcount（失败数），每次向 work() 方法传递对象，failcount 都会递减。
 * 当它等于 0 的时候，work() 会抛出异常。如果直接传入值为 0 的 failcount，则永远不会抛出异常。
 */
public class Breakable {
    String id;
    private int failcount;

    public Breakable(String id, int failcount) {
        this.id = id;
        this.failcount = failcount;
    }

    @Override
    public String toString() {
        return "Breakable_" + id + " [" + failcount + "]";
    }

    public static Breakable work(Breakable b) {
        if (--b.failcount == 0) {
            System.out.println("Throwing Exception for " + b.id);
            throw new RuntimeException("Breakable_" + b.id + " failed");
        }
        System.out.println(b);
        return b;
    }
}
