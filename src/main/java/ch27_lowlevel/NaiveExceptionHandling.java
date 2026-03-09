package ch27_lowlevel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author runningpig66
 * @date 3月9日 周一
 * @time 17:10
 * P.283 §6.2 捕获异常
 * {ThrowsException}
 * <p>
 * 将 main() 方法体包裹在 try-catch 块中也无法成功运行。参照：ExceptionThread.java 注释。
 */
public class NaiveExceptionHandling {
    public static void main(String[] args) {
        ExecutorService es = Executors.newCachedThreadPool();
        try {
            es.execute(new ExceptionThread());
        } catch (RuntimeException ue) {
            // This statement will NOT execute!
            System.out.println("Exception was handled!");
        } finally {
            es.shutdown();
        }
    }
}
/* Output:
Exception in thread "pool-1-thread-1" java.lang.RuntimeException: hello
	at ch27_lowlevel.ExceptionThread.run(ExceptionThread.java:25)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1144)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:642)
	at java.base/java.lang.Thread.run(Thread.java:1583)
 */
