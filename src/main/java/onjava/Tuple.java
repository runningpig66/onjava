package onjava;

/**
 * @author runningpig66
 * @date 2025/12/25 周四
 * @time 22:57
 * P.657 §20.4 泛型方法 §20.4.3 简化元组的使用
 * Tuple library using type argument inference
 * <p>
 * 有了类型参数推断和静态导入，我们便可以将本章前述的元组重写为一个更加通用的库。下面我们用一个重载的静态方法来创建元组：
 */
public class Tuple {
    public static <A, B> Tuple2<A, B> tuple(A a, B b) {
        return new Tuple2<>(a, b);
    }

    public static <A, B, C> Tuple3<A, B, C> tuple(A a, B b, C c) {
        return new Tuple3<>(a, b, c);
    }

    public static <A, B, C, D> Tuple4<A, B, C, D> tuple(A a, B b, C c, D d) {
        return new Tuple4<>(a, b, c, d);
    }

    public static <A, B, C, D, E> Tuple5<A, B, C, D, E> tuple(A a, B b, C c, D d, E e) {
        return new Tuple5<>(a, b, c, d, e);
    }
}
