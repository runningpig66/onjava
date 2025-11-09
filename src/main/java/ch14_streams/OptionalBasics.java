package ch14_streams;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/11/3 周一
 * @time 16:40
 * 代码清单 P.397 Optional 类型：
 * 我们接收到一个 Optional 时，首先要调用 isPresent(), 看看里面是不是有东西。如果有，再使用 get() 来获取。
 */
class OptionalBasics {
    static void test(Optional<String> optString) {
        if (optString.isPresent()) {
            System.out.println(optString.get());
        } else {
            System.out.println("Nothing inside!");
        }
    }

    public static void main(String[] args) {
        test(Optional.of("Epithets"));
        test(Stream.<String>empty().findFirst());
    }
}
/* Output:
Epithets
Nothing inside!
 */
