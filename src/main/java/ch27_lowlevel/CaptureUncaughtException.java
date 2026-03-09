package ch27_lowlevel;

import org.jspecify.annotations.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author runningpig66
 * @date 3月9日 周一
 * @time 17:28
 * P.284 §6.2 捕获异常
 * <p>
 * Thread.UncaughtExceptionHandler 接口会向每个 Thread 对象添加异常处理程序。当线程由于未捕获的异常而即将消亡时，
 * Thread.UncaughtExceptionHandler.uncaughtException() 就会被自动调用。要使用该方法，
 * 可以创建一个新的 ThreadFactory 类型，它会为它所创建的每个新 Thread 对象添加一个新的 Thread.UncaughtExceptionHandler。
 * 将这个 ThreadFactory 工厂传入 Executors.newCachedThreadPool 方法，该方法会创建新 ExecutorService：
 * <p>
 * 从附加的跟踪代码可以验证得知，工厂所创建的线程获得了新的 UncaughtExceptionHandler。未捕获的异常现在被 uncaughtException 捕获了。
 */
class ExceptionThread2 implements Runnable {
    @Override
    public void run() {
        Thread t = Thread.currentThread();
        System.out.println("run() by " + t.getName());
        System.out.println("eh = " + t.getUncaughtExceptionHandler());
        throw new RuntimeException();
    }
}

class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("caught " + e);
    }
}

class HandlerThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(@NonNull Runnable r) {
        System.out.println(this + " creating new Thread");
        Thread t = new Thread(r);
        System.out.println("created " + t);
        // void setUncaughtExceptionHandler(UncaughtExceptionHandler ueh)
        t.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
        System.out.println("eh = " + t.getUncaughtExceptionHandler());
        return t;
    }
}

public class CaptureUncaughtException {
    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool(new HandlerThreadFactory());
        exec.execute(new ExceptionThread2());
        exec.shutdown();
    }
}
/* Output:
ch27_lowlevel.HandlerThreadFactory@6b884d57 creating new Thread
created Thread[#29,Thread-0,5,main]
eh = ch27_lowlevel.MyUncaughtExceptionHandler@65ab7765
run() by Thread-0
eh = ch27_lowlevel.MyUncaughtExceptionHandler@65ab7765
caught java.lang.RuntimeException
 */
