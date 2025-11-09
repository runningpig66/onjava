package ch14_streams;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/11/10 周一
 * @time 1:51
 * 代码清单 P.408 终结操作：收集操作
 * <p>
 * 可以从某个流生成一个 Map:
 * <p>
 * public static <T, K, U> Collector<T, ?, Map<K,U>> toMap(
 * Function<? super T, ? extends K> keyMapper,
 * Function<? super T, ? extends U> valueMapper)
 */
class Pair {
    public final Character c;
    public final Integer i;

    Pair(Character c, Integer i) {
        this.c = c;
        this.i = i;
    }

    public Character getC() {
        return c;
    }

    public Integer getI() {
        return i;
    }

    @Override
    public String toString() {
        return "Pair(" + c + ", " + i + ")";
    }
}

class RandomPair {
    Random rand = new Random(47);
    // An infinite iterator of random capital letters:
    Iterator<Character> capChars = rand.ints(65, 91) // 即大写字母对应的 ASCII 值
            .mapToObj(i -> (char) i)
            .iterator();

    public Stream<Pair> stream() {
        return rand.ints(100, 1000)
                .distinct()
                .mapToObj(i -> new Pair(capChars.next(), i));
    }
}

public class MapCollector {
    public static void main(String[] args) {
        Map<Integer, Character> map =
                new RandomPair().stream()
                        .limit(8)
                        .collect(Collectors.toMap(Pair::getI, Pair::getC));
        System.out.println(map);
    }
}
/* Output:
{688=W, 293=B, 309=C, 761=N, 858=N, 668=G, 622=F, 751=N}
 */
