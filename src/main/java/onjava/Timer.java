package onjava;

import java.util.concurrent.TimeUnit;

/**
 * @author runningpig66
 * @date 2025/11/23 周日
 * @time 4:37
 * P.512 §16.6 基准测试 §16.6.1 微基准测试
 * <p>
 * 我们可以编写一个计时实用程序来比较不同代码段的运行速度，这个想法很诱人，看起来好像能生成一些有用的数据。
 * 例如，我们有一个简单的 Timer 类，它可以通过如下两种方式使用。
 * 1. 创建一个 Timer 对象，执行你想要的操作，然后在这个 Timer 对象上调用 duration() 方法，来生成以毫秒为单位的用时。
 * 2. 将 Runnable 传递给静态方法 duration(). 符合 Runnable 接口的类都会有一个函数式方法 run(),
 * 这个函数式方法有一个不带参数也不返回任何东西的函数签名。
 */
public class Timer {
    private long start = System.nanoTime();

    public long duration() {
        return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
    }

    public static long duration(Runnable test) {
        Timer timer = new Timer();
        test.run();
        return timer.duration();
    }
}
