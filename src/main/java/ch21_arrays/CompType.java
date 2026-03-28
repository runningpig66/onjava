package ch21_arrays;

import onjava.ArrayShow;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.SplittableRandom;

/**
 * @author runningpig66
 * @date 2026-03-26
 * @time 18:17
 * P.797 §21.17 数组排序
 * Implementing Comparable in a class
 * <p>
 * Java 有两种方式来实现比较功能。第一种是给目标类增加 “原生” 的比较方法，通过实现 java.lang.Comparable 接口即可，该接口十分简单，
 * 只有一个方法：compareTo()。该方法接受另一个相同类型的对象作为参数，如果当前对象比传入对象更小就返回负值，相等就返回 0，更大就返回正值。
 * 下面这个类实现了 Comparable 接口，并使用 Java 标准库方法 Arrays.sort() 来演示了其可比较性。
 */
public class CompType implements Comparable<CompType> {
    int i;
    int j;
    private static int count = 1;

    public CompType(int n1, int n2) {
        i = n1;
        j = n2;
    }

    @Override
    public String toString() {
        String result = "[i = " + i + ", j = " + j + "]";
        if (count++ % 3 == 0) {
            result += "\n";
        }
        return result;
    }

    @Override
    public int compareTo(@NonNull CompType rv) {
        // return i < rv.i ? -1 : (i == rv.i ? 0 : 1);
        return Integer.compare(i, rv.i);
    }

    private static SplittableRandom r = new SplittableRandom(47);

    public static CompType get() {
        return new CompType(r.nextInt(100), r.nextInt(100));
    }

    public static void main(String[] args) {
        CompType[] a = new CompType[12];
        Arrays.setAll(a, n -> get());
        ArrayShow.show("Before sorting", a);
        // 如果没有实现 Comparable 接口，在调用 sort() 方法时会抛出运行时异常 ClassCastException。
        // 这是因为 sort() 方法会在底层比较时，将其参数数组中的元素转型为 Comparable。
        // static void sort(Object[] a)
        Arrays.sort(a);
        ArrayShow.show("After sorting", a);
    }
}
/* Output:
Before sorting: [[i = 35, j = 37], [i = 41, j = 20], [i = 77, j = 79]
, [i = 56, j = 68], [i = 48, j = 93], [i = 70, j = 7]
, [i = 0, j = 25], [i = 62, j = 34], [i = 50, j = 82]
, [i = 31, j = 67], [i = 66, j = 54], [i = 21, j = 6]
]
After sorting: [[i = 0, j = 25], [i = 21, j = 6], [i = 31, j = 67]
, [i = 35, j = 37], [i = 41, j = 20], [i = 48, j = 93]
, [i = 50, j = 82], [i = 56, j = 68], [i = 62, j = 34]
, [i = 66, j = 54], [i = 70, j = 7], [i = 77, j = 79]
]
 */
