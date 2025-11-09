package ch14_streams;

import java.util.stream.LongStream;

/**
 * @author runningpig66
 * @date 2025/10/28 周二
 * @time 23:11
 * 代码清单 P.390 移除元素
 * LongStream filter(LongPredicate predicate)
 * LongStream rangeClosed(long startInclusive, final long endInclusive)
 * boolean noneMatch(LongPredicate predicate)
 */
public class Prime {
    public static boolean isPrime(long n) {
        return LongStream.rangeClosed(2, (long) Math.sqrt(n)) // rangeClosed() 包含了上界值
                .noneMatch(i -> n % i == 0); // 如果没有任何一个取余操作的结果为 0, 则 noneMatch() 操作返回 true
    }

    public LongStream numbers() {
        return LongStream.iterate(2, i -> i + 1)
                .filter(Prime::isPrime);
    }

    public static void main(String[] args) {
        new Prime().numbers()
                .limit(10)
                .forEach(n -> System.out.format("%d ", n));
        System.out.println();
        new Prime().numbers()
                .skip(90)
                .limit(10)
                .forEach(n -> System.out.format("%d ", n));
    }
}
/* Output:
2 3 5 7 11 13 17 19 23 29
467 479 487 491 499 503 509 521 523 541
 */
