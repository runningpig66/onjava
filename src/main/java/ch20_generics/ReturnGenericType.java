package ch20_generics;

/**
 * @author runningpig66
 * @date 2025/12/26 周五
 * @time 19:05
 * P.669 §20.6 类型擦除的奥秘 §20.6.1 C++ 的实现方法
 * <p>
 * 这引出了很重要的一点：只有在类型参数比具体类型（及其所有子类）更加“泛型”（泛化）的时候————也就是说，
 * 在希望代码能够跨多个类型运行的时候————泛型才会有所帮助。因此，在有实用价值的泛型代码中，
 * 类型参数和它们的应用通常会比简单的类替换更复杂。不过，你不能因此就轻易地认为 <T extends HasF> 这种形式的用法是有缺陷的。
 * 比如说，如果某个类有一个返回 T 的方法，那么泛型就能起作用，因为泛型可以让该方法返回精确的类型：
 */
class ReturnGenericType<T extends HasF> {
    private T obj;

    ReturnGenericType(T x) {
        obj = x;
    }

    // 此处返回泛型的价值在于调用端：虽然底层擦除为 HasF，但这能让调用者拿到精确的 T 类型，免去显式向下转型。
    public T get() {
        return obj;
    }
}
