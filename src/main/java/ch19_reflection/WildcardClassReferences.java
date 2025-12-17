package ch19_reflection;

/**
 * @author runningpig66
 * @date 2025/12/17 周三
 * @time 17:43
 * P.599 §19.2 Class 对象 §19.2.2 泛型类的引用
 * <p>
 * 要想放松使用泛化的 Class 引用时的限制，请使用通配符 ?, 它是 Java 泛型的一部分，表示“任何事物”。
 * 所以我们可以在上面的例子中为普通的 Class 引用加上通配符，这样就可以产生相同的结果：
 */
public class WildcardClassReferences {
    public static void main(String[] args) {
        Class<?> intClass = int.class; // "int"
        intClass = double.class; // "double"
        intClass = Double.class; // "class java.lang.Double"

        System.out.println(int.class == Integer.class); // false
        System.out.println(int.class == Integer.TYPE); // true
        System.out.println(Integer.class == Integer.TYPE); // false

        // int.class 是基本类型 int 的 Class 对象（名称为 "int"）
        System.out.println(int.class.getName()); // "int"
        // Integer.class 是包装类 Integer 的 Class 对象（名称为 "java.lang.Integer"）
        System.out.println(Integer.class.getName()); // "java.lang.Integer"
    }
}
/* Output:
false
true
false
int
java.lang.Integer
 */
