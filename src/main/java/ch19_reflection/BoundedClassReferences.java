package ch19_reflection;

/**
 * @author runningpig66
 * @date 2025/12/17 周三
 * @time 18:13
 * P.600 §19.2 Class 对象 §19.2.2 泛型类的引用
 * <p>
 * 如果想创建一个 Class 引用，并将其限制为某个类型或任意子类型，可以将通配符与 extends 关键字组合来创建一个界限（bound）。
 * 因此，与其使用 Class<Number>，不如像下面这样做：
 */
public class BoundedClassReferences {
    public static void main(String[] args) {
        Class<? extends Number> bounded = int.class;
        System.out.println(bounded);

        bounded = double.class;
        System.out.println(bounded);

        bounded = Number.class;
        System.out.println(bounded);
        // Or anything else derived from Number.
    }
}
/* Output:
int
double
class java.lang.Number
 */
