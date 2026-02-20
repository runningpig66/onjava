package ch24_collectiontopics;

import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.function.Function;

/**
 * @author runningpig66
 * @date 2月20日 周五
 * @time 1:57
 * P.129 §3.10 Set 与存储顺序
 * Methods necessary to put your own type in a Set
 * <p>
 * 这个示例演示了要配合某个特定的 Set 实现来使用时，一个类型需要实现的方法：
 */
// Set (interface): 向 Set 中添加的每个元素都必须是唯一的，Set 不会添加重复的元素。
// 被添加到 Set 中的元素必须至少定义了 equals() 方法，用以确定对象的唯一性。
// Set 继承了 Collection，而且没有添加任何东西。Set 接口不保证以任何特定的顺序维护元素
class SetType {
    protected int i;

    SetType(int n) {
        i = n;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SetType setType &&
                Objects.equals(this.i, setType.i);
    }

    @Override
    public String toString() {
        return Integer.toString(i);
    }
}

// HashSet: 用于对快速查找时间要求较高的 Set。要添加的元素必须定义 hashCode() 和 equals() 方法
class HashType extends SetType {
    HashType(int n) {
        super(n);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(i);
    }
}

// TreeSet: 有序的 Set，底层是一个树结构。通过这种方式，我们可以从某个 Set 提取出一个有序的序列。
// 要添加的元素必须实现 Comparable 接口；compareTo() == 0 是 TreeSet 判断元素是否重复的唯一标准！
class TreeType extends SetType implements Comparable<TreeType> {
    TreeType(int n) {
        super(n);
    }

    // 我们通常希望 compareTo() 方法能产生与 equals() 方法一致的自然排序。
    // 如果对于某个特定的比较，equals() 的结果为 true，那么 compareTo() 的结果应该为零；
    // 如果 equals() 的结果为 false，那么 compareTo() 的结果应该是一个非零值。
    @Override
    public int compareTo(@NonNull TreeType arg) {
        return Integer.compare(arg.i, i);
        // Equivalent to: return arg.i < i ? -1 : (arg.i == i ? 0 : 1);
    }
}

public class TypesForSets {
    static <T> void fill(Set<T> set, Function<Integer, T> type) {
        for (int i = 10; i >= 5; i--) { // Descending
            set.add(type.apply(i));
        }
        for (int i = 0; i < 5; i++) { // Ascending
            set.add(type.apply(i));
        }
    }

    static <T> void test(Set<T> set, Function<Integer, T> type) {
        fill(set, type);
        fill(set, type); // Try to add duplicates
        fill(set, type);
        System.out.println(set);
    }

    public static void main(String[] args) {
        test(new HashSet<>(), HashType::new);
        test(new LinkedHashSet<>(), HashType::new);
        test(new TreeSet<>(), TreeType::new);
        // Things that don't work:
        test(new HashSet<>(), SetType::new);
        test(new HashSet<>(), TreeType::new);
        test(new LinkedHashSet<>(), SetType::new);
        test(new LinkedHashSet<>(), TreeType::new);
        try {
            test(new TreeSet<>(), SetType::new);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            test(new TreeSet<>(), HashType::new);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
/* Output:
[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
[10, 9, 8, 7, 6, 5, 0, 1, 2, 3, 4]
[10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0]
[1, 0, 8, 6, 7, 8, 9, 4, 10, 7, 5, 4, 6, 1, 5, 3, 9, 2, 8, 7, 10, 5, 3, 1, 2, 4, 2, 0, 3, 9, 10, 0, 6]
[7, 3, 4, 1, 4, 1, 8, 7, 6, 9, 5, 3, 0, 10, 5, 3, 10, 8, 9, 10, 2, 5, 0, 1, 6, 9, 2, 0, 7, 2, 6, 4, 8]
[10, 9, 8, 7, 6, 5, 0, 1, 2, 3, 4, 10, 9, 8, 7, 6, 5, 0, 1, 2, 3, 4, 10, 9, 8, 7, 6, 5, 0, 1, 2, 3, 4]
[10, 9, 8, 7, 6, 5, 0, 1, 2, 3, 4, 10, 9, 8, 7, 6, 5, 0, 1, 2, 3, 4, 10, 9, 8, 7, 6, 5, 0, 1, 2, 3, 4]
class ch24_collectiontopics.SetType cannot be cast to class java.lang.Comparable (ch24_collectiontopics.SetType is in unnamed module of loader 'app'; java.lang.Comparable is in module java.base of loader 'bootstrap')
class ch24_collectiontopics.HashType cannot be cast to class java.lang.Comparable (ch24_collectiontopics.HashType is in unnamed module of loader 'app'; java.lang.Comparable is in module java.base of loader 'bootstrap')
 */
