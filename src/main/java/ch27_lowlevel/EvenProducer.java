package ch27_lowlevel;

/**
 * @author runningpig66
 * @date 3月9日 周一
 * @time 23:08
 * P.290 §6.3 共享资源
 * When threads collide
 * {VisuallyInspectOutput}
 * <p>
 * 下面我们会看到第一种 IntGenerator 的实现 ( EvenProducer )，其中的 next() 方法用来生成一系列偶数值：
 * <p>
 * 要注意，自增操作本身也包括多个执行步骤，而任务有可能在自增操作的中途被线程机制挂起。
 * 也就是说，Java 中的自增操作并不是原子操作。因此即使是简单的自增操作，如果不对任务进行必要的保护，也是不安全的。
 * 该程序并不总是在首次生成非偶数值时就终止。所有的任务都不会立刻终止，这也是并发程序的常态了。
 */
public class EvenProducer extends IntGenerator {
    private int currentEvenValue = 0;

    @Override
    public int next() {
        ++currentEvenValue;
        // [1] 有可能出现这样的情况：一个任务在另一个任务执行完 currentEvenValue 的第一次自增之后，
        // 但在它执行第二次自增之前（即还没有完成第二次自增时），调用了 next()。这就使该变量陷入了一种‘不正确’的状态。
        ++currentEvenValue;
        return currentEvenValue;
    }

    public static void main(String[] args) {
        EvenChecker.test(new EvenProducer());
    }
}
/* Output 1: (Example)
23593 not even!
23601 not even!
23603 not even!
23599 not even!
23595 not even!
23607 not even!
23609 not even!
23605 not even!
23597 not even!
23611 not even!
 */
/* Output 2: (Example)
20233 not even!
 */
