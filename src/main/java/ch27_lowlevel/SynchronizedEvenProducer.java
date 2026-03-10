package ch27_lowlevel;

import onjava.Nap;

/**
 * @author runningpig66
 * @date 3月10日 周二
 * @time 4:05
 * P.292 §6.3 共享资源 §6.3.3 将 EvenProducer 同步化
 * Simplifying mutexes with the synchronized keyword
 * <p>
 * SynchronizedEvenProducer 是一个将 EvenProducer.java 中的 next() 方法
 * 修改为 synchronized 版本的 IntGenerator，这样可以阻止不符合预期的线程访问：
 */
public class SynchronizedEvenProducer extends IntGenerator {
    private int currentEvenValue = 0;

    @Override
    public synchronized int next() {
        ++currentEvenValue;
        new Nap(0.01); // Cause failure faster
        ++currentEvenValue;
        return currentEvenValue;
    }

    public static void main(String[] args) {
        EvenChecker.test(new SynchronizedEvenProducer());
    }
}
/* Output:
No odd numbers discovered
 */
