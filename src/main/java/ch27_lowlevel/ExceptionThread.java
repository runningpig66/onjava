package ch27_lowlevel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author runningpig66
 * @date 3月9日 周一
 * @time 5:15
 * P.283 §6.2 捕获异常
 * {ThrowsException}
 * <p>
 * 你无法捕获已逃逸线程的异常。一旦异常逃逸到任务的 run() 方法之外，便会扩散到控制台，除非采用专门的步骤来捕获这种不正确的异常。
 * 下面这个任务抛出了传播到其 run() 方法之外的异常，而 main() 则显示了运行该任务时发生的情况：
 * <p>
 * 本示例演示了并发环境下传统 try-catch 异常捕获机制的失效现象。在使用 execute 方法提交异步任务时，主线程试图用 try-catch 块来拦截任务内部抛出的异常，这种做法是无效的。
 * 由于 execute 方法的异步特性，任务的提交与实际执行发生在不同的线程上下文中。主线程在将任务加入线程池内部队列后，会立即继续向下执行并脱离当前的 try-catch 监控范围。
 * 随后，任务在后台工作线程的独立调用栈中运行。若任务内部抛出未检查异常，该异常仅会沿着工作线程自身的调用栈进行回溯。物理层面的线程调用栈隔离，
 * 导致主线程的 catch 块无法拦截此异常。最终，异常将击穿工作线程边界发生逃逸，交由 JVM 默认的未捕获异常处理器接管，并将完整的错误堆栈输出至控制台。
 * 此示例表明，在并发编程中处理跨线程的逃逸异常时，必须摒弃依赖词法作用域的常规捕获模式，转而为底层线程配置专门的未捕获异常处理器。
 */
public class ExceptionThread implements Runnable {
    @Override
    public void run() {
        throw new RuntimeException("hello");
    }

    public static void main(String[] args) {
        ExecutorService es = Executors.newCachedThreadPool();
        // es.execute(new ExceptionThread());
        try {
            // 试图在主线程里捕获子线程的异常
            es.execute(new ExceptionThread());
        } catch (Exception e) {
            // 这里的代码永远不会被执行！
            System.out.println("主线程捕获到了异常：" + e.getMessage());
        }
        es.shutdown();
    }
}
/* Output:
Exception in thread "pool-1-thread-1" java.lang.RuntimeException: hello
	at ch27_lowlevel.ExceptionThread.run(ExceptionThread.java:25)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
	at java.base/java.lang.Thread.run(Thread.java:1583)
 */
