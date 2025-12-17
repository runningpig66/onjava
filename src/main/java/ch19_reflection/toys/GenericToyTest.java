package ch19_reflection.toys;

/**
 * @author runningpig66
 * @date 2025/12/17 周三
 * @time 20:32
 * P.601 §19.2 Class 对象 §19.2.2 泛型类的引用
 * Testing class Class
 * {java ch19_reflection.toys.GenericToyTest}
 * <p>
 * 对 Class 对象使用泛型语法时，newInstance() 会返回对象的确切类型，而不仅仅是简单的 Object，
 * 就像在 ToyTest.java 示例中看到的那样。但它也会受到一些限制：
 * <p>
 * 如果你得到了基类，那么编译器只允许你声明这个基类引用是“FancyToy的某个基类”，就像表达式 Class<? super FancyToy> 所声明的那样。
 * 它不能被声明为 Class<Toy>。这看起来有点儿奇怪，因为 getSuperclass() 返回了基类（不是接口），
 * 而编译器在编译时就知道这个基类是什么————在这里就是 Toy.class，而不仅仅是“FancyToy的某个基类”。
 * 不管怎么样，因为存在这种模糊性，所以 up.getConstructor().newInstance() 的返回值不是一个确切的类型，而只是一个 Object。
 */
public class GenericToyTest {
    public static void main(String[] args) throws Exception {
        Class<FancyToy> ftc = FancyToy.class;
        // Produces exact type:
        FancyToy fancyToy = ftc.getConstructor().newInstance();

        // 虽然反射 API 源于无泛型时代，但 Java 5 引入泛型后，getSuperclass() 被设计为返回 Class<? super T>，
        // 它直接返回一个表示“T 的某个超类”的 Class 引用，类型信息精确且安全
        // 因此用 Class<? super T> 接收是最完美匹配的写法，无需任何转换或强转
        Class<? super FancyToy> up = ftc.getSuperclass();

        // This won't compile:
        // error: incompatible types: Class<CAP#1> cannot be converted to Class<Toy>
        // Class<Toy> up2 = ftc.getSuperclass();
        // Only produces Object:
        Object obj = up.getConstructor().newInstance();
    }
}
