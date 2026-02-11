package ch23_references;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author runningpig66
 * @date 2月11日 周三
 * @time 22:17
 * P.088 §2.4 不可变类
 * A trivial wrapper class
 */
class IntValue2 {
    public int n;

    IntValue2(int n) {
        this.n = n;
    }
}

public class SimplerMutableInteger {
    public static void main(String[] args) {
        List<IntValue2> v = IntStream.range(0, 10)
                .mapToObj(IntValue2::new)
                .collect(Collectors.toList());
        v.forEach(iv2 -> System.out.print(iv2.n + " "));
        System.out.println();
        v.forEach(iv2 -> iv2.n += 1);
        v.forEach(iv2 -> System.out.print(iv2.n + " "));
    }
}
/* Output:
0 1 2 3 4 5 6 7 8 9
1 2 3 4 5 6 7 8 9 10
 */
