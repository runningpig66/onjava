package ch14_streams;

import java.util.Random;

/**
 * @author runningpig66
 * @date 2025/10/19 周日
 * @time 12:17
 * 代码清单 P.376 流 (stream) - 声明式编程
 */
public class Randoms {
    public static void main(String[] args) {
        new Random(47)
                .ints(5, 20)
                .distinct()
                .limit(7)
                .sorted()
                .forEach(System.out::println);
    }
}
/* Output:
6
10
13
16
17
18
1
 */
