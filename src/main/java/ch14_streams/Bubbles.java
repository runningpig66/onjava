package ch14_streams;

import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/10/23 周四
 * @time 23:20
 * 代码清单 P.384 因为 bubbler() 与 Supplier<Bubble> 接口兼容，
 * 所以我们可以将其方法引用传给 Stream.generate()
 */
public class Bubbles {
    public static void main(String[] args) {
        Stream.generate(Bubble::bubbler)
                .limit(5)
                .forEach(System.out::println);
    }
}
/* Output:
Bubble(0)
Bubble(1)
Bubble(2)
Bubble(3)
Bubble(4)
 */
