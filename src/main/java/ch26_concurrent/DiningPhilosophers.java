package ch26_concurrent;

import onjava.Nap;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.CompletableFuture;

/**
 * @author runningpig66
 * @date 3月6日 周五
 * @time 0:54
 * P.262 §5.11 死锁
 * Hidden deadlock
 * {ExcludeFromGradle} Gradle has trouble
 */
public class DiningPhilosophers {
    private StickHolder[] sticks;
    private static Philosopher[] philosophers;

    public DiningPhilosophers(int n) {
        sticks = new StickHolder[n];
        Arrays.setAll(sticks, i -> new StickHolder());
        philosophers = new Philosopher[n];
        Arrays.setAll(philosophers, i ->
                new Philosopher(i, sticks[i], sticks[(i + 1) % n])); // [1]
        // Fix by reversing stick order for this one:
        //- philosophers[1] = new Philosopher(0, sticks[0], sticks[1]); // [2]
        // 原书此处可能存在的笔误：[2] 处示例代码错误地篡改了座位号，并导致圆桌共享资源的物理闭环断裂。
        // 破坏死锁的正确实现应当保持座位号（1）不变，仅在实例化时将该哲学家的物理左手（sticks[1]）与物理右手（sticks[2]）作为参数对调传入。
        // 此举强制该哲学家优先获取物理左手资源，从而打破全体成员动作一致性，消除死锁的“循环等待”条件。
        philosophers[1] = new Philosopher(1, sticks[2], sticks[1]);
        Arrays.stream(philosophers)
                .forEach(CompletableFuture::runAsync); // [3]
    }

    public static void main(String[] args) {
        // Returns right away:
        new DiningPhilosophers(5); // [4]
        // Keeps main() from exiting:
        new Nap(3);
        // 破除死锁后引发的“饥饿（Starvation）”现象（资源分配极化）：P2 >>> P3 > P4 >>> P0, P1
        Arrays.stream(philosophers)
                .sorted(Comparator.comparingInt(p -> p.eatCount))
                .forEach(p -> System.out.println(p + " 干饭次数: " + p.eatCount));
    }
}
/* Output: (Example)
P0 eating
P0 eating
P0 eating
P0 eating
P1 eating
Shutdown
 */
