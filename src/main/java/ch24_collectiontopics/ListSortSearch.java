package ch24_collectiontopics;

import java.util.*;

/**
 * @author runningpig66
 * @date 2月22日 周日
 * @time 1:43
 * P.147 §3.13 工具函数 §3.13.1 List 上的排序和查找
 * Sorting/searching Lists with Collections utilities
 * <p>
 * 在 List 上执行排序和查找的工具函数虽然与用于对象数组的那些拥有同样的名字和签名，
 * 但它们是 Collections 而非 Arrays 的 static 方法。下面的示例使用了来自 Utilities.java 的 list 数据：
 */
public class ListSortSearch {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>(Utilities.list);
        list.addAll(Utilities.list);
        System.out.println(list);
        Collections.shuffle(list, new Random(47));
        System.out.println("Shuffled: " + list);

        // 注：更简短的写法其实是 list.subList(10, list.size()).clear();，但作者借此向你展示了迭代器的强大游标能力。
        ListIterator<String> it = list.listIterator(10); // [1] list.listIterator(10) 在 list 的位置 10 处创建了一个 ListIterator。
        while (it.hasNext()) { // [2] 只要在这个 list 的位置 10 及其后面仍有元素，while 循环就会删除它们。
            it.next();
            it.remove();
        }
        System.out.println("Trimmed: " + list);

        // <T extends Comparable<? super T>> void sort(List<T> list)
        // <T> void sort(List<T> list, Comparator<? super T> c)
        // 使用自然顺序对参数指定的 List<T> 排序。第二种形式接受一个 Comparator 来排序。
        Collections.sort(list);
        System.out.println("Sorted: " + list);

        String key = list.get(7);
        // <T> int binarySearch(List<? extends Comparable<? super T>> list, T key)
        // <T> int binarySearch(List<? extends T> list, T key, Comparator<? super T> c)
        int index = Collections.binarySearch(list, key);
        System.out.println("Location of " + key + " is " + index +
                ", list.get(" + index + ") = " + list.get(index));

        Collections.sort(list, String.CASE_INSENSITIVE_ORDER); // 忽略大小写
        System.out.println("Case-insensitive sorted: " + list);

        key = list.get(7);
        // 同对数组执行查找和排序一样，如果使用一个 Comparator 来排序，也必须使用同样的 Comparator 来执行 binarySearch()。
        index = Collections.binarySearch(list, key, String.CASE_INSENSITIVE_ORDER); // 忽略大小写
        System.out.println("Location of " + key + " is " + index +
                ", list.get(" + index + ") = " + list.get(index));
    }
}
/* Output:
[one, Two, three, Four, five, six, one, one, Two, three, Four, five, six, one]
Shuffled: [Four, five, one, one, Two, six, six, three, three, five, Four, Two, one, one]
Trimmed: [Four, five, one, one, Two, six, six, three, three, five]
Sorted: [Four, Two, five, five, one, one, six, six, three, three]
Location of six is 7, list.get(7) = six
Case-insensitive sorted: [five, five, Four, one, one, six, six, three, three, Two]
Location of three is 7, list.get(7) = three
 */
