package ch12_collections;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author runningpig66
 * @date 2025-07-30
 * @time 下午 15:33
 * "适配器方法惯用法". 提供了更多 Iterable 对象用于 for-in 语句
 */
class ReversibleArrayList<T> extends ArrayList<T> {
    ReversibleArrayList(Collection<T> c) {
        super(c);
    }

    public Iterable<T> reversed1() {
        return new Iterable<T>() {
            @NotNull
            @Override
            public Iterator<T> iterator() {
                return new Iterator<>() {
                    int current = size() - 1;

                    @Override
                    public boolean hasNext() {
                        return current > -1;
                    }

                    @Override
                    public T next() {
                        return get(current--);
                    }
                };
            }
        };
    }
}

public class AdapterMethodIdiom {
    public static void main(String[] args) {
        ReversibleArrayList<String> ral = new ReversibleArrayList<>(
                Arrays.asList("To be or not to be".split(" ")));
        // 通过 iterator() 获得原始的迭代器
        for (String s : ral) {
            System.out.print(s + " ");
        }
        System.out.println();
        // 使用我们选择的迭代方式
        for (String s : ral.reversed1()) {
            System.out.print(s + " ");
        }
    }
}
/* Output:
To be or not to be
be to not or be To
 */
