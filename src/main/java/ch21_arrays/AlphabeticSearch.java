package ch21_arrays;

import onjava.ArrayShow;
import onjava.Rand;

import java.util.Arrays;

/**
 * @author runningpig66
 * @date 2026-03-28
 * @time 3:21
 * P.803 §21.18 用 Arrays.binarySearch() 进行二分查找
 * Searching with a Comparator import
 * <p>
 * 如果使用了 Comparator 来对对象数组进行排序（基本类型的数组不允许使用 Comparator 来排序），那么在使用 binarySearch()
 * （适用的 binarySearch() 重载版本）时，也需要引用同一个 Comparator。例如 StringSorting.java 就可以通过改造来实现查找：
 * <p>
 * Comparator 必须作为第三个参数传给重载的 binarySearch() 方法。此例能保证运行成功，因为待查找目标本来就是从数组自身的元素中选取的。
 */
public class AlphabeticSearch {
    public static void main(String[] args) {
        String[] sa = new Rand.String().array(30);
        Arrays.sort(sa, String.CASE_INSENSITIVE_ORDER);
        ArrayShow.show(sa);
        // static <T> int binarySearch(T[] a, T key, Comparator<? super T> c)
        int index = Arrays.binarySearch(sa, sa[10], String.CASE_INSENSITIVE_ORDER);
        System.out.println("Index: " + index + "\n" + sa[index]);
    }
}
/* Output:
[anmkkyh, bhmupju, btpenpc, cjwzmmr, cuxszgv, eloztdv, ewcippc, ezdeklu, fcjpthl, fqmlgsh, gmeinne, hyoubzl, jbvlgwc, jlxpqds, ljlbynx, mvducuj, qgekgly, skddcat, taprwxz, uybypgp, vjsszkn, vniyapk, vqqakbm, vwodhcf, ydpulcq, ygpoalk, yskvett, zehpfmm, zofmmvm, zrxmclh]
Index: 10
gmeinne
 */
