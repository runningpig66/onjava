package ch27_lowlevel;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author runningpig66
 * @date 3月12日 周四
 * @time 11:37
 * P.303 §6.5 原子性 §6.5.2 原子类
 * Atomic classes: occasionally useful in regular code
 * <p>
 * 现在可以重写 SynchronizedEvenProducer.java 以使用 AtomicInteger 了；通过 AtomicInteger，再一次去除了所有其他形式的同步。
 */
public class AtomicEvenProducer extends IntGenerator {
    private AtomicInteger currentEvenValue = new AtomicInteger(0);

    @Override
    public int next() {
        return currentEvenValue.addAndGet(2);
    }

    public static void main(String[] args) {
        EvenChecker.test(new AtomicEvenProducer());
    }
}
/* Output:
No odd numbers discovered
 */
