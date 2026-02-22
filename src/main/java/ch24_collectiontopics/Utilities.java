package ch24_collectiontopics;

import java.util.*;

/**
 * @author runningpig66
 * @date 2月21日 周六
 * @time 22:09
 * P.145 §3.13 工具函数
 * Simple demonstrations of the Collections utilities
 */
public class Utilities {
    static List<String> list = Arrays.asList("one Two three Four five six one".split(" "));

    public static void main(String[] args) {
        System.out.println(list);

        // <T> Set<T> singleton(T o)
        // <T> List<T> singletonList(T o)
        // <K,V> Map<K,V> singletonMap(K key, V value)
        // 生成一个不可变的 Set<T>、List<T> 或 Map<K, V>，其中包含基于给定参数创建的一个元素。
        // boolean disjoint(Collection<?> c1, Collection<?> c2)
        // 如果参数中的两个 Collection 没有共同元素，则返回 true。
        System.out.println("'list' disjoint (Four)?: " +
                Collections.disjoint(list, Collections.singletonList("Four")));

        // <T extends Object & Comparable<? super T>> T max(Collection<? extends T> coll)
        // <T extends Object & Comparable<? super T>> T min(Collection<? extends T> coll)
        // 使用参数指向的 Collection 中的对象自然比较方法，计算其中最大或最小的元素。
        System.out.println("max: " + Collections.max(list));
        System.out.println("min: " + Collections.min(list));

        // <T> T max(Collection<? extends T> coll, Comparator<? super T> comp)
        // <T> T min(Collection<? extends T> coll, Comparator<? super T> comp)
        // 使用 Comparator 来计算 Collection 中最大或最小的元素。
        System.out.println("max w/ comparator: " + Collections.max(list, String.CASE_INSENSITIVE_ORDER));
        System.out.println("min w/ comparator: " + Collections.min(list, String.CASE_INSENSITIVE_ORDER));

        List<String> sublist = Arrays.asList("Four five six".split(" "));
        // int indexOfSubList(List<?> source, List<?> target)
        // 计算 target 在 source 中第一次出现的位置的起始索引；如果没有出现过，则返回 -1。
        System.out.println("indexOfSubList: " + Collections.indexOfSubList(list, sublist));
        // int lastIndexOfSubList(List<?> source, List<?> target)
        // 计算 target 在 source 中最后一次出现的位置的起始索引；如果没有出现过，则返回 -1。
        System.out.println("lastIndexOfSubList: " + Collections.lastIndexOfSubList(list, sublist));

        // <T> boolean replaceAll(List<T> list, T oldVal, T newVal)
        // 用 newVal 替换所有的 oldVal。
        // noinspection ResultOfMethodCallIgnored
        Collections.replaceAll(list, "one", "Yo");
        System.out.println("replaceAll: " + list);

        // void reverse(List<?> list)
        // 就地（in-place）将所有的元素变为逆向。
        Collections.reverse(list);
        System.out.println("reverse: " + list);

        // void rotate(List<?> list, int distance)
        // 将所有元素向前移动 distance 的距离，对于超出末尾的元素，将其放到列表的开始处。
        Collections.rotate(list, 3);
        System.out.println("rotate: " + list);

        List<String> source = Arrays.asList("in the matrix".split(" "));
        // <T> void copy(List<? super T> dest, List<? extends T> src)
        // 将 src 中的元素复制到 dest 中。执行该操作后，目标列表中每个已复制元素的索引将与其在源列表中的索引完全一致。
        Collections.copy(list, source);
        System.out.println("copy: " + list);

        // void swap(List<?> list, int i, int j)
        // 交换这个 List 中位于位置 i 和 j 处的元素。执行速度可能会比我们手写的代码更快。
        Collections.swap(list, 0, list.size() - 1);
        System.out.println("swap: " + list);

        // void shuffle(List<?> list)
        // void shuffle(List<?> list, Random rnd)
        // 随机变换指定的列表。第一种形式有自己的随机源，也可以用第二种形式提供我们的随机源。
        Collections.shuffle(list, new Random(47));
        System.out.println("shuffled: " + list);

        // <T> void fill(List<? super T> list, T obj)
        // 将 List 中的所有元素都替换为 obj。
        Collections.fill(list, "pop");
        System.out.println("fill: " + list);

        // int frequency(Collection<?> c, Object o)
        // 返回这个 Collection 中和 x 等价的元素的数量。
        System.out.println("frequency of 'pop': " + Collections.frequency(list, "pop"));

        // <T> List<T> nCopies(int n, T o)
        // 返回一个大小为 n 的不可变且享元的（Flyweight Pattern） List<T>，其中的所有引用都指向 x。
        List<String> dups = Collections.nCopies(3, "snap");
        System.out.println("dups: " + dups);
        System.out.println("'list' disjoint 'dups'?: " + Collections.disjoint(list, dups));

        // Getting an old-style Enumeration:
        // <T> Enumeration<T> enumeration(final Collection<T> c)
        // 生成一个老式的 Enumeration<T>。它是 Java 1.0 时代的迭代器（Iterator 的前身）。
        Enumeration<String> e = Collections.enumeration(dups);
        Vector<String> v = new Vector<>();
        while (e.hasMoreElements()) {
            v.addElement(e.nextElement());
        }

        // Converting an old-style Vector to a List via an Enumeration:
        // <T> ArrayList<T> list(Enumeration<T> e)
        // 生成一个 ArrayList<T>，包含按照顺序从（老式的）Enumeration（Iterator的前身）返回的元素。用于转换遗留代码。
        ArrayList<String> arrayList = Collections.list(v.elements());
        System.out.println("arrayList: " + arrayList);
    }
}
/* Output:
[one, Two, three, Four, five, six, one]
'list' disjoint (Four)?: false
max: three
min: Four
max w/ comparator: Two
min w/ comparator: five
indexOfSubList: 3`
lastIndexOfSubList: 3
replaceAll: [Yo, Two, three, Four, five, six, Yo]
reverse: [Yo, six, five, Four, three, Two, Yo]
rotate: [three, Two, Yo, Yo, six, five, Four]
copy: [in, the, matrix, Yo, six, five, Four]
swap: [Four, the, matrix, Yo, six, five, in]
shuffled: [six, matrix, the, Four, Yo, five, in]
fill: [pop, pop, pop, pop, pop, pop, pop]
frequency of 'pop': 7
dups: [snap, snap, snap]
'list' disjoint 'dups'?: true
arrayList: [snap, snap, snap]
 */
