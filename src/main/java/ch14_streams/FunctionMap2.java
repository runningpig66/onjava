package ch14_streams;

import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/11/1 周六
 * @time 22:34
 * 代码清单 P.392 将函数应用于每个流元素 2：
 * <R> Stream<R> map(Function<? super T, ? extends R> mapper);
 */
class Numbered {
    final int n;

    Numbered(int n) {
        this.n = n;
    }

    @Override
    public String toString() {
        return "Numbered(" + n + ")";
    }
}

public class FunctionMap2 {
    public static void main(String[] args) {
        Stream.of(1, 5, 7, 9, 11, 13)
                .map(Numbered::new)
                .forEach(System.out::println);
    }
}
/* Output:
Numbered(1)
Numbered(5)
Numbered(7)
Numbered(9)
Numbered(11)
Numbered(13)
 */
