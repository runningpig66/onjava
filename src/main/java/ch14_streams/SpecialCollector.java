package ch14_streams;

import java.util.ArrayList;

/**
 * @author runningpig66
 * @date 2025/11/10 周一
 * @time 2:28
 * 代码清单 P.409 终结操作：收集操作
 * <p>
 * 大多数情况下，如果看一下 java.util.stream.Collectors, 就能找到一个满足我们要求的预定义 Collector。
 * 找不到的情况只是极少数，这时候可以使用 collect() 的第二种形式。下面的示例演示了第二种形式的基本情况:
 * <p>
 * <R> R collect(Supplier<R> supplier,
 * BiConsumer<R, ? super T> accumulator,
 * BiConsumer<R, R> combiner);
 */
public class SpecialCollector {
    public static void main(String[] args) throws Exception {
        ArrayList<String> words = FileToWords.stream("src/main/java/ch14_streams/Cheese.dat")
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        words.stream()
                .filter(s -> s.equals("cheese"))
                .forEach(System.out::println);
    }
}
/* Output:
cheese
cheese
 */
