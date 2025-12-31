package ch20_generics;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @author runningpig66
 * @date 2025/12/27 周六
 * @time 21:57
 * P.672 §20.6 类型擦除的奥秘 §20.6.4 边界的行为
 * <p>
 * 类型擦除的存在，使我发现了泛型最容易令人困惑的方面————可以将无意义的事物表达出来。
 * <p>
 * 尽管 kind 看起来是被存储为 Class<T>，但是类型擦除意味着它实际上只是被存储为一个不带参数的 Class。
 * 因此，在你使用它的时候，如同创建数组一样，Array.newInstance() 并不能实际掌握 kind 含有的类型信息。
 * 它无法生成具体的结果，因此必须进行类型转换，这会产生令人不悦的警告。
 * <p>
 * 注意对 Array.newInstance() 的使用，这是在泛型中创建数组的推荐方式。
 * public static Object newInstance(Class<?> componentType, int length) throws NegativeArraySizeException
 * <p>
 * notes: 03-泛型数组创建与类型擦除机制解析.md
 * notes: 04-泛型边界行为：数组的运行时依赖与集合的编译期一致性.md
 * notes: 07-为什么需要 Array.newInstance？—从“写死”到“通用”的跨越.md
 * notes: 08-泛型数组的完整生命周期：从编译期推导到运行时反射.md
 */
public class ArrayMaker<T> {
    private Class<T> kind;

    public ArrayMaker(Class<T> kind) {
        this.kind = kind;
    }

    T[] create(int size) {
        // Warning: Unchecked cast: 'java.lang.Object' to 'T[]'
        return (T[]) Array.newInstance(kind, size);
    }

    public static void main(String[] args) {
        //- ArrayMaker<String> exceptionMaker = new ArrayMaker(Integer.class);
        ArrayMaker<String> stringMaker = new ArrayMaker<>(String.class);
        String[] stringArray = stringMaker.create(9);
        System.out.println(Arrays.toString(stringArray));
    }
}
/* Output:
// Note: ArrayMaker.java uses unchecked or unsafe operations.
// Note: Recompile with -Xlint:unchecked for details.
[null, null, null, null, null, null, null, null, null]
 */
