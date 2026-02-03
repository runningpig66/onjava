package onjava;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2026-01-27 周二
 * @time 19:37
 * P.743 §20.17 Java 8 中的辅助潜在类型机制 §使用 Supplier 的泛型方法
 * A utility to use with Suppliers
 * <p>
 * 有了辅助潜在类型机制，就可以定义在本章其他地方已用到过的 Suppliers 类了。
 * 该类包含了通过生成器填充集合的实用工具方法。我们有必要将这类操作“通用化”：
 * <p>
 * notes: 16-泛型推断机制解析：上下文敏感性与钻石操作符行为.md
 * notes: 17-泛型推断分析：构造器引用与类型约束.md
 */
public class Suppliers {
    // create() 可以为你生成新的集合子类，与此同时，第一个版本的 fill() 会向已有的集合子类放入元素。
    // 注意同时还返回了传入的容器的具体类型，因此类型信息并未丢失。
    // Create a collection and fill it:
    public static <T, C extends Collection<T>> C create(
            Supplier<C> factory, Supplier<T> gen, int n
    ) {
        return Stream.generate(gen)
                .limit(n)
                .collect(factory, C::add, C::addAll);
    }

    // Fill an existing collection:
    public static <T, C extends Collection<T>> C fill(
            C coll, Supplier<T> gen, int n
    ) {
        Stream.generate(gen)
                .limit(n)
                .forEach(coll::add);
        return coll;
    }

    // 前两个方法一般仅限用于集合的子类。第二个版本的 fill() 可用于任意类型的 holder。它有个额外的参数：未绑定的方法引用 adder。
    // 通过辅助潜在类型机制，fill() 可用于任意类型的 holder，只要该 holder 内含有用于添加元素的方法。
    // 由于该未绑定方法 adder 必须接收一个参数（要添加到 holder 中的元素），adder 必须是一个 BiConsumer<H, A>。
    // 其中 H 是要绑定的目标 holder 对象的类型，而 A 则是要添加的元素类型。对 accept() 的调用会以 a 为参数，在对象 holder 上调用未绑定方法 adder。
    // Use an unbound method reference to produce a more general method:
    public static <H, A> H fill(
            H holder, BiConsumer<H, A> adder, Supplier<A> gen, int n
    ) {
        Stream.generate(gen)
                .limit(n)
                .forEach(a -> adder.accept(holder, a));
        return holder;
    }
}
