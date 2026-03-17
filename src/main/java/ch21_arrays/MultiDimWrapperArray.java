package ch21_arrays;

import java.util.Arrays;

/**
 * @author runningpig66
 * @date 3月17日 周二
 * @time 3:15
 * P.760 §21.4 多维数组
 * Multidimensional arrays of "wrapper" objects
 */
public class MultiDimWrapperArray {
    public static void main(String[] args) {
        Integer[][] a1 = { // Autoboxing
                {1, 2, 3},
                {4, 5, 6},
        };
        Double[][][] a2 = { // Autoboxing
                {{1.1, 2.2}, {3.3, 4.4}},
                {{5.5, 6.6}, {7.7, 8.8}},
                {{9.9, 1.2}, {2.3, 3.4}},
        };
        String[][] a3 = {
                {"The", "Quick", "Sly", "Fox"},
                {"Jumped", "Over"},
                {"The", "Lazy", "Brown", "Dog", "&", "friend"},
        };
        System.out.println("a1: " + Arrays.deepToString(a1));
        System.out.println("a2: " + Arrays.deepToString(a2));
        System.out.println("a3: " + Arrays.deepToString(a3));
    }
}
/* Output:
a1: [[1, 2, 3], [4, 5, 6]]
a2: [[[1.1, 2.2], [3.3, 4.4]], [[5.5, 6.6], [7.7, 8.8]], [[9.9, 1.2], [2.3, 3.4]]]
a3: [[The, Quick, Sly, Fox], [Jumped, Over], [The, Lazy, Brown, Dog, &, friend]]
 */
