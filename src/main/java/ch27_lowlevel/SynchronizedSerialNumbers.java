package ch27_lowlevel;

/**
 * @author runningpig66
 * @date 3月12日 周四
 * @time 10:34
 * P.302 §6.5 原子性 §6.5.1 Josh 的序列号
 * <p>
 * 注：可见性 ≠ 原子性。volatile 在这里仅起到了 1.禁用寄存器驻留（编译期） 2.强制主存读取 3.强制主存刷新的作用，
 * 但无法逾越复合操作的原子性缺失。要解决这个问题，就需要为 nextSerialNumber() 增加 synchronized 关键字：
 * int serialNumber 不再需要 volatile 了，因为 nextSerialNumber()方法的 synchronized 关键字确保实现了 volatile 的行为。
 */
public class SynchronizedSerialNumbers extends SerialNumbers {
    private int serialNumber = 0;

    @Override
    public synchronized int nextSerialNumber() {
        return serialNumber++;
    }

    public static void main(String[] args) {
        SerialNumberChecker.test(new SynchronizedSerialNumbers());
    }
}
/* Output:
No duplicates detected
 */
