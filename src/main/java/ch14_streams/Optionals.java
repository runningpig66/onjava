package ch14_streams;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/11/4 周二
 * @time 18:16
 * 代码清单 P.397 Optional 类型：便捷函数
 * <p>
 * 如果值存在，则用这个值来调用 Consumer, 否则什么都不做
 * public void ifPresent(Consumer<? super T> action)
 * <p>
 * 如果对象存在，则返回这个对象，否则返回 other
 * public T orElse(T other)
 * <p>
 * 如果对象存在，则返回这个对象，否则返回使用 Supplier 函数创建的替代对象
 * public T orElseGet(Supplier<? extends T> supplier)
 * <p>
 * 如果对象存在，则返回这个对象，否则抛出一个使用 Supplier 函数创建的异常
 * public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X
 */
public class Optionals {
    static void basics(Optional<String> optString) {
        if (optString.isPresent()) {
            System.out.println(optString.get());
        } else {
            System.out.println("Nothing inside!");
        }
    }

    static void ifPresent(Optional<String> optString) {
        optString.ifPresent(System.out::println);
    }

    static void orElse(Optional<String> optString) {
        System.out.println(optString.orElse("Nada"));
    }

    static void orElseGet(Optional<String> optString) {
        System.out.println(optString.orElseGet(() -> "Generated"));
    }

    static void orElseThrow(Optional<String> optString) {
        try {
            System.out.println(optString.orElseThrow(() -> new Exception("Supplied")));
        } catch (Exception e) {
            System.out.println("Caught " + e);
        }
    }

    static void test(String testName, Consumer<Optional<String>> cos) {
        System.out.println(" === " + testName + " === ");
        cos.accept(Stream.of("Epithets").findFirst());
        cos.accept(Stream.<String>empty().findFirst());
    }

    public static void main(String[] args) {
        test("basics", Optionals::basics);
        test("ifPresent", Optionals::ifPresent);
        test("orElse", Optionals::orElse);
        test("orElseGet", Optionals::orElseGet);
        test("orElseThrow", Optionals::orElseThrow);
    }
}
/* Output:
 === basics ===
Epithets
Nothing inside!
 === ifPresent ===
Epithets
 === orElse ===
Epithets
Nada
 === orElseGet ===
Epithets
Generated
 === orElseThrow ===
Epithets
Caught java.lang.Exception: Supplied
 */
