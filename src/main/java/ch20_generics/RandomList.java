package ch20_generics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * @author runningpig66
 * @date 2025/12/25 周四
 * @time 17:05
 * P.649 §20.2 简单泛型 §20.2.3 RandomList
 * <p>
 * 再举一个 holder 的例子。假设你想实现一个 list 类型，每次调用 select() 都会从其中的元素里随机选取一个。
 * 如果想构建成一个可用于所有类型的工具，便可以使用泛型：
 */
public class RandomList<T> extends ArrayList<T> {
    private Random rand = new Random(47);

    public T select() {
        return get(rand.nextInt(size()));
    }

    public static void main(String[] args) {
        RandomList<String> rs = new RandomList<>();
        Arrays.stream(
                ("The quick brown fox jumped over the lazy brown dog").split(" ")
        ).forEach(rs::add);
        IntStream.range(0, 11).forEach(i ->
                System.out.print(rs.select() + " "));
    }
}
/* Output:
brown over fox quick quick dog brown The brown lazy brown
 */
