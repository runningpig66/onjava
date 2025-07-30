package ch12_collections;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author runningpig66
 * @date 2025-07-30 周三
 * @time 下午 15:54
 * 添加几个适配器方法
 */
public class MultiIterableClass extends IterableClass {
    public Iterable<String> reversed() {
        return new Iterable<>() {
            @NotNull
            @Override
            public Iterator<String> iterator() {
                return new Iterator<>() {
                    int current = words.length - 1;

                    @Override
                    public boolean hasNext() {
                        return current > -1;
                    }

                    @Override
                    public String next() {
                        return words[current--];
                    }
                };
            }
        };
    }

    public Iterable<String> randomized() {
        return new Iterable<>() {
            @NotNull
            @Override
            public Iterator<String> iterator() {
                List<String> shuffled = new ArrayList<>(Arrays.asList(words));
                Collections.shuffle(shuffled, new Random(47));
                return shuffled.iterator();
            }
        };
    }

    public static void main(String[] args) {
        MultiIterableClass mic = new MultiIterableClass();
        for (String s : mic.reversed()) {
            System.out.print(s + " ");
        }
        System.out.println();
        for (String s : mic.randomized()) {
            System.out.print(s + " ");
        }
        System.out.println();
        for (String s : mic) {
            System.out.print(s + " ");
        }
    }
}
/* Output:
banana-shaped. be to Earth the know we how is that And
is banana-shaped. Earth that how the be And we know to
And that is how we know the Earth to be banana-shaped.
 */
