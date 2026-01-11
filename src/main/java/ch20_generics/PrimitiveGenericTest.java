package ch20_generics;

import onjava.Rand;

import java.util.Arrays;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

/**
 * @author runningpig66
 * @date 2026/1/7 周三
 * @time 17:39
 * P.708 §20.10 问题 §20.10.1 基本类型不可作为类型参数
 * <p>
 * 自动装箱可以解决部分问题，但无法解决所有问题。【数组不支持自动装箱。int[] 并非 Integer[] 的子类型，两者无继承关系，互不兼容。】
 * 在下面这个示例中，接口 FillArray 包含了一系列通过 Supplier 向数组中填入对象的方法（因为是静态方法，所以这里无法将类定义为泛型类）。
 * Supplier 的定义见第 21 章，而在 main() 中你可以看到，通过 FillArray.fill() 向数组中填充了对象。
 * <p>
 * 自动装箱机制不会对数组生效，因此我们需要实现 FillArray.fill() 的重载版本，或者实现一个生成器，来生成包装后的输出结果。
 * FillArray 只是稍微比 java.util.Arrays.setAll() 更有用一点。因为它可以返回填充后的数组。
 */
// Fill an array using a generator:
interface FillArray {
    static <T> T[] fill(T[] a, Supplier<T> gen) {
        Arrays.setAll(a, n -> gen.get());
        return a;
    }

    static int[] fill(int[] a, IntSupplier gen) {
        Arrays.setAll(a, n -> gen.getAsInt());
        return a;
    }

    static long[] fill(long[] a, LongSupplier gen) {
        Arrays.setAll(a, n -> gen.getAsLong());
        return a;
    }

    static double[] fill(double[] a, DoubleSupplier gen) {
        Arrays.setAll(a, n -> gen.getAsDouble());
        return a;
    }
}

public class PrimitiveGenericTest {
    public static void main(String[] args) {
        String[] strings = FillArray.fill(new String[5], new Rand.String(9));
        System.out.println(Arrays.toString(strings));
        int[] integers = FillArray.fill(new int[9], new Rand.Pint());
        System.out.println(Arrays.toString(integers));
    }
}
/* Output:
[btpenpccu, xszgvgmei, nneeloztd, vewcippcy, gpoalkljl]
[635, 8737, 3941, 4720, 6177, 8479, 6656, 3768, 4948]
 */
