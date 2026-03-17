package ch21_arrays;

import java.util.Arrays;

/**
 * @author runningpig66
 * @date 3月16日 周一
 * @time 3:45
 * P.757 §21.4 多维数组
 * 你也可以使用关键字 new 来创建数组。下例便以此方法创建了一个三维数组：
 */
public class ThreeDWithNew {
    public static void main(String[] args) {
        // 3-D array with fixed length:
        int[][][] a = new int[2][2][4];
        System.out.println(Arrays.deepToString(a));
    }
}
/* Output:
[[[0, 0, 0, 0], [0, 0, 0, 0]], [[0, 0, 0, 0], [0, 0, 0, 0]]]
 */
