package ch21_arrays;

import onjava.ArrayShow;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author runningpig66
 * @date 2026-03-26
 * @time 19:46
 * P.798 §21.17 数组排序
 * The Collections.reverseOrder() Comparator
 * <p>
 * 现在假设有人给了你一个没有实现 Comparable 接口的类，或者给了你一个实现了 Comparable 接口的类，但是你并不喜欢它的实现方式，决定要换一种。
 * 要解决这个问题，你可以单独创建一个实现了 Comparator 接口的类，它有两个方法：compare() 和 equals()。然而，除非有特殊的性能需求，
 * 否则你无须专门实现 equals() 方法。这是因为无论在何时创建一个类，它都隐式地继承于 Object 类，自然就会有 equals() 方法。可以直接用默认的 equals() 方法来满足接口的强制规范。
 * <p>
 * Collections 类包含 reverseOrder() 方法，用来生成和自然排序顺序相反的 Comparator，它在 ComType 中的应用如下所示：
 * <p>
 * notes: notes/Transaction.java
 */
public class Reverse {
    public static void main(String[] args) {
        CompType[] a = new CompType[12];
        Arrays.setAll(a, n -> CompType.get());
        ArrayShow.show("Before sorting", a);
        // static <T> void sort(T[] a, Comparator<? super T> c)
        Arrays.sort(a, Collections.reverseOrder());
        ArrayShow.show("After sorting", a);
    }
}
/* Output:
Before sorting: [[i = 35, j = 37], [i = 41, j = 20], [i = 77, j = 79]
, [i = 56, j = 68], [i = 48, j = 93], [i = 70, j = 7]
, [i = 0, j = 25], [i = 62, j = 34], [i = 50, j = 82]
, [i = 31, j = 67], [i = 66, j = 54], [i = 21, j = 6]
]
After sorting: [[i = 77, j = 79], [i = 70, j = 7], [i = 66, j = 54]
, [i = 62, j = 34], [i = 56, j = 68], [i = 50, j = 82]
, [i = 48, j = 93], [i = 41, j = 20], [i = 35, j = 37]
, [i = 31, j = 67], [i = 21, j = 6], [i = 0, j = 25]
]
 */
