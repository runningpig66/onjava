package ch21_arrays;

import onjava.ConvertTo;
import onjava.Rand;

import java.util.Arrays;

/**
 * @author runningpig66
 * @date 2026-03-26
 * @time 17:37
 * P.795 §21.16 流和数组
 * <p>
 * stream() 方法能够很方便地根据某些类型的数组生成流；相较于直接操作数组，先将数组转化为流通常更容易生成你想要的结果。
 * 要注意的是，尽管当前流 “用完了”（你无法重复消费流），但你仍然持有数组，因此还可以用别的方式继续使用它——包括再生成一个流。
 */
public class StreamFromArray {
    public static void main(String[] args) {
        String[] s = new Rand.String().array(10);
        // static <T> Stream<T> stream(T[] array)
        Arrays.stream(s)
                .skip(3)
                .limit(5)
                .map(ss -> ss + "!")
                .forEach(System.out::println);

        int[] ia = new Rand.Pint().array(10);
        // static IntStream stream(int[] array)
        Arrays.stream(ia)
                .skip(3)
                .limit(5)
                .map(i -> i * 10)
                .forEach(System.out::println);

        Arrays.stream(new long[10]);
        Arrays.stream(new double[10]);

        // Only int, long and double work:
        //- Arrays.stream(new boolean[10]);
        //- Arrays.stream(new byte[10]);
        //- Arrays.stream(new char[10]);
        //- Arrays.stream(new short[10]);
        //- Arrays.stream(new float[10]);

        // 只有 int、long 和 double 这些原生支持的类型适用于 Arrays.stream()，其他类型则总是需要通过包装类数组来实现。
        // For the other types you must use wrapped arrays:
        float[] fa = new Rand.Pfloat().array(10);
        Arrays.stream(ConvertTo.boxed(fa));
        Arrays.stream(new Rand.Float().array(10));
    }
}
/* Output:
eloztdv!
ewcippc!
ygpoalk!
ljlbynx!
taprwxz!
47200
61770
84790
66560
37680
 */
