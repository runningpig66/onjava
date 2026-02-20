package ch24_collectiontopics;

import java.util.Arrays;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author runningpig66
 * @date 2月20日 周五
 * @time 4:37
 * P.133 §3.10 Set 与存储顺序 §SortedSet
 */
public class SortedSetDemo {
    public static void main(String[] args) {
        SortedSet<String> sortedSet = Arrays.stream("one two three four five six seven eight".split(" "))
                .collect(Collectors.toCollection(TreeSet::new));
        System.out.println(sortedSet);
        String low = sortedSet.first(); // E first(): 生成最小的元素。
        String high = sortedSet.last(); // E last(): 生成最大的元素。
        System.out.println(low);
        System.out.println(high);
        Iterator<String> it = sortedSet.iterator();
        for (int i = 0; i <= 6; i++) {
            if (i == 3) low = it.next(); // i == 3 的这一轮循环中，迭代器实际上消耗了两个元素。
            if (i == 6) high = it.next();
            else it.next(); // i == 3
        }
        System.out.println(low);
        System.out.println(high);
        // SortedSet<E> subSet(E fromElement, E toElement):
        // 生成该 Set 的一个视图，包含从 fromElement（包含）到 toElement（不包含）的元素。
        System.out.println(sortedSet.subSet(low, high));
        // SortedSet<E> headSet(E toElement): 生成该 Set 的一个视图，包含小于 toElement 的所有元素。
        System.out.println(sortedSet.headSet(high));
        // SortedSet<E> tailSet(E fromElement): 生成该 Set 的一个视图，包含大于或等于 fromElement 的所有元素。
        System.out.println(sortedSet.tailSet(low));
    }
}
/* Output:
[eight, five, four, one, seven, six, three, two]
eight
two
one
two
[one, seven, six, three]
[eight, five, four, one, seven, six, three]
[one, seven, six, three, two]
 */
