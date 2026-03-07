package ch26_concurrent;

/**
 * @author runningpig66
 * @date 3月7日 周六
 * @time 18:07
 * P.268 §5.12 构造器并不是线程安全的
 * <p>
 * 另一种方法是将构造器设为私有的（因此会阻止继承），并实现一个静态的工厂方法来生成新的对象：
 */
// 语义说明：虽然 private 构造器已能阻断外部类的常规继承，但追加 final 修饰符具有双重工程约束：
// 1. 弥补语法漏洞：彻底封死同顶级类内部的嵌套类（以及 CGLib 等动态代理底层技术）绕过 private 权限进行派生继承的可能。
// 2. 显式设计契约：在类声明处直观且强制地表明该类完全封闭的架构意图，降低后续维护的认知成本。
final class SyncFactory implements HasID {
    private final int id;

    private SyncFactory(SharedArg sa) {
        id = sa.get();
    }

    @Override
    public int getID() {
        return id;
    }

    // 通过将静态工厂方法设为同步的，实际上是在构造过程中对 Class 对象上了锁（即 SyncFactory.class）。
    public static synchronized SyncFactory factory(SharedArg sa) {
        return new SyncFactory(sa);
    }
}

public class SynchronizedFactory {
    public static void main(String[] args) {
        Unsafe unsafe = new Unsafe();
        IDChecker.test(() -> SyncFactory.factory(unsafe));
    }
}
/* Output:
0
 */
