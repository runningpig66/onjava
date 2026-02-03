package ch20_generics;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author runningpig66
 * @date 2026-01-23
 * @time 21:28
 * P.738 §20.16 对于缺少（直接的）潜在类型机制的补偿 §20.16.2 将方法应用于序列
 * <p>
 * 反射给了人很大的想象空间，但是它将所有的类型检查都降级到了运行时。因此在很多情况下它不是我们想要的。
 * 如果能够实现编译时类型检查，通常来说会更能满足我们的需求。但是可以同时拥有编译时类型检查以及潜在类型机制吗？
 * <p>
 * 我们来看一个关于这个问题的一个例子。假设你要创建一个 apply() 方法，用于按一定顺序执行每个对象上的任意方法。
 * 这种情况下用接口似乎无法满足需求。你想要将任意方法应用在集合中的对象上，而接口对于表述“任意方法”有着太多限制。在 Java 中怎样才能实现呢？
 * <p>
 * 一开始，我们可以用反射解决这个问题。由于使用了可变参数，这个方案看起来相当优雅：
 * <p>
 * 为什么不能使用 Java 8 的方法引用（稍后会演示）来代替反射 Method f 呢？
 * 注意那个 invoke() 有着可以接收任何数量的参数的优点，因此 apply() 也同样具有此优点。在某些情况下，这样的灵活性可能非常重要。
 */
public class Apply {
    public static <T, S extends Iterable<T>> void apply(S seq, Method f, Object... args) {
        try {
            for (T t : seq) {
                f.invoke(t, args);
            }
        } catch (IllegalAccessException |
                 IllegalArgumentException |
                 InvocationTargetException e) {
            // Failures are programmer errors
            throw new RuntimeException(e);
        }
    }
}
