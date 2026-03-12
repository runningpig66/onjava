package ch27_lowlevel;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author runningpig66
 * @date 3月12日 周四
 * @time 12:47
 * P.304 §6.5 原子性 §6.5.2 原子类
 * <p>
 * 下面是使用了 AtomicInteger 的 SerialNumbers 实现：
 */
public class AtomicSerialNumbers extends SerialNumbers {
    private AtomicInteger serialNumber = new AtomicInteger();

    @Override
    public int nextSerialNumber() {
        return serialNumber.getAndIncrement();
    }

    public static void main(String[] args) {
        SerialNumberChecker.test(new AtomicSerialNumbers());
    }
}
/* Output:
No duplicates detected
 */
