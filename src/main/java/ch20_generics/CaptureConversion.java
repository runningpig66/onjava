package ch20_generics;

/**
 * @author runningpig66
 * @date 2026/1/6 周二
 * @time 2:04
 * P.705 §20.9 通配符 §20.9.4 捕获转换
 * <p>
 * 有一种情况下特别需要使用 <?> 而不是原生类型。如果向某个使用了 <?> 的方法传入了原生类型，
 * 编译器有可能会推断出具体的类型参数，因此该方法可以转而调用另一个使用了该具体类型的方法。下面这个示例演示了这项技巧，
 * 这技巧称为捕获转换（capture conversion），这是因为它可以捕获未指定的通配符类型，并将其转化为某个具体类型。
 * <p>
 * f1() 中的类型参数都是具体的类型，而未使用通配符或边界。在 f2() 中，Holder 参数是无边界通配符，
 * 因此它看起来似乎是未知的（类型）。但是在 f2() 中，对 f1() 进行了调用。而 f1() 需要已知类型的参数。
 * 所以这里面实际上是在调用 f2() 的过程中捕获了参数的类型，并将其用于对 f1() 的调用。
 * <p>
 * 你可能会想知道，该技巧是否可以用于写入场景，而这需要你在传入 Holder<?> 的同时，额外传入一个具体的类型。
 * 捕获转换仅适用于“在方法中必须使用确切类型”的情况。注意，你无法从 f2() 方法中返回 T，因为对于 f2() 来说，T 是未知的。
 */
public class CaptureConversion {
    // 泛型方法 f1：捕获转换的辅助方法。该方法定义了具体的类型参数 <T>。虽然调用者使用通配符，但在本方法内部，T 被锁定为 Holder 自身携带的确定类型。
    // 这种确定性恢复了数据的“可写性”，使得内部可以安全地执行依赖类型一致性的操作（如读取并回写、元素交换），编译器能确保写入的数据与读取的数据属于同一类型。
    private static <T> void f1(Holder<T> holder) {
        T t = holder.get();
        System.out.println(t.getClass().getSimpleName());
    }

    // 公共接口 f2：接收通配符参数的 API 门面。参数 Holder<?> 提供了最大的兼容性，但通配符 ? 的存在导致容器在当前作用域下变为“只读”（无法写入具体对象）。
    // 通过调用 f1(holder)，编译器执行 捕获转换 (Capture Conversion)：
    // 1. 将无界通配符 ? 捕获为一个临时的具体类型变量（标记为 CAP#1，CAP#1 extends Object）。
    // 2. 将该变量传递给 f1 的类型参数 T。这一桥接过程将模糊的通配符转化为具体的类型参数，从而恢复了对数据的操作能力。
    public static void f2(Holder<?> holder) {
        f1(holder); // Call with captured type
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Holder raw = new Holder<>(1);

        f1(raw);
        // CaptureConversion.java:39: warning: [unchecked] unchecked method invocation: method f1 in class CaptureConversion is applied to given types
        //         f1(raw);
        //   ^
        // required: Holder<T>
        // found:    Holder
        // where T is a type-variable:
        // T extends Object declared in method <T>f1(Holder<T>)
        // CaptureConversion.java:25: warning: [unchecked] unchecked conversion
        // f1(raw);
        //    ^
        // required: Holder<T>
        // found:    Holder
        // where T is a type-variable:
        // T extends Object declared in method <T>f1(Holder<T>)
        // 2 warnings

        f2(raw); // No warnings
        Holder rawBasic = new Holder();

        rawBasic.set(new Object());
        // CaptureConversion.java:59: warning: [unchecked] unchecked call to set(T) as a member of the raw type Holder
        // rawBasic.set(new Object());
        //             ^
        // where T is a type-variable:
        // T extends Object declared in class Holder
        // 1 warning

        f2(rawBasic); // No warnings
        // Upcast to Holder<?>, still figures it out:
        Holder<?> wildcarded = new Holder<>(1.0);
        f2(wildcarded);
    }
}
/* Output:
Integer
Integer
Object
Double
 */
