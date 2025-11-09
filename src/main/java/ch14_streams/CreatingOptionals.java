package ch14_streams;

import java.util.Optional;

/**
 * @author runningpig66
 * @date 2025/11/5 周三
 * @time 0:33
 * 代码清单 P.399 Optional 类型：创建 Optional
 * <p>
 * 返回一个空的 Optional
 * public static<T> Optional<T> empty()
 * <p>
 * 如果已经知道这个 value 不是 null, 可以使用该方法将其包在一个 Optional 中
 * public static <T> Optional<T> of(T value)
 * <p>
 * 如果不知道这个 value 是不是 null, 使用这个方法。
 * 如果 value 为 null, 它会自动返回 Optional.empty, 否则会将这个 value 包在一个 Optional 中
 * public static <T> Optional<T> ofNullable(T value)
 */
public class CreatingOptionals {
    static void test(String testName, Optional<String> opt) {
        System.out.println(" === " + testName + " === ");
        System.out.println(opt.orElse("Null"));
    }

    public static void main(String[] args) {
        test("empty", Optional.empty());
        test("of", Optional.of("Howdy"));
        try {
            test("of", Optional.of(null));
        } catch (Exception e) {
            System.out.println(e);
        }
        test("ofNullable", Optional.ofNullable("Hi"));
        test("ofNullable", Optional.ofNullable(null));
    }
}
/* Output
 === empty ===
Null
 === of ===
Howdy
java.lang.NullPointerException
 === ofNullable ===
Hi
 === ofNullable ===
Null
 */
