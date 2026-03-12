package ch27_lowlevel;

/**
 * @author runningpig66
 * @date 3月12日 周四
 * @time 6:31
 * P.300 §6.5 原子性 §6.5.1 Josh 的序列号
 * <p>
 * 作为第二个示例，我们来考虑一件更简单的事：一个生成序列号的类，该类的灵感来自 Joshua Bloch 的
 * Effective Java Programming Language Guide 一书中的第 190 页。每次调用 nextSerialNumber() 都必须返回一个唯一值：
 * <p>
 * 注：可见性 ≠ 原子性。volatile 在这里仅起到了 1.禁用寄存器驻留（编译期） 2.强制主存读取 3.强制主存刷新的作用，但无法逾越复合操作的原子性缺失。
 */
public class SerialNumbers {
    private volatile int serialNumber = 0;

    public int nextSerialNumber() {
        return serialNumber++; // Not thread-safe
    }
}
