package ch21_arrays;

import onjava.ArrayShow;

import java.util.Arrays;

/**
 * @author runningpig66
 * @date 3月17日 周二
 * @time 20:45
 * P.764 §21.6 Arrays.fill()
 * Using Arrays.fill()
 * <p>
 * 在进行数组相关或者是更广义的编程试验时，通常需要便捷地创建数组并填充测试数据的方法。在 Java 标准库 Arrays 中有一个小方法 fill()，
 * 它可以将一个数值复制到数组的所有位置；对于对象数组，它可以将一个引用复制到数组的所有位置：
 * <p>
 * 你可以填充整个数组，或者像以下示例的最后两行语句那样，填充数组的指定范围。
 * 由于只能向 Arrays.fill() 传入一个数据值来填充，因此这样产生的数组往往不具有实际作用。
 */
public class FillingArrays {
    public static void main(String[] args) {
        int size = 6;
        boolean[] a1 = new boolean[size];
        byte[] a2 = new byte[size];
        char[] a3 = new char[size];
        short[] a4 = new short[size];
        int[] a5 = new int[size];
        long[] a6 = new long[size];
        float[] a7 = new float[size];
        double[] a8 = new double[size];
        String[] a9 = new String[size];
        Arrays.fill(a1, true);
        ArrayShow.show("a1", a1);
        Arrays.fill(a2, (byte) 11);
        ArrayShow.show("a2", a2);
        Arrays.fill(a3, 'x');
        ArrayShow.show("a3", a3);
        Arrays.fill(a4, (short) 17);
        ArrayShow.show("a4", a4);
        Arrays.fill(a5, 19);
        ArrayShow.show("a5", a5);
        Arrays.fill(a6, 23);
        ArrayShow.show("a6", a6);
        Arrays.fill(a7, 29f);
        ArrayShow.show("a7", a7);
        Arrays.fill(a8, 47d);
        ArrayShow.show("a8", a8);
        // static void fill(Object[] a, Object val)
        Arrays.fill(a9, "Hello");
        ArrayShow.show("a9", a9);
        // Manipulating ranges:
        // static void fill(Object[] a, int fromIndex, int toIndex, Object val)
        Arrays.fill(a9, 3, 5, "World");
        ArrayShow.show("a9", a9);
    }
}
/* Output:
a1: [true, true, true, true, true, true]
a2: [11, 11, 11, 11, 11, 11]
a3: [x, x, x, x, x, x]
a4: [17, 17, 17, 17, 17, 17]
a5: [19, 19, 19, 19, 19, 19]
a6: [23, 23, 23, 23, 23, 23]
a7: [29.0, 29.0, 29.0, 29.0, 29.0, 29.0]
a8: [47.0, 47.0, 47.0, 47.0, 47.0, 47.0]
a9: [Hello, Hello, Hello, Hello, Hello, Hello]
a9: [Hello, Hello, Hello, World, World, Hello]
 */
