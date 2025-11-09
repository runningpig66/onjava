package ch14_streams;

import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/10/23 周四
 * @time 23:04
 * 代码清单 P.383 RandomWords.java 用到了 Supplier<T> 和 Stream.generate()。下面是第二个示例
 */
public class Generator implements Supplier<String> {
    Random rand = new Random(47);
    char[] letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    @Override
    public String get() {
        return "" + letters[rand.nextInt(letters.length)];
    }

    public static void main(String[] args) {
        String word = Stream.generate(new Generator())
                .limit(30)
                .collect(Collectors.joining());
        System.out.println(word);
    }
}
/* Output:
YNZBRNYGCFOWZNTCQRGSEGZMMJMROE
 */
