package ch21_arrays;

import onjava.ArrayShow;
import onjava.Rand;

import java.util.Arrays;

/**
 * @author runningpig66
 * @date 2026-03-23
 * @time 21:26
 * P.788 §21.11 修改已有的数组元素
 * <p>
 * 传给 Arrays.setAll() 的生成器函数可以通过传给它的数组索引来修改已存在的数组元素：
 */
public class ModifyExisting {
    public static void main(String[] args) {
        double[] da = new double[7];
        // static void setAll(double[] array, IntToDoubleFunction generator)
        Arrays.setAll(da, new Rand.Double()::get);
        ArrayShow.show(da);
        Arrays.setAll(da, n -> da[n] / 100); // [1] lambda 表达式在这里特别有用，因为数组正好一直在表达式的作用范围内。
        ArrayShow.show(da);
    }
}
/* Output:
[4.83, 2.89, 2.9, 1.97, 3.01, 0.18, 0.99]
[0.0483, 0.028900000000000002, 0.028999999999999998, 0.0197, 0.0301, 0.0018, 0.009899999999999999]
 */
