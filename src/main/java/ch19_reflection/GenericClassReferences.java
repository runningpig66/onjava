package ch19_reflection;

/**
 * @author runningpig66
 * @date 2025/12/17 周三
 * @time 17:28
 * P.598 §19.2 Class 对象 §19.2.2 泛型类的引用
 * <p>
 * Class 引用指向的是一个 Class 对象，该对象可以生成类的实例，并包含了这些实例所有方法的代码。
 * 它还包含该类的静态字段和静态方法。所以一个 Class 引用表示的就是它所指向的确切类型：Class 类的一个对象。
 * 你可以使用泛型语法来限制 Class 引用的类型。在下面的示例中，这两种语法都是正确的：
 */
public class GenericClassReferences {
    public static void main(String[] args) {
        // intClass 可以重新赋值为任何其他的 Class 对象，例如 double.class，而不会产生警告。
        Class intClass = int.class;
        intClass = double.class;

        // 泛化的类引用 genericIntClass 只能分配给声明的类型。通过使用泛型语法，可以让编译器强制执行额外的类型检查。
        Class<Integer> genericIntClass = int.class;
        System.out.println(genericIntClass);

        genericIntClass = Integer.class; // Same thing
        System.out.println(genericIntClass);
        // error: incompatible types: Class<Double> cannot be converted to Class<Integer>
        // genericIntClass = double.class; // Illegal
    }
}
/* Output:
int
class java.lang.Integer
 */
