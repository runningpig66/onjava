package ch26_concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * @author runningpig66
 * @date 3月2日 周一
 * @time 6:29
 * P.233 §5.8 创建和运行任务 §5.8.2 使用更多的线程
 * <p>
 * 本类演示了通过“线程封闭 (thread confinement)”机制解决多线程数据冲突的方案。运行机制与结论：
 * 底层使用 SingleThreadExecutor，将所有提交的任务置于排队队列中，由唯一的后台工作线程依次串行执行。
 * 由于同一时刻绝对只会运行一项任务，这些任务永远不会互相影响。
 * 这种机制在物理层面上隔离了竞态条件，确保了即使任务类（InterferingTask）本身缺乏线程安全保障，
 * 也能安全地得出一致且准确的递增结果（输出严格递增至 1000）。
 * 将多个任务限制在单线程上执行，虽然限制了并发提速，但有效规避了数据同步的开销与底层调试成本。
 */
public class SingleThreadExecutor3 {
    public static void main(String[] args) {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        IntStream.range(0, 10)
                .mapToObj(InterferingTask::new)
                .forEach(exec::execute);
        exec.shutdown();
    }
}
/* Output:
0 pool-1-thread-1 100
1 pool-1-thread-1 200
2 pool-1-thread-1 300
3 pool-1-thread-1 400
4 pool-1-thread-1 500
5 pool-1-thread-1 600
6 pool-1-thread-1 700
7 pool-1-thread-1 800
8 pool-1-thread-1 900
9 pool-1-thread-1 1000
 */
