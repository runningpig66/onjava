package ch20_generics;

import org.jspecify.annotations.NonNull;

import java.util.Iterator;

/**
 * @author runningpig66
 * @date 2025/12/25 周四
 * @time 18:31
 * P.653 §20.3 泛型接口
 * Adapt the Fibonacci class to make it Iterable
 * <p>
 * 我们可以更进一步，实现一个 Iterable（可迭代）的斐波那契数列。
 * 一种选择是直接实现这个类，并增加 Iterable 接口，但是你不一定总能拥有原始代码的控制权，所以除非必要，否则不要重写。
 * 相反，我们可以创建一个适配器（Adapter）来生成所需的接口————这种设计模式在本书前面的章节有过介绍。
 * <p>
 * 适配器有多种实现方式。比如，可以通过继承生成适配器类：
 */
public class IterableFibonacci extends Fibonacci implements Iterable<Integer> {
    private int n;

    // 为了可以在 for-in 语句中使用 IterableFibonacci，你需要为构造器设置一个边界，这样 hasNext() 才能知道何时该返回 false。
    public IterableFibonacci(int count) {
        n = count;
    }

    @Override
    public @NonNull Iterator<Integer> iterator() {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return n > 0;
            }

            @Override
            public Integer next() {
                n--;
                return IterableFibonacci.this.get();
            }

            @Override
            public void remove() { // Not implemented
                throw new UnsupportedOperationException();
            }
        };
    }

    public static void main(String[] args) {
        for (int i : new IterableFibonacci(18)) {
            System.out.print(i + " ");
        }
    }
}
/* Output:
1 1 2 3 5 8 13 21 34 55 89 144 233 377 610 987 1597 2584
 */
