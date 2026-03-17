package ch21_arrays;

import java.util.Arrays;
import java.util.SplittableRandom;

/**
 * @author runningpig66
 * @date 3月16日 周一
 * @time 3:49
 * P.758 §21.4 多维数组
 * 数组中构成矩阵的每组向量的大小没有限制 [ 称为不规则数组 (ragged array) ]：
 */
public class RaggedArray {
    static int val = 1;

    public static void main(String[] args) {
        SplittableRandom rand = new SplittableRandom(47);
        // 3-D array with varied-length vectors:
        int[][][] a = new int[rand.nextInt(7)][][];
        for (int i = 0; i < a.length; i++) {
            a[i] = new int[rand.nextInt(5)][];
            for (int j = 0; j < a[i].length; j++) {
                a[i][j] = new int[rand.nextInt(5)];
                Arrays.setAll(a[i][j], n -> val++); // [1]
            }
        }
        System.out.println(Arrays.deepToString(a));
    }
}
/* Output:
[[[1], []], [[2, 3, 4, 5], [6]], [[7, 8, 9], [10, 11, 12], []]]
 */
