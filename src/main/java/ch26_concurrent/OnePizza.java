package ch26_concurrent;

import onjava.Timer;

/**
 * @author runningpig66
 * @date 3月7日 周六
 * @time 20:42
 * P.269 §5.13 工作量、复杂性、成本
 * <p>
 * 如果是一个人在做一个比萨，那么所有的步骤都是线性的，一个接着一个：
 * 时间单位是毫秒，并且将所有步骤的工作量相加起来，结果和我们期望的一致。
 */
public class OnePizza {
    public static void main(String[] args) {
        Pizza za = new Pizza(0);
        System.out.println(
                Timer.duration(() -> {
                    while (!za.complete()) {
                        za.next();
                    }
                })
        );
    }
}
/* Output:
currentThread: [main], Pizza 0: ROLLED
currentThread: [main], Pizza 0: SAUCED
currentThread: [main], Pizza 0: CHEESED
currentThread: [main], Pizza 0: TOPPED
currentThread: [main], Pizza 0: BAKED
currentThread: [main], Pizza 0: SLICED
currentThread: [main], Pizza 0: BOXED
1669
 */
