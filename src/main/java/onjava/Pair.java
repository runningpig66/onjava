package onjava;

/**
 * @author runningpig66
 * @date 2月17日 周二
 * @time 2:52
 * P.109 §3.6 填充集合 §3.6.2 使用 Suppliers 来填充 Map
 */
public class Pair<K, V> {
    public final K key;
    public final V value;

    public Pair(K k, V v) {
        key = k;
        value = v;
    }

    public K key() {
        return key;
    }

    public V value() {
        return value;
    }

    public static <K, V> Pair<K, V> make(K k, V v) {
        return new Pair<K, V>(k, v);
    }
}
