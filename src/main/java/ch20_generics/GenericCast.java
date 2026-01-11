package ch20_generics;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2026/1/8 周四
 * @time 16:08
 * P.710 §20.10 问题 §20.10.3 类型转换和警告
 * <p>
 * 对类型参数使用类型转换或 instanceof 是没有任何效果的，
 * 下面这个集合在内部将元素存储为 Object，并在你读取它们的时候，将它们转回 T：
 * <p>
 * 如果没有 @SuppressWarnings 注解，编译器会在调用 pop() 和 stream() 时产生 “unchecked cast” 的警告。
 * 由于类型擦除的缘故，编译器无法知道类型转换是否是安全的。T 会被擦除为自身的第一个边界，默认情况下则是 Object，
 * 因此 pop() 实际上只是将 Object 转换成 Object。
 */
class FixedSizeStack<T> {
    private final int size;
    private Object[] storage;
    private int index = 0;

    FixedSizeStack(int size) {
        this.size = size;
        storage = new Object[size];
    }

    public void push(T item) {
        if (index < size) {
            storage[index++] = item;
        }
    }

    @SuppressWarnings("unchecked")
    public T pop() {
        // return index == 0 ? null : (T) storage[--index];
        // 1_2 是照着书写的，但如果我们要让这段代码符合“生产规范”，pop 方法应该稍微修改一下以防止内存泄漏（Memory Leak）：
        // 但是即便置为 null，Arrays.stream(storage) 仍然会把 null 也作为流的一个元素输出来，除非使用 Arrays.stream(storage, 0, index) 来只流化有效部分。
        if (index == 0) {
            return null;
        }
        // 在字节码层面，此处的 (T) 这个强制转换确实对应一个 CHECKCAST 指令，但它的参数是擦除后的类型————字节码：CHECKCAST java/lang/Object。
        // 真正的、有意义的 CHECKCAST 指令，不是插在 pop() 方法里，而是插在调用它的 main() 方法里（调用端）。见 // [1] 字节码视角（Main 方法中）：
        T result = (T) storage[--index];
        storage[index] = null;
        return result;
    }

    // 1_1 逻辑漏洞：注意看输出结果。你 pop() 出了一个 "S"，但在随后调用的 stream() 输出中，S 依然存在！
    @SuppressWarnings("unchecked")
    Stream<T> stream() {
        // 2 这种转换不是标准的 Java 面向对象转换，而是利用了类型擦除的特性，进行了一次未检查的转换（Unchecked Cast）。这确实很“生猛”，但在底层代码优化中很常见。
        return (Stream<T>) Arrays.stream(storage);
    }
}

public class GenericCast {
    static String[] letters = "ABCDEFGHIJKLMNOPQRS".split("");

    public static void main(String[] args) {
        FixedSizeStack<String> strings = new FixedSizeStack<>(letters.length);
        Arrays.stream(letters).forEach(strings::push);
        // [1] 字节码视角（Main 方法中）：
        // INVOKEVIRTUAL FixedSizeStack.pop ()Ljava/lang/Object; (调用方法，拿到 Object)
        // CHECKCAST java/lang/String (这才是真正的安检！)
        System.out.println(strings.pop());
        strings.stream()
                .map(s -> s + " ")
                .forEach(System.out::print);
    }
}
/* Output:
S
A B C D E F G H I J K L M N O P Q R null
 */
