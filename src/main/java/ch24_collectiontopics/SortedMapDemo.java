package ch24_collectiontopics;

import onjava.CountMap;

import java.util.Iterator;
import java.util.TreeMap;

/**
 * @author runningpig66
 * @date 2月21日 周六
 * @time 0:26
 * P.142 §3.12 理解 Map §3.12.2 SortedMap
 * What you can do with a TreeMap
 * <p>
 * 利用 SortedMap（TreeMap 或 ConcurrentSkipListMap 实现了该接口），可以确保键是有序的，从而可以使用 SortedMap 接口中的方法提供的功能。
 * 下面是一个与 SortedSetDemo.java 类似的示例，演示了 TreeMap 的其他行为：这里，键值对是按照键的顺序有序存储的。
 * 因为 TreeMap 是有序的，“定位”的概念也就有意义了，所以我们可以获得第一个和最后一个元素以及子映射。
 */
public class SortedMapDemo {
    public static void main(String[] args) {
        TreeMap<Integer, String> sortedMap = new TreeMap<>(new CountMap(10));
        System.out.println(sortedMap);
        Integer low = sortedMap.firstKey(); // K firstKey(): 生成最小的键。
        Integer high = sortedMap.lastKey(); // K lastKey(): 生成最大的键。
        System.out.println(low);
        System.out.println(high);
        Iterator<Integer> it = sortedMap.keySet().iterator();
        for (int i = 0; i <= 6; i++) {
            if (i == 3) low = it.next(); // i == 3 的这一轮循环中，迭代器实际上消耗了两个元素。
            if (i == 6) high = it.next();
            else it.next(); // i == 3
        }
        System.out.println(low);
        System.out.println(high);
        // SortedMap<K,V> subMap(K fromKey, K toKey):
        // 生成该 Map 的一个视图，包含从 fromKey（包含）到 toKey（不包含）的键。
        System.out.println(sortedMap.subMap(low, high));
        // SortedMap<K,V> headMap(K toKey): 生成该 Map 的一个视图，包含小于 toKey 的所有键。
        System.out.println(sortedMap.headMap(high));
        // SortedMap<K,V> tailMap(K fromKey): 生成该 Map 的一个视图，包含大于或等于 fromKey 的所有键。
        System.out.println(sortedMap.tailMap(low));
    }
}
/* Output:
{0=A0, 1=B0, 2=C0, 3=D0, 4=E0, 5=F0, 6=G0, 7=H0, 8=I0, 9=J0}
0
9
3
7
{3=D0, 4=E0, 5=F0, 6=G0}
{0=A0, 1=B0, 2=C0, 3=D0, 4=E0, 5=F0, 6=G0}
{3=D0, 4=E0, 5=F0, 6=G0, 7=H0, 8=I0, 9=J0}
 */
