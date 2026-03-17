package ch21_arrays;

import java.util.Arrays;

/**
 * @author runningpig66
 * @date 3月17日 周二
 * @time 3:08
 * P.760 §21.4 多维数组
 * Creating multidimensional arrays
 * <p>
 * 下面演示了如何一步步的构造一个非基本类型的对象数组：
 */
public class AssemblingMultidimensionalArrays {
    public static void main(String[] args) {
        Integer[][] a;
        a = new Integer[3][];
        for (int i = 0; i < a.length; i++) {
            a[i] = new Integer[3];
            for (int j = 0; j < a[i].length; j++) {
                a[i][j] = i * j; // Autoboxing
            }
        }
        System.out.println(Arrays.deepToString(a));
    }
}
/* Output:
[[0, 0, 0], [0, 1, 2], [0, 2, 4]]
 */
