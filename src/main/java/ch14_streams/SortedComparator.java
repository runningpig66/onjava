package ch14_streams;

import java.util.Comparator;

/**
 * @author runningpig66
 * @date 2025/10/28 周二
 * @time 22:59
 * 代码清单 P.389 对流元素进行排序：我们在 Randoms.java 中看到过以默认的比较方式使用 sorted() 进行排序的情况。
 * 还有一种接受 Comparator 参数的 sorted() 形式
 */
public class SortedComparator {
    public static void main(String[] args) throws Exception {
        FileToWords.stream("src/main/java/ch14_streams/Cheese.dat")
                .skip(10)
                .limit(10)
                .sorted(Comparator.reverseOrder())
                .map(w -> w + " ")
                .forEach(System.out::print);
    }
}
/* Output:
you what to the that sir leads in district And
 */
