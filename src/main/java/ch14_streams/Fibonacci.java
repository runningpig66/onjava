package ch14_streams;

import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/10/23 周四
 * @time 23:28
 * 代码清单 P.385 Stream.iterate(seed, f) 从一个种子开始（第一个参数）生成第一个流元素，
 * 然后将其传给第二个参数所引用的方法 f，其结果被添加到这个流上，并且保存下来作为下一次 f 调用的第一个参数。
 * 产生的序列是 seed, f(seed), f(f(seed)), …，以此类推，每一步的结果会作为下一步 f 的输入。
 * 我们可以通过迭代生成一个斐波那契数列
 */
public class Fibonacci {
    int x = 1;

    Stream<Integer> numbers() {
        return Stream.iterate(0, i -> {
            int result = x + i;
            x = i;
            return result;
        });
    }

    public static void main(String[] args) {
        new Fibonacci().numbers()
                .skip(20) // Don't use the first 20
                .limit(10) // Then take 10 of them
                .forEach(System.out::println);
    }
}
/* Output:
6765
10946
17711
28657
46368
75025
121393
196418
317811
514229
 */