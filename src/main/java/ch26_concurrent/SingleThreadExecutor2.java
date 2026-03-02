package ch26_concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * @author runningpig66
 * @date 3月2日 周一
 * @time 4:00
 * P.231 §5.8 创建和运行任务 §5.8.1 Task 和 Executor
 * <p>
 * 如果你仅仅调用 exec.shutdown()，程序会在所有任务完成后立即结束。也就是说，while(!exec.isTerminated()) 并不是必需的：
 */
public class SingleThreadExecutor2 {
    public static void main(String[] args) {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        IntStream.range(0, 10)
                .mapToObj(NapTask::new)
                .forEach(exec::execute);
        exec.shutdown();
    }
}
/* Output:
NapTask[0] pool-1-thread-1
NapTask[1] pool-1-thread-1
NapTask[2] pool-1-thread-1
NapTask[3] pool-1-thread-1
NapTask[4] pool-1-thread-1
NapTask[5] pool-1-thread-1
NapTask[6] pool-1-thread-1
NapTask[7] pool-1-thread-1
NapTask[8] pool-1-thread-1
NapTask[9] pool-1-thread-1
 */
