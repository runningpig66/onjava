package onjava;

import java.util.concurrent.TimeUnit;

/**
 * @author runningpig66
 * @date 3月2日 周一
 * @time 2:09
 * P.229 §5.8 创建和运行任务 §5.8.1 Task 和 Executor
 * <p>
 * 我们通过 Nap 类来实现“睡眠”：为了消除异常处理产生的视觉干扰，它被定义为公共工具类。第二个构造器在时间结束后会显示出相关信息。
 */
public class Nap {
    public Nap(double t) { // Seconds
        try {
            TimeUnit.MILLISECONDS.sleep((int) (1000 * t));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Nap(double t, String msg) {
        this(t);
        System.out.println(msg);
    }
}
