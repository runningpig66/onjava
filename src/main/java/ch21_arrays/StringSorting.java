package ch21_arrays;

import onjava.ArrayShow;
import onjava.Rand;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author runningpig66
 * @date 2026-03-28
 * @time 1:00
 * P.800 §21.17 数组排序 §21.17.1 使用 Arrays.sort()
 * Sorting an array of Strings
 * <p>
 * 通过内建的排序方法，可以对任何基本类型数组或者对象类型（实现了 Comparable 接口或实现了对应的 Comparator）进行排序。
 * 下例中我们生成了一个随机字符串的数组，然后进行排序：
 * <p>
 * 注意该字符串排序算法中的输出使用的是字典顺序，因此会将所有大写字母开头的单词放在前面，小写字母开头的单词放在后面（电话簿就是典型的例子）。
 * 如果想忽略大小写分组，可以像例子中的最后一次调用 sort() 那样，使用 String.CASE_INSENSITIVE_ORDER。
 * <p>
 * Java 标准库中的排序算法已经为各种类型做了最优的设计——对基本类型使用快速排序，对对象使用稳定的归并排序。
 */
public class StringSorting {
    public static void main(String[] args) {
        String[] sa = new Rand.String().array(20);
        ArrayShow.show("Before sort", sa);
        Arrays.sort(sa);
        ArrayShow.show("After sort", sa);
        Arrays.sort(sa, Collections.reverseOrder());
        ArrayShow.show("Reverse sort", sa);
        Arrays.sort(sa, String.CASE_INSENSITIVE_ORDER);
        ArrayShow.show("Case-insensitive sort", sa);
    }
}
/* Output:
Before sort: [btpenpc, cuxszgv, gmeinne, eloztdv, ewcippc, ygpoalk, ljlbynx, taprwxz, bhmupju, cjwzmmr, anmkkyh, fcjpthl, skddcat, jbvlgwc, mvducuj, ydpulcq, zehpfmm, zrxmclh, qgekgly, hyoubzl]
After sort: [anmkkyh, bhmupju, btpenpc, cjwzmmr, cuxszgv, eloztdv, ewcippc, fcjpthl, gmeinne, hyoubzl, jbvlgwc, ljlbynx, mvducuj, qgekgly, skddcat, taprwxz, ydpulcq, ygpoalk, zehpfmm, zrxmclh]
Reverse sort: [zrxmclh, zehpfmm, ygpoalk, ydpulcq, taprwxz, skddcat, qgekgly, mvducuj, ljlbynx, jbvlgwc, hyoubzl, gmeinne, fcjpthl, ewcippc, eloztdv, cuxszgv, cjwzmmr, btpenpc, bhmupju, anmkkyh]
Case-insensitive sort: [anmkkyh, bhmupju, btpenpc, cjwzmmr, cuxszgv, eloztdv, ewcippc, fcjpthl, gmeinne, hyoubzl, jbvlgwc, ljlbynx, mvducuj, qgekgly, skddcat, taprwxz, ydpulcq, ygpoalk, zehpfmm, zrxmclh]
 */
