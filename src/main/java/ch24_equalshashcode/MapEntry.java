package ch24_equalshashcode;

import java.util.Map;
import java.util.Objects;

/**
 * @author runningpig66
 * @date 2月12日 周四
 * @time 21:27
 * P.465 §C.2 哈希和哈希码 §C.2.1 理解 hashCode()
 * A simple Map.Entry for sample Map implementations
 */
public class MapEntry<K, V> implements Map.Entry<K, V> {
    private K key;
    private V value;

    public MapEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V result = this.value;
        this.value = value;
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    /*@SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object rval) {
        return rval instanceof MapEntry &&
                Objects.equals(key, ((MapEntry<K, V>) rval).getKey()) &&
                Objects.equals(value, ((MapEntry<K, V>) rval).getValue());
    }*/

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MapEntry<?, ?> mapEntry &&
                Objects.equals(key, mapEntry.key) && // 在类定义内部，允许访问该类任何实例的私有成员
                Objects.equals(value, mapEntry.value);
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }
}
