package ch21_arrays;

import onjava.ArrayShow;

import java.util.Arrays;

/**
 * @author runningpig66
 * @date 3月17日 周二
 * @time 21:10
 * P.765 §21.7 Arrays.setAll()
 * <p>
 * 在之前的 RaggedArray.java 中，本书首次介绍了 Arrays.setAll() 的使用，并在 ArrayOfGenerics.java 中再次用到。
 * Arrays.setAll() 是 Java 8 中新引入的方法，它能利用生成器生成不同的值，具体的生成方式则由数组的索引元素决定（生成器通过访问当前索引来读取并修改数组的值）。
 * <p>
 * int、long 和 double 由专用的版本来处理，其他类型则由泛型版本来处理。生成器并非 Supplier，因为 Supplier 并不接收参数，
 * 而生成器则必须传入 int 型的数组索引作为参数。下面这个非常简单的 setAll() 例子应用了几个随手编写的 lambda 表达式和方法引用：
 */
class Bob {
    final int id;

    Bob(int n) {
        id = n;
    }

    @Override
    public String toString() {
        return "Bob" + id;
    }
}

public class SimpleSetAll {
    public static final int SZ = 8;
    static int val = 1;
    static char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    static char getChar(int n) {
        return chars[n];
    }

    public static void main(String[] args) {
        int[] ia = new int[SZ];
        long[] la = new long[SZ];
        double[] da = new double[SZ];
        // static void setAll(int[] array, IntUnaryOperator generator)
        Arrays.setAll(ia, n -> n); // [1] 此例中直接把数组索引作为值插入。在 long 和 double 的版本中，该转换是自动进行的。
        // static void setAll(long[] array, IntToLongFunction generator)
        Arrays.setAll(la, n -> n);
        // static void setAll(double[] array, IntToDoubleFunction generator)
        Arrays.setAll(da, n -> n);
        ArrayShow.show(ia);
        ArrayShow.show(la);
        ArrayShow.show(da);
        Arrays.setAll(ia, n -> val++); // [2] 该函数只需要接收索引参数，就能生成相应的结果。这里忽略了索引的值，而使用 val 来生成结果。
        Arrays.setAll(la, n -> val++);
        Arrays.setAll(da, n -> val++);
        ArrayShow.show(ia);
        ArrayShow.show(la);
        ArrayShow.show(da);

        Bob[] ba = new Bob[SZ];
        // static <T> void setAll(T[] array, IntFunction<? extends T> generator)
        // [3] 本方法引用是有效的，因为 Bob 的构造器接收 int 类型参数。只要我们传入的函数能接收 int 类型参数并生成预期的结果，这种方式就有效。
        Arrays.setAll(ba, Bob::new);
        ArrayShow.show(ba);

        Character[] ca = new Character[SZ];
        // [4] 要处理 int、long 和 double 之外的基本类型，则需要为该基本类型编写相匹配的包装类型数组，然后使用泛型版本的 setAll()。
        // 要注意 getChar() 生成的是基本类型，所以这里将其自动装箱为 Character。
        Arrays.setAll(ca, SimpleSetAll::getChar);
        ArrayShow.show(ca);
    }
}
/* Output:
[0, 1, 2, 3, 4, 5, 6, 7]
[0, 1, 2, 3, 4, 5, 6, 7]
[0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0]
[1, 2, 3, 4, 5, 6, 7, 8]
[9, 10, 11, 12, 13, 14, 15, 16]
[17.0, 18.0, 19.0, 20.0, 21.0, 22.0, 23.0, 24.0]
[Bob0, Bob1, Bob2, Bob3, Bob4, Bob5, Bob6, Bob7]
[a, b, c, d, e, f, g, h]
 */
