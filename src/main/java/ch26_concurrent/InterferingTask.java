package ch26_concurrent;

/**
 * @author runningpig66
 * @date 3月2日 周一
 * @time 6:12
 * P.232 §5.8 创建和运行任务 §5.8.2 使用更多的线程
 * <p>
 * 本类定义了一个非线程安全的任务模型。隐患分析：
 * 1. 共享状态：变量 val 被声明为 static，意味着它是分配在方法区（或元空间）的类级共享变量。
 * 所有 InterferingTask 的实例在执行时，访问的都是同一块物理内存地址。
 * 2. 非原子操作：代码段 `val++` 属于“读取-修改-回写”的复合操作。在没有锁机制保障的情况下，
 * 多线程并发执行该代码极易产生“丢失更新 (Lost Update)”现象。
 */
public class InterferingTask implements Runnable {
    final int id;
    private static Integer val = 0;

    public InterferingTask(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            val++;
        }
        System.out.println(id + " " + Thread.currentThread().getName() + " " + val);
    }
}
