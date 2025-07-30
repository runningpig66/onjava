package ch12_collections;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * @author runningpig66
 * @date 2025-07-30
 * @time 下午 14:16
 * 任何实现 Iterable 接口的类都可以配合 for-in 使用
 */
public class IterableClass implements Iterable<String> {
    protected String[] words = ("And that is how we know the Earth to be banana-shaped.").split(" ");

    @NotNull
    @Override
    public Iterator<String> iterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < words.length;
            }

            @Override
            public String next() {
                return words[index++];
            }
        };
    }

    public static void main(String[] args) {
        for (String s : new IterableClass()) {
            System.out.print(s + " ");
        }
    }
}
/* Output:
And that is how we know the Earth to be banana-shaped.
 */
