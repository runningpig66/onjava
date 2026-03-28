package ch21_arrays;

import onjava.ArrayShow;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author runningpig66
 * @date 2026-03-26
 * @time 19:54
 * P.799 §21.17 数组排序
 * Implementing a Comparator for a class
 * <p>
 * 也可以编写自定义的 Comparator。下面的示例通过 j（而不是 i）的值，对 CompType 对象进行了比较：
 * <p>
 * notes: notes/Transaction.java
 */
class CompTypeComparator implements Comparator<CompType> {
    @Override
    public int compare(CompType o1, CompType o2) {
        // return o1.j < o2.j ? -1 : (o1.j == o2.j ? 0 : 1);
        return Integer.compare(o1.j, o2.j);
    }
}

public class ComparatorTest {
    public static void main(String[] args) {
        CompType[] a = new CompType[12];
        Arrays.setAll(a, n -> CompType.get());
        ArrayShow.show("Before sorting", a);
        Arrays.sort(a, new CompTypeComparator());
        ArrayShow.show("After sorting", a);
    }
}
/* Output:
Before sorting: [[i = 35, j = 37], [i = 41, j = 20], [i = 77, j = 79]
, [i = 56, j = 68], [i = 48, j = 93], [i = 70, j = 7]
, [i = 0, j = 25], [i = 62, j = 34], [i = 50, j = 82]
, [i = 31, j = 67], [i = 66, j = 54], [i = 21, j = 6]
]
After sorting: [[i = 21, j = 6], [i = 70, j = 7], [i = 41, j = 20]
, [i = 0, j = 25], [i = 62, j = 34], [i = 35, j = 37]
, [i = 66, j = 54], [i = 31, j = 67], [i = 56, j = 68]
, [i = 77, j = 79], [i = 50, j = 82], [i = 48, j = 93]
]
 */
