package ch26_concurrent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author runningpig66
 * @date 3月7日 周六
 * @time 0:26
 * P.266 §5.12 构造器并不是线程安全的
 * <p>
 * （TestStaticIDField.java 中）重复 id 的数量相当多。显然，单纯的 static int 对于构造过程来说并不安全。
 * 下面我们通过 AtomicInteger 来让该过程变为线程安全的：
 * <p>
 * notes: race-condition-and-thread-confinement.md
 */
public class GuardedIDField implements HasID {
    private static AtomicInteger counter = new AtomicInteger();
    private int id = counter.getAndIncrement();

    @Override
    public int getID() {
        return id;
    }

    public static void main(String[] args) {
        IDChecker.test(GuardedIDField::new);
    }
}
/* Output:
0
 */
