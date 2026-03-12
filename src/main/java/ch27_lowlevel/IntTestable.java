package ch27_lowlevel;

import java.util.function.IntSupplier;

/**
 * @author runningpig66
 * @date 3月11日 周三
 * @time 6:26
 * P.299 §6.5 原子性
 * <p>
 * 我们用下面这段代码来测试原子性的这个概念：定义一个抽象类，在其中写一个方法，将一个整型变量按偶数值自增，然后由 run() 方法持续调用该方法。
 * <p>
 * notes: 多线程可见性失效分析：编译期指令重构与物理缓存隔离.md
 */
public abstract class IntTestable implements Runnable, IntSupplier {
    abstract void evenIncrement();

    @Override
    public void run() {
        while (true) {
            evenIncrement();
        }
    }
}
