package ch20_generics;

import java.lang.reflect.Array;

/**
 * @author runningpig66
 * @date 2025/12/30 周二
 * @time 2:57
 * P.684 §20.7 对类型擦除的补偿 §20.7.2 泛型数组
 * <p>
 * 对于新代码，应该传入一个类型标记。这种情况下的 GenericArray 看起来是这样的：
 * <p>
 * 这里将类型标记 Class<T> 传入了构造器，以用于擦除后的类型恢复，这样就能够创建实际所需类型的数组了，
 * 尽管还是必须通过 @SuppressWarnings 禁用类型转换导致的警告。如你在 main() 中所见，
 * 一旦得到了实际的类型，就可以将其返回，并生成想要的结果，数组的运行时类型是将确的 T[] 类型。
 * <p>
 * notes: 07-为什么需要 Array.newInstance？—从“写死”到“通用”的跨越.md
 * notes: 08-泛型数组的完整生命周期：从编译期推导到运行时反射.md
 */
public class GenericArrayWithTypeToken<T> {
    private T[] array;

    @SuppressWarnings("unchecked")
    public GenericArrayWithTypeToken(Class<T> type, int sz) {
        array = (T[]) Array.newInstance(type, sz);
    }

    public void put(int index, T item) {
        array[index] = item;
    }

    public T get(int index) {
        return array[index];
    }

    // Expose the underlying representation:
    public T[] rep() {
        return array;
    }

    public static void main(String[] args) {
        GenericArrayWithTypeToken<Integer> gai = new GenericArrayWithTypeToken<>(Integer.class, 10);
        // This now works:
        Integer[] ia = gai.rep();
    }
}
