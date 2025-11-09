package ch14_streams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author runningpig66
 * @date 2025/11/6 周四
 * @time 1:32
 * 代码清单 P.407 终结操作：收集操作
 * <p>
 * 假设想把我们的条目最终放到一个 TreeSet 中，由此使它们总是有序的。
 * 在 Collectors 中没有特定的 toTreeSet() 方法，但是可以使用 Collectors.toCollection(),
 * 并将任何类型的 Collection 的构造器引用传给它。下面的程序提取文件中的单词放到 TreeSet 中：
 * <p>
 * 使用这个 Collector 将流元素累加到一个结果集合中。
 * <R, A> R collect(Collector<? super T, A, R> collector);
 * <p>
 * public static <T, C extends Collection<T>>
 * Collector<T, ?, C> toCollection(Supplier<C> collectionFactory)
 */
public class TreeSetOfWords {
    public static void main(String[] args) throws IOException {
        TreeSet<String> words2 = Files.lines(Paths.get("src/main/java/ch14_streams/TreeSetOfWords.java"))
                .flatMap(s -> Arrays.stream(s.split("\\W+")))
                .filter(s -> !s.matches("\\d+")) // No numbers
                .map(String::trim)
                .filter(s -> s.length() > 2)
                .limit(100)
                .collect(Collectors.toCollection(TreeSet::new));
        System.out.println(words2);
    }
}
/* Output:
[Arrays, Collectors, Files, IOException, Output, Paths, String, System, TreeSet, TreeSetOfWords, args, author, ch14_streams, class, collect, date, file, filter, flatMap, get, import, java, length, limit, lines, main, map, matches, new, nio, numbers, out, package, println, public, runningpig66, split, src, static, stream, throws, time, toCollection, trim, util, void, words2]
 */
