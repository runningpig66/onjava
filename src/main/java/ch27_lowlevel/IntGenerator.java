package ch27_lowlevel;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author runningpig66
 * @date 3月9日 周一
 * @time 21:41
 * P.287 §6.3 共享资源
 * <p>
 * 看看下面这个示例，一个任务会生成偶数，而其他任务则会消费这些偶数。此处，消费者任务唯一要做的事就是检查这些偶数的有效性。
 * 这个示例中会定义 EvenChecker，即消费者任务，并使其在后续的示例中也能够复用。
 * 为了将 EvenChecker 从我们的各种实验用生成器中分离出来，首先创建一个名为 IntGenerator 的抽象类，
 * 它包含了 EvenChecker 需要用到的最小必要方法集——next() 方法，而且它可以被取消。
 */
public abstract class IntGenerator {
    private AtomicBoolean canceled = new AtomicBoolean();

    public abstract int next();

    public void cancel() {
        canceled.set(true);
    }

    public boolean isCanceled() {
        return canceled.get();
    }
}
