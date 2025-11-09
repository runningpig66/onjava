package ch14_streams;

import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/11/1 周六
 * @time 23:22
 * 代码清单 P.393 在应用 map() 期间组合流：
 * 接上例；我们天真地希望得到一个 String 流，但得到的是一个由指向其他流的“头”组成的流。
 * 可以用 flatMap() 轻松地解决这个问题：
 * <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper);
 */
public class FlatMap {
    public static void main(String[] args) {
        Stream.of(1, 2, 3)
                .flatMap(i -> Stream.of("Gonzo", "Fozzie", "Beaker"))
                .forEach(System.out::println);
    }
}
/* Output:
Gonzo
Fozzie
Beaker
Gonzo
Fozzie
Beaker
Gonzo
Fozzie
Beaker
 */
