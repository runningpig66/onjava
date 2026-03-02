package ch26_concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author runningpig66
 * @date 3月2日 周一
 * @time 4:07
 * P.231 §5.8 创建和运行任务 §5.8.1 Task 和 Executor
 * <p>
 * 一旦调用了 exec.shutdown()，此后再想提交新任务，就会抛出 RejectedExecutionException 异常：
 */
public class MoreTasksAfterShutdown {
    public static void main(String[] args) {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.execute(new NapTask(1));
        exec.shutdown();
        try {
            exec.execute(new NapTask(99));
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
/* Output:
java.util.concurrent.RejectedExecutionException: Task NapTask[99] rejected from java.util.concurrent.ThreadPoolExecutor@23fc625e[Shutting down, pool size = 1, active threads = 1, queued tasks = 0, completed tasks = 0]
NapTask[1] pool-1-thread-1
 */
