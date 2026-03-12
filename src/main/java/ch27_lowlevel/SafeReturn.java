package ch27_lowlevel;

/**
 * @author runningpig66
 * @date 3月12日 周四
 * @time 6:15
 * P.300 §6.5 原子性
 * <p>
 * 但是，Atomicity.test() 在遇到非偶数值时会失败。虽然 return i 确实是原子操作，但这里未加上同步，
 * 这会导致该值能在对象处于不稳定的中间状态时被读到。此外，i 也不是 volatile 的，因此会存在可见性问题。
 * getValue() 和 evenIncrement() 都必须是 synchronized 的（这样可以在 i 未被设为 volatile 的情况下，依然保证其线程安全性）：
 * <p>
 * 在 SafeReturn 示例中，evenIncrement() 方法包含两次 i++ 操作，这两条语句共同构成一个复合操作，期间 i 会短暂处于奇数中间状态。
 * 若仅将 i 声明为 volatile 并取消 getAsInt() 上的 synchronized，虽然能保证主线程读取到最新值，但 volatile 无法为复合操作提供互斥语义。
 * 由于读操作不再受锁保护，主线程可能在 evenIncrement() 执行中途切入，读取到奇数中间值，导致测试失败。
 * 因此，对于涉及复合操作的共享变量，读写两端必须使用相同的监视器锁，同时保证可见性与原子性。
 * <p>
 * 只有并发方面的专家才有足够的能力在这种情况下进行优化。再次强调，要应用 Brian 的同步法则。
 * <p>
 * notes: 多线程可见性失效分析：编译期指令重构与物理缓存隔离.md
 */
public class SafeReturn extends IntTestable {
    private int i = 0;

    @Override
    public synchronized int getAsInt() {
        return i;
    }

    @Override
    public synchronized void evenIncrement() {
        i++;
        i++;
    }

    public static void main(String[] args) {
        Atomicity.test(new SafeReturn());
    }
}
/* Output:
No failures found
 */
