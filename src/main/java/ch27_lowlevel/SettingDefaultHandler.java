package ch27_lowlevel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author runningpig66
 * @date 3月9日 周一
 * @time 18:41
 * P.285 §6.2 捕获异常
 * <p>
 * 上例（CaptureUncaughtException.java）基于具体问题具体分析的方式设置处理器。如果你确定要在所有地方都应用同一个异常处理程序，
 * 更简单的方法是设置默认未捕获异常处理程序，该处理器会在 Thread 类内部设置一个静态字段：
 * <p>
 * 只有在为线程专门配置的未捕获异常处理程序不存在的时候，该处理器才会被调用。系统会检查是否存在为线程专门配置的处理器，如果不存在，
 * 则会检查该线程组是否专门实现了其 uncaughtException() 方法，如果没有，则会调用 defaultUncaughtExceptionHandler。
 */
public class SettingDefaultHandler {
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute(new ExceptionThread());
//        es.shutdown();
    }
}
/* Output:
caught java.lang.RuntimeException: hello
 */
