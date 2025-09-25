package ch13_functional;

/**
 * @author runningpig66
 * @date 2025/9/26 周五
 * @time 5:54
 * 代码清单 P.361
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R> {
    R apply(T t, U u, V v);
}
