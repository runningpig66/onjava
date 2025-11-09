package ch14_streams;

import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author runningpig66
 * @date 2025/10/19 周日
 * @time 12:31
 * 代码清单 P.376 显然命令式编程理解起来要更困难
 */
public class ImperativeRandoms {
    public static void main(String[] args) {
        Random rand = new Random(47);
        SortedSet<Integer> rints = new TreeSet<>();
        while (rints.size() < 7) {
            /* PS: P.377 作者吐槽：“nextInt() 没有设置下界的选项，这使得代码更复杂了：它内置的下界总是零…”
            直到 Java 17, Random 才新增了 nextInt(int origin, int bound). */
            int r = rand.nextInt(5, 20);
            if (r < 5) continue;
            rints.add(r);
        }
        System.out.println(rints);
    }
}
/* Output:
[7, 8, 9, 11, 13, 15, 18]
 */
