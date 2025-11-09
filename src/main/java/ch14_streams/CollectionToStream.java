package ch14_streams;

import java.util.*;

/**
 * @author runningpig66
 * @date 2025/10/19 周日
 * @time 15:44
 * 代码清单 P.378 流的创建：每个 Collection 都可以使用 stream() 方法来生成一个流
 */
public class CollectionToStream {
    public static void main(String[] args) {
        List<Bubble> bubbles = Arrays.asList(new Bubble(1), new Bubble(2), new Bubble(3));
        System.out.println(bubbles.stream()
                .mapToInt(b -> b.i)
                .sum());

        Set<String> w = new HashSet<>(
                Arrays.asList("It's a wonderful day for pie!".split(" ")));
        w.stream()
                .map(x -> x + " ")
                .forEach(System.out::print);
        System.out.println();

        Map<String, Double> m = new HashMap<>();
        m.put("pi", 3.14159);
        m.put("e", 2.718);
        m.put("phi", 1.618);
        m.entrySet().stream()
                .map(e -> e.getKey() + " " + e.getValue())
                .forEach(System.out::println);
    }
}
/* Output:
6
a pie! It's for wonderful day
phi 1.618
e 2.718
pi 3.14159
 */
