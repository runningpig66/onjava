package ch26_concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * @author runningpig66
 * @date 3月2日 周一
 * @time 6:18
 * P.233 §5.8 创建和运行任务 §5.8.2 使用更多的线程
 * <p>
 * 本类演示了非线程安全任务在无界并发环境下的数据覆盖现象。运行机制与现象：
 * 主线程在极短的时间内向 CachedThreadPool 提交了 10 个 InterferingTask 任务。
 * 根据 CachedThreadPool 的特性，它会立即创建并唤醒 10 个不同的物理线程来并行处理这些任务。
 * 由于这 10 个线程在互相平行的执行线上同时试图对单例的 val 进行写操作，
 * 导致了严重的数据覆盖与计算丢失。最终控制台输出的乱序及不规则残缺数值（如 197, 897 等），
 * 客观反映了在缺乏同步手段时，多线程并发修改共享可变状态所带来的破坏性结果。
 */
public class CachedThreadPool2 {
    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        IntStream.range(0, 10)
                .mapToObj(InterferingTask::new)
                .forEach(exec::execute);
        exec.shutdown();
    }
}
/* Output:
7 pool-1-thread-8 797
0 pool-1-thread-1 197
5 pool-1-thread-6 597
2 pool-1-thread-3 297
4 pool-1-thread-5 497
6 pool-1-thread-7 697
1 pool-1-thread-2 197
3 pool-1-thread-4 397
9 pool-1-thread-10 997
8 pool-1-thread-9 897
 */
