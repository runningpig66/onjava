package ch26_concurrent;

import onjava.Nap;

import java.util.Random;
import java.util.concurrent.Callable;

/**
 * @author runningpig66
 * @date 3月2日 周一
 * @time 16:43
 * P.234 §5.8 创建和运行任务 §5.8.3 生成结果
 * <p>
 * 对于 InterferingTask，如果能消除副作用，并且只返回任务结果就好了。要达到这个目的，我们需要创建一个 Callable，而不是 Runnable：
 * call() 完全独立地生成结果，独立于任何其他的 CountingTask，这意味着并不存在可变共享状态。
 */
public class CountingTask implements Callable<Integer> {
    final int id;

    public CountingTask(int id) {
        this.id = id;
    }

    @Override
    public Integer call() {
        new Nap(new Random().nextDouble(1D, Math.nextUp(5D)));
        Integer val = 0;
        for (int i = 0; i < 100; i++) {
            val++;
        }
        System.out.println(id + " " + Thread.currentThread().getName() + " " + val);
        return val;
    }
}
