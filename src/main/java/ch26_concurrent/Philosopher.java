package ch26_concurrent;

/**
 * @author runningpig66
 * @date 3月6日 周五
 * @time 0:43
 * P.261 §5.11 死锁
 * <p>
 * 每个 Philosopher 都是一个任务，它尝试从左右两边 pickUp()（拿起）筷子来吃饭，然后通过 putDown() 来释放这些筷子：
 */
public class Philosopher implements Runnable {
    private final int seat;
    private final StickHolder left, right;
    public volatile int eatCount = 0; // 干饭计数器

    public Philosopher(int seat, StickHolder left, StickHolder right) {
        this.seat = seat;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return "P" + seat;
    }

    @Override
    public void run() {
        while (true) {
            // System.out.println("Thinking"); // [1]
            right.pickUp();
            left.pickUp();
            eatCount++;
            System.out.println(Thread.currentThread().getName() + this + " eating");
            right.putDown();
            left.putDown();
        }
    }
}
