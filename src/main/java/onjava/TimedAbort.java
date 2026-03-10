package onjava;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author runningpig66
 * @date 3月9日 周一
 * @time 22:12
 * P.288 §6.3 共享资源
 * Terminate a program after t seconds
 */
public class TimedAbort {
    private volatile boolean restart = true;

    // 我们用 lambda 表达式创建了一个 Runnable，通过 CompletableFuture 的 static runAsync() 方法来执行。
    // 使用 runAsync() 的价值是可以立刻返回调用。因此，TimedAbort 不会让任何本来可以完成的任务保持开启状态，
    // 不过如果执行时间太长，它仍然会终止该任务 [TimedAbort 有时称为守护进程 ( daemon ) ]。
    public TimedAbort(double t, String msg) {
        CompletableFuture.runAsync(() -> {
            try {
                while (restart) {
                    restart = false;
                    TimeUnit.MILLISECONDS.sleep((int) (1000 * t));
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(msg);
            // static void exit(int status)
            // 调用此方法将立即终止当前正在运行的 Java 虚拟机（JVM）进程。无论调用发起于主线程、普通用户子线程还是后台守护线程，
            // 整个 JVM 都会直接停止所有正在执行的任务和控制流，并以指定的状态码退出程序。
            System.exit(0);
        });
    }

    public TimedAbort(double t) {
        this(t, "TimedAbort " + t);
    }

    // TimedAbort 同样允许我们将其 restart()（重启），这是为了在需要持续运行某些必要的操作时，可以让程序一直保持运行。
    public void restart() {
        restart = true;
    }
}
