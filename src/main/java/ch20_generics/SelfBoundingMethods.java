package ch20_generics;

/**
 * @author runningpig66
 * @date 2026/1/12 周一
 * @time 20:25
 * P.717 §20.11 自限定类型 §20.11.2 自限定
 * <p>
 * 也可以将自限定用于泛型方法：这使得该方法无法应用于除所示形式的自限定参数外的任何对象。
 */
public class SelfBoundingMethods {
    static <T extends SelfBounded<T>> T f(T arg) {
        return arg.set(arg).get();
    }

    public static void main(String[] args) {
        A a = f(new A());
    }
}
