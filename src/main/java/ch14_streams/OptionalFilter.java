package ch14_streams;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/11/5 周三
 * @time 0:51
 * 代码清单 P.400 Optional 类型：Optional 对象上的操作
 * <p>
 * 将 Predicate 应用于 Optional 的内容，并返回其结果。
 * 如果 Optional 与 Predicate 不匹配，则将其转换为 empty（不会删除元素）。
 * 如果 Optional 本身已经是 empty, 则直接传回。
 * public Optional<T> filter(Predicate<? super T> predicate)
 */
public class OptionalFilter {
    static String[] elements = {"Foo", "", "Bar", "Baz", "Bingo"};

    static Stream<String> testStream() {
        return Arrays.stream(elements);
    }

    static void test(String descr, Predicate<String> pred) {
        System.out.println(" ---( " + descr + " )--- ");
        for (int i = 0; i <= elements.length; i++) {
            // 每次进人 for 循环，都会重新获得一个流，并跳过用 for 循环的索引设置的元素数，这就使其看上去像流中的连续元素。
            System.out.println(
                    testStream()
                            .skip(i)
                            .findFirst()
                            .filter(pred));
        }
    }

    public static void main(String[] args) {
        test("true", str -> true);
        test("false", str -> false);
        test("str != \"\"", str -> str != "");
        test("str.length() == 3", str -> str.length() == 3);
        test("startsWith(\"B\")", str -> str.startsWith("B"));
    }
}
/* Output:
 ---( true )---
Optional[Foo]
Optional[]
Optional[Bar]
Optional[Baz]
Optional[Bingo]
Optional.empty
 ---( false )---
Optional.empty
Optional.empty
Optional.empty
Optional.empty
Optional.empty
Optional.empty
 ---( str != "" )---
Optional[Foo]
Optional.empty
Optional[Bar]
Optional[Baz]
Optional[Bingo]
Optional.empty
 ---( str.length() == 3 )---
Optional[Foo]
Optional.empty
Optional[Bar]
Optional[Baz]
Optional.empty
Optional.empty
 ---( startsWith("B") )---
Optional.empty
Optional.empty
Optional[Bar]
Optional[Baz]
Optional[Bingo]
Optional.empty
 */
