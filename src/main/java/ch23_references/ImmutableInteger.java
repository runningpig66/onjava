package ch23_references;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author runningpig66
 * @date 2月11日 周三
 * @time 21:40
 * P.087 §2.4 不可变类
 * The Integer class cannot be changed
 */
public class ImmutableInteger {
    @SuppressWarnings("removal")
    public static void main(String[] args) {
        List<Integer> v = IntStream.range(0, 10)
                .mapToObj(Integer::new)
                .collect(Collectors.toList());
        System.out.println(v);
        // But how do you change the int inside the Integer?
    }
}
/* Output:
[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
 */
