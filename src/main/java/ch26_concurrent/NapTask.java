package ch26_concurrent;

import onjava.Nap;

/**
 * @author runningpig66
 * @date 3月2日 周一
 * @time 2:06
 * P.229 §5.8 创建和运行任务 §5.8.1 Task 和 Executor
 * <p>
 * 一开始，我们会创建一个几乎什么都不做的任务。它会“睡眠”（挂起执行）100毫秒，然后显示它的标识符和正在执行任务的 Thread 名称，最后结束：
 */
public class NapTask implements Runnable {
    final int id;

    public NapTask(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        new Nap(0.1); // Seconds
        System.out.println(this + " " + Thread.currentThread().getName());
    }

    @Override
    public String toString() {
        return "NapTask[" + id + "]";
    }
}
