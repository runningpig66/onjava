package ch27_lowlevel;

/**
 * @author runningpig66
 * @date 3月11日 周三
 * @time 6:36
 * P.299 §6.5 原子性
 * <p>
 * 我们很容易盲目地应用原子性的概念。此处，getAsInt() 看似是安全的原子操作：
 * <p>
 * 但是，Atomicity.test() 在遇到非偶数值时会失败。虽然 return i 确实是原子操作，但这里未加上同步，
 * 这会导致该值能在对象处于不稳定的中间状态时被读到。此外，i 也不是 volatile 的，因此会存在可见性问题。
 * getAsInt() 和 evenIncrement() 都必须是 synchronized 的（这样可以在 i 未被设为 volatile 的情况下，依然保证其线程安全性）：
 * <p>
 * notes: 多线程可见性失效分析：编译期指令重构与物理缓存隔离.md
 */
public class UnsafeReturn extends IntTestable {
    private int i = 0;

    @Override
    public int getAsInt() {
        return i;
    }

    @Override
    public synchronized void evenIncrement() {
        i++;
        i++;
    }

    public static void main(String[] args) {
        Atomicity.test(new UnsafeReturn());
    }
}
/* Output:
failed with: 23
 */
