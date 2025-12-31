package ch20_generics;

/**
 * @author runningpig66
 * @date 2025/12/29 周一
 * @time 3:06
 * P.676 §20.7 对类型擦除的补偿
 * {WillNotCompile}
 * <p>
 * 由于类型擦除的缘故，我们失去了在泛型代码中执行某些操作的能力。任何需要在运行时知道确切类型的操作都无法运行：
 * <p>
 * 你可以偶尔在编程时绕过这些问题，但是有时你必须通过引入类型标签（type tag）来补偿类型擦除导致的损失。
 * 这意味着要在类型表达式中显式地为你要使用的类型传入一个 Class 对象。
 * <p>
 * notes: 06-泛型擦除的边界：为什么 new T() 和 instanceof T 被禁止？.md
 * notes: 07-为什么需要 Array.newInstance？—从“写死”到“通用”的跨越.md
 * notes: 08-泛型数组的完整生命周期：从编译期推导到运行时反射.md
 */
public class Erased<T> {
    private final int SIZE = 100;

    public void f(Object arg) {
        // error: illegal generic type for instanceof
        if (arg instanceof T) {
        }

        // error: unexpected type
        T var = new T();

        // error: generic array creation
        T[] array = new T[SIZE];

        // warning: [unchecked] unchecked cast
        T[] array1 = (T[]) new Object[SIZE];
    }
}
