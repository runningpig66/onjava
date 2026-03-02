package ch26_concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author runningpig66
 * @date 3月3日 周二
 * @time 3:02
 * P.237 §5.8 创建和运行任务 §5.8.4 作为任务的 lambda 与方法引用
 * <p>
 * 有了 lambda 和方法引用，你将不再仅限于使用 Runnable 和 Callable。由于 Java 8 通过匹配签名的方式支持 lambda 和方法引用
 * [也就是支持结构一致性（structural conformance）]，我们可以将非 Runnable 或 Callable 类型的参数传递给 ExecutorService：
 */
class NotRunnable {
    public void go() {
        System.out.println("NotRunnable");
    }
}

class NotCallable {
    public Integer get() {
        System.out.println("NotCallable");
        return 1;
    }
}

public class LambdasAndMethodReferences {
    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();

        // 此处，前两次 submit() 调用可以替代为 execute() 调用。
        // Future<?> submit(Runnable task);
        exec.submit(() -> System.out.println("Lambda1"));
        exec.submit(new NotRunnable()::go);

        // 所有的 submit() 调用都会返回 Future，你可以在第二次的两个调用中用它们来提取结果。
        // <T> Future<T> submit(Callable<T> task);
        exec.submit(() -> {
            System.out.println("Lambda2");
            return 1;
        });
        exec.submit(new NotCallable()::get);
        exec.shutdown();
    }
}
/* Output:
Lambda1
NotRunnable
Lambda2
NotCallable
 */
