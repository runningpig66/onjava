package ch26_concurrent;

/**
 * @author runningpig66
 * @date 3月7日 周六
 * @time 17:29
 * P.267 §5.12 构造器并不是线程安全的
 * <p>
 * 虽然语言层面并不支持 synchronized 修饰的构造器，但是可以通过 synchronized 语句块，来创建自己的 (同步) 构造器
 * (要了解 synchronized 关键字，请参阅本书第 6 章)。虽然 JLS 声明 “......这会阻塞正在创建的对象”，但这并不是真的——
 * 构造器事实上是个静态方法，因此 synchronized 的构造器实际上会阻塞 Class 对象。我们可以通过创建自己的静态对象并对它上锁，来复现该过程：
 */
class SyncConstructor implements HasID {
    private final int id;
    private static Object constructorLock = new Object();

    SyncConstructor(SharedArg sa) {
        synchronized (constructorLock) {
            id = sa.get();
        }
    }

    @Override
    public int getID() {
        return id;
    }
}

public class SynchronizedConstructor {
    public static void main(String[] args) {
        Unsafe unsafe = new Unsafe();
        // 对 unsafe 类的共享现在是安全的了。
        IDChecker.test(() -> new SyncConstructor(unsafe));
    }
}
/* Output:
0
 */
