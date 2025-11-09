package ch14_streams;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/11/5 周三
 * @time 2:40
 * 代码清单 P.403 Optional 类型：Optional 对象上的操作
 * <p>
 * 和 map() 类似，flatMap() 会获得非 empty 的 Optional 中的对象，并将其交给映射函数。
 * 但是所提供的映射函数会将结果包在 Optional 中，这样 flatMap() 最后就不会再做任何包装了。
 * public <U> Optional<U> flatMap(Function<? super T, ? extends Optional<? extends U>> mapper)
 */
public class OptionalFlatMap {
    static String[] elements = {"12", "", "23", "45"};

    static Stream<String> testStream() {
        return Arrays.stream(elements);
    }

    static void test(String descr, Function<String, Optional<String>> func) {
        System.out.println(" ---( " + descr + " )--- ");
        for (int i = 0; i <= elements.length; i++) {
            System.out.println(
                    testStream()
                            .skip(i)
                            .findFirst()
                            .flatMap(func));
        }
    }

    public static void main(String[] args) {
        test("Add brackets", s -> Optional.of("[" + s + "]"));
        test("Increment", s -> {
            try {
                return Optional.of(Integer.parseInt(s) + 1 + "");
            } catch (NumberFormatException e) {
                return Optional.of(s);
            }
        });
        test("Replace", s -> Optional.of(s.replace("2", "9")));
        test("Take last digit", s -> Optional.of(
                s.length() > 0 ? s.charAt(s.length() - 1) + "" : s));
    }
}
/* Output:
 ---( Add brackets )---
Optional[[12]]
Optional[[]]
Optional[[23]]
Optional[[45]]
Optional.empty
 ---( Increment )---
Optional[13]
Optional[]
Optional[24]
Optional[46]
Optional.empty
 ---( Replace )---
Optional[19]
Optional[]
Optional[93]
Optional[45]
Optional.empty
 ---( Take last digit )---
Optional[2]
Optional[]
Optional[3]
Optional[5]
Optional.empty
 */
