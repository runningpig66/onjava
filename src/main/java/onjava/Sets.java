package onjava;

import java.util.HashSet;
import java.util.Set;

/**
 * @author runningpig66
 * @date 2025/12/26 周五
 * @time 0:07
 * P.658 §20.4 泛型方法 §20.4.4 Set 实用工具
 * <p>
 * 再举一个泛型方法的例子，想想由 Set 所表示的那些数学关系。这些数学关系可以很方便地定义成可用于所有不同类型的泛型方法:
 * <p>
 * 前三个方法将第一个参数的引用复制到了一个新的 HashSet 对象中，从而复制了该参数，
 * 这样作为参数的 Set 就不会被直接修改了。因此返回值是一个新的 Set 对象。
 */
public class Sets {
    // union() 返回一个由两个参数合并而成的 Set
    public static <T> Set<T> union(Set<T> a, Set<T> b) {
        Set<T> result = new HashSet<>(a);
        result.addAll(b);
        return result;
    }

    // intersection() 返回由两个参数的元素交集组成的 Set
    public static <T> Set<T> intersection(Set<T> a, Set<T> b) {
        Set<T> result = new HashSet<>(a);
        result.retainAll(b);
        return result;
    }

    // Subtract subset from superset:
    // difference() 从 superset 中移除 subset 所包含的全部元素
    public static <T> Set<T> difference(Set<T> superset, Set<T> subset) {
        Set<T> result = new HashSet<>(superset);
        result.removeAll(subset);
        return result;
    }

    // Reflexive--everything not in the intersection:
    // complement() 则返回由两个参数的交集之外的所有元素组成的 Set
    public static <T> Set<T> complement(Set<T> a, Set<T> b) {
        return difference(union(a, b), intersection(a, b));
    }
}
