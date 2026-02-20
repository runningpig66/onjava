package onjava;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2月17日 周二
 * @time 16:55
 * P.111 §3.6 填充集合 §3.6.2 使用 Suppliers 来填充 Map
 * <p>
 * 以上示例 (ch24_collectiontopics/StreamFillMaps.java) 体现了一个模式，我们可以用它来编写一个自动创建和填充 Map 的工具：
 */
public class FillMap {
    public static <K, V> Map<K, V> basic(Supplier<Pair<K, V>> pairGen, int size) {
        return Stream.generate(pairGen).limit(size)
                .collect(Collectors.toMap(Pair::key, Pair::value));
    }

    public static <K, V> Map<K, V> basic(Supplier<K> keyGen, Supplier<V> valueGen, int size) {
        return Stream.generate(() -> Pair.make(keyGen.get(), valueGen.get())).limit(size)
                .collect(Collectors.toMap(Pair::key, Pair::value));
    }

    public static <K, V, M extends Map<K, V>> M create(Supplier<K> keyGen,
                                                       Supplier<V> valueGen,
                                                       Supplier<M> mapSupplier, int size) {
        return Stream.generate(() -> Pair.make(keyGen.get(), valueGen.get())).limit(size)
                .collect(Collectors.toMap(Pair::key, Pair::value, (v1, v2) -> v1, mapSupplier));
    }
}
