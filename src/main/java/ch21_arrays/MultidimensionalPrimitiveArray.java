package ch21_arrays;

import java.util.Arrays;

/**
 * @author runningpig66
 * @date 3月16日 周一
 * @time 3:41
 * P.757 §21.4 多维数组
 * 如果要创建多维的基本类型数组，你需要使用大括号来分隔数组中的每个向量：
 */
public class MultidimensionalPrimitiveArray {
    public static void main(String[] args) {
        int[][] a = {{1, 2, 3}, {4, 5, 6},};
        // 本例中使用了 Arrays.deepToString() 方法，将多维数组转化为 String，以在输出结果中格式化显示。
        // public static String deepToString(Object[] a)
        System.out.println(Arrays.deepToString(a));
    }
}
/* Output:
[[1, 2, 3], [4, 5, 6]]
 */
