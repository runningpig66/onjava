package ch14_streams;

import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/11/10 周一
 * @time 4:52
 * 代码清单 P.413 终结操作：选择一个元素
 * <p>
 * 如果必须选择某个流的最后一个元素，请使用 reduce():
 */
public class LastElement {
    public static void main(String[] args) {
        OptionalInt last = IntStream.range(10, 20)
                .reduce((n1, n2) -> n2);
        System.out.println(last.orElse(-1));
        // Non-numeric object:
        Optional<String> lastobj = Stream.of("one", "two", "three")
                .reduce((n1, n2) -> n2);
        System.out.println(lastobj.orElse("Nothing there!"));
    }
}
/* Output:
19
three
 */
