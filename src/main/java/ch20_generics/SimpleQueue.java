package ch20_generics;

import org.jspecify.annotations.NonNull;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author runningpig66
 * @date 2026-01-27 周二
 * @time 14:55
 * P.740 §20.16 对于缺少（直接的）潜在类型机制的补偿 §20.16.2 将方法应用于序列
 * A different kind of Iterable collection
 */
public class SimpleQueue<T> implements Iterable<T> {
    private final LinkedList<T> storage = new LinkedList<>();

    public void add(T t) {
        storage.offer(t);
    }

    public T get() {
        return storage.poll();
    }

    @Override
    public @NonNull Iterator<T> iterator() {
        return storage.iterator();
    }
}
