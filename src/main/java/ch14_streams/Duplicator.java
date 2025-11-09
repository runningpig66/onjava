package ch14_streams;

import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/10/23 周四
 * @time 23:16
 * 代码清单 P.384 如果想创建一个由完全相同的对象组成的流，
 * 只需要将一个生成这些对象的 lambda 表达式传给 generate()
 */
public class Duplicator {
    public static void main(String[] args) {
        Stream.generate(() -> "duplicate")
                .limit(3)
                .forEach(System.out::println);
    }
}
/* Output:
duplicate
duplicate
duplicate
 */
