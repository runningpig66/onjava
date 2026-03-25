package onjava;

import java.util.Arrays;
import java.util.SplittableRandom;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

/**
 * @author runningpig66
 * @date 3月21日 周六
 * @time 20:42
 * P.775 §21.9 随机数生成器
 * Generate random values of different types
 * <p>
 * 可以参照 Count.java 的结构来创造一个生成随机数的工具；对于除了 int、long 和 double 以外的基本类型生成器，
 * 我们只保留了数组的生成，而不像在 Count 中那样是完整的操作集合。这只是设计上的选择，因为作为教学示例，并不需要那么多额外的功能。
 */
public interface Rand {
    int MOD = 10_000;

    class Boolean implements Supplier<java.lang.Boolean> {
        SplittableRandom r = new SplittableRandom(47);

        @Override
        public java.lang.Boolean get() {
            return r.nextBoolean();
        }

        public java.lang.Boolean get(int n) {
            return get();
        }

        public java.lang.Boolean[] array(int sz) {
            java.lang.Boolean[] result = new java.lang.Boolean[sz];
            Arrays.setAll(result, n -> get());
            return result;
        }
    }

    class Pboolean {
        public boolean[] array(int sz) {
            return ConvertTo.primitive(new Boolean().array(sz));
        }
    }

    class Byte implements Supplier<java.lang.Byte> {
        SplittableRandom r = new SplittableRandom(47);

        @Override
        public java.lang.Byte get() {
            return (byte) r.nextInt(MOD);
        }

        public java.lang.Byte get(int n) {
            return get();
        }

        public java.lang.Byte[] array(int sz) {
            java.lang.Byte[] result = new java.lang.Byte[sz];
            Arrays.setAll(result, n -> get());
            return result;
        }
    }

    class Pbyte {
        public byte[] array(int sz) {
            return ConvertTo.primitive(new Byte().array(sz));
        }
    }

    class Character implements Supplier<java.lang.Character> {
        SplittableRandom r = new SplittableRandom(47);

        @Override
        public java.lang.Character get() {
            return (char) r.nextInt('a', 'z' + 1);
        }

        public java.lang.Character get(int n) {
            return get();
        }

        public java.lang.Character[] array(int sz) {
            java.lang.Character[] result = new java.lang.Character[sz];
            Arrays.setAll(result, n -> get());
            return result;
        }
    }

    class Pchar {
        public char[] array(int sz) {
            return ConvertTo.primitive(new Character().array(sz));
        }
    }

    class Short implements Supplier<java.lang.Short> {
        SplittableRandom r = new SplittableRandom(47);

        @Override
        public java.lang.Short get() {
            return (short) r.nextInt(MOD);
        }

        public java.lang.Short get(int n) {
            return get();
        }

        public java.lang.Short[] array(int sz) {
            java.lang.Short[] result = new java.lang.Short[sz];
            Arrays.setAll(result, n -> get());
            return result;
        }
    }

    class Pshort {
        public short[] array(int sz) {
            return ConvertTo.primitive(new Short().array(sz));
        }
    }

    class Integer implements Supplier<java.lang.Integer> {
        SplittableRandom r = new SplittableRandom(47);

        @Override
        public java.lang.Integer get() {
            return r.nextInt(MOD);
        }

        public java.lang.Integer get(int n) {
            return get();
        }

        public java.lang.Integer[] array(int sz) {
            int[] primitive = new Pint().array(sz);
            java.lang.Integer[] result = new java.lang.Integer[sz];
            for (int i = 0; i < sz; i++) {
                result[i] = primitive[i];
            }
            return result;
        }
    }

    class Pint implements IntSupplier {
        SplittableRandom r = new SplittableRandom(47);

        @Override
        public int getAsInt() {
            return r.nextInt(MOD);
        }

        public int get(int n) {
            return getAsInt();
        }

        public int[] array(int sz) {
            return r.ints(sz, 0, MOD).toArray();
        }
    }

    class Long implements Supplier<java.lang.Long> {
        SplittableRandom r = new SplittableRandom(47);

        @Override
        public java.lang.Long get() {
            return r.nextLong(MOD);
        }

        public java.lang.Long get(int n) {
            return get();
        }

        public java.lang.Long[] array(int sz) {
            long[] primitive = new Plong().array(sz);
            java.lang.Long[] result = new java.lang.Long[sz];
            for (int i = 0; i < sz; i++) {
                result[i] = primitive[i];
            }
            return result;
        }
    }

    class Plong implements LongSupplier {
        SplittableRandom r = new SplittableRandom(47);

        @Override
        public long getAsLong() {
            return r.nextLong(MOD);
        }

        public long get(int n) {
            return getAsLong();
        }

        public long[] array(int sz) {
            return r.longs(sz, 0, MOD).toArray();
        }
    }

    class Float implements Supplier<java.lang.Float> {
        SplittableRandom r = new SplittableRandom(47);

        @Override
        public java.lang.Float get() {
            return (float) trim(r.nextDouble());
        }

        public java.lang.Float get(int n) {
            return get();
        }

        public java.lang.Float[] array(int sz) {
            java.lang.Float[] result = new java.lang.Float[sz];
            Arrays.setAll(result, n -> get());
            return result;
        }
    }

    class Pfloat {
        public float[] array(int sz) {
            return ConvertTo.primitive(new Float().array(sz));
        }
    }

    // 将区间 [0.0, 1.0) 内的原始随机浮点数，映射为 [0.0, 10.0) 范围内且最多保留两位小数的数值。
    // 1. 乘以 1000.0：小数点右移三位（如 0.56789... -> 567.89...）
    // 2. Math.round 取整：四舍五入截断多余小数（如 567.89... -> 568）
    // 3. 除以 100.0：小数点左移两位，保留两位小数精度（如 568 -> 5.68）
    static double trim(double d) {
        // static long round(double a) // 四舍五入取整
        return ((double) Math.round(d * 1000.0)) / 100.0;
    }

    class Double implements Supplier<java.lang.Double> {
        SplittableRandom r = new SplittableRandom(47);

        @Override
        public java.lang.Double get() {
            return trim(r.nextDouble());
        }

        public java.lang.Double get(int n) {
            return get();
        }

        public java.lang.Double[] array(int sz) {
            double[] primitive = new Rand.Pdouble().array(sz);
            java.lang.Double[] result = new java.lang.Double[sz];
            for (int i = 0; i < sz; i++) {
                result[i] = primitive[i];
            }
            return result;
        }
    }

    class Pdouble implements DoubleSupplier {
        SplittableRandom r = new SplittableRandom(47);

        @Override
        public double getAsDouble() {
            return trim(r.nextDouble());
        }

        public double get(int n) {
            return getAsDouble();
        }

        public double[] array(int sz) {
            double[] result = r.doubles(sz).toArray();
            // static void setAll(double[] array, IntToDoubleFunction generator)
            // 此处的 Lambda 表达式虽然合法，但在语义上存在对同一数组位置的重复赋值。
            // 根据 Java 语言规范（JLS），赋值操作（=）本身是一个表达式，其运算结果即为赋值完成后的目标值。
            // 因此，表达式 result[n] = trim(result[n]) 在执行时，会先将 trim 后的结果赋给 result[n]，
            // 并将该结果作为整个 Lambda 的返回值抛出。当 Arrays.setAll 框架接收到这个返回值后，
            // 会在底层再次将其赋值给原数组的同一个位置，从而导致了冗余操作。
            // 规范的写法应当仅计算并返回结果，将真正的赋值动作交由底层框架统一处理，即：n -> trim(result[n])
            Arrays.setAll(result, n -> result[n] = trim(result[n]));
            return result;
        }
    }

    class String implements Supplier<java.lang.String> {
        SplittableRandom r = new SplittableRandom(47);
        private int strlen = 7; // Default length

        public String() {
        }

        public String(int strLength) {
            strlen = strLength;
        }

        @Override
        public java.lang.String get() {
            return r.ints(strlen, 'a', 'z' + 1)
                    // <R> R collect(Supplier<R> supplier,
                    //              ObjIntConsumer<R> accumulator,
                    //              BiConsumer<R, R> combiner);
                    // 基本类型流（IntStream.java）的 collect 聚合操作机制。
                    // 这是一段避免装箱/拆箱性能损耗的最佳实践，利用 IntStream 配合 StringBuilder 直接在底层完成类型转换与可变结果的拼接。
                    // 与对象流 Stream<T> 不同，基本类型流的收集器在累加环节专门使用了 ObjIntConsumer<R>。该 collect 方法按序接收以下三个参数：
                    // 1. 容器提供者 (Supplier)：此处为 StringBuilder::new。在收集操作初始化时，
                    // 调用其无参构造函数创建一个空的 StringBuilder 实例，作为后续承载状态突变（Mutable Reduction）的最终结果容器。
                    // 2. 元素累加器 (ObjIntConsumer)：此处为 StringBuilder::appendCodePoint。
                    // 它接收当前的累加容器与流中到达的 int 元素，等价于 (sb, i) -> sb.appendCodePoint(i)，负责将数值逐一转换为字符并写入容器。
                    // 3. 并行合并器 (BiConsumer)：此处为 StringBuilder::append。该操作仅在并行流（Parallel Stream）环境下触发。
                    // 当流被拆分至多线程执行时，合并器负责将各个线程独立生成的局部 StringBuilder 实例首尾相连，等价于 (sb1, sb2) -> sb1.append(sb2)。
                    .collect(StringBuilder::new,
                            // StringBuilder appendCodePoint(int codePoint)
                            // 此处必须使用 StringBuilder::appendCodePoint 而非 append(int)。
                            // 因为 r.ints() 生成的是基本类型流 IntStream，流中的元素底层类型为 int（如字符 'a' 对应的 97）。
                            // 如果调用 append(int)，StringBuilder 会将该整数作为普通数值处理，将其转换为字符串 "97" 追加，
                            // 导致最终结果变为无意义的纯数字串。而 appendCodePoint(int) 明确约定将传入的 int 值视为一个 Unicode 代码点，
                            // 它可以正确解析底层数字对应的实际 Unicode 字符，从而实现将数字流安全地拼装为真正的字符串。
                            StringBuilder::appendCodePoint,
                            StringBuilder::append)
                    .toString();
        }

        public java.lang.String get(int n) {
            return get();
        }

        public java.lang.String[] array(int sz) {
            java.lang.String[] result = new java.lang.String[sz];
            Arrays.setAll(result, n -> get());
            return result;
        }
    }
}
