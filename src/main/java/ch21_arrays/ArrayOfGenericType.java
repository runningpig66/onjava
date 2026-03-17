package ch21_arrays;

/**
 * @author runningpig66
 * @date 3月17日 周二
 * @time 17:46
 * P.763 §21.5 数组和泛型
 * <p>
 * 大多数时候，你会发现泛型在类和方法的边界上是有效的；而在内部，类型擦除通常会使泛型变得不可用。因此你会受到一些限制，比如无法创建泛型类型的数组：
 * <p>
 * 此时类型擦除再次介入——以下示例试图创建已被擦除类型（因此是未知类型）的数组。你可以创建 Object 类型的数组，然后进行强制类型转换。
 * 但如果没加上 @SuppressWarnings 注解，由于数组并没有真正持有或动态检查类型 T，因此会得到运行时报警 “unchecked”。
 * 也就是说，如果创建了一个 String[]，Java 会在编译时和运行时都强制要求只能往里放入 String 对象。
 * 然而，如果创建的是 Object[]，那么就可以往里放入除基本类型外的任何对象。
 * <p>
 * notes: ch20_generics/notes/06-泛型擦除的边界：为什么 new T() 和 instanceof T 被禁止？.md
 */
public class ArrayOfGenericType<T> {
    T[] array; // OK

    @SuppressWarnings("unchecked")
    public ArrayOfGenericType(int size) {
        // error: generic array creation
        //        array = new T[size];
        //                ^
        //- array = new T[size];
        array = (T[]) new Object[size];
    }

    /* 尝试在泛型方法中实例化泛型数组并将其暴露给调用端，此模式违反了 Java 泛型数组的安全边界。受限于类型擦除机制，
     * 泛型参数 U 在运行时处于未知状态，因此代码在堆内存中实际分配的是物理类型为 [Ljava.lang.Object; (即 Object[]) 的底层数组。
     * 同时，(U[]) 的强制类型转换在编译后会被完全擦除，导致该方法实际向外传递的仅仅是一个 Object[] 引用。
     * 当这种伪装的泛型数组逸出到外部作用域时，它直接破坏了调用端基于泛型推断产生的静态类型预期，进而必定触发 JVM 在运行时的严格类型校验失败。
     */
    @SuppressWarnings("unchecked")
    public <U> U[] makeArray() {
        // error: generic array creation
        //        return new U[10];
        //               ^
        //- return new U[10];
        return (U[]) new Object[10];
    }

    public static void main(String[] args) {
        ArrayOfGenericType<String> gen = new ArrayOfGenericType<>(10);
        // 编译器会根据泛型参数推断此处需要 String[] 引用，并在字节码层面隐式插入向下转型的 checkcast 指令。
        // 当 JVM 在运行时执行该指令时，会通过运行时类型识别（RTTI）检测到 makeArray() 返回的底层实际对象为 Object[]。
        // 虽然 Java 允许将真正指向子类对象的父类引用向下转型，但此处堆内存中的物理实体就是纯粹的 Object[]，并非真实的 String[]。
        // 由于底层的实际类型与目标引用类型不匹配，强制转换的校验必然失败，从而导致赋值操作立即中断并抛出 ClassCastException。
        //- String[] strings = gen.<String>makeArray();
        // ClassCastException: class [Ljava.lang.Object; cannot be cast to class [Ljava.lang.String;
    }
}
