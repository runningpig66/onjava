package onjava;

/**
 * @author runningpig66
 * @date 2025/12/25 周四
 * @time 15:32
 * P.647 §20.2 简单泛型 §20.2.1 元组库
 * <p>
 * 长度更长的元组则可以用继承的方式创建。添加更多类型的参数很简单：
 */
public class Tuple5<A, B, C, D, E> extends Tuple4<A, B, C, D> {
    public final E a5;

    public Tuple5(A a, B b, C c, D d, E e) {
        super(a, b, c, d);
        a5 = e;
    }

    @Override
    public String rep() {
        return super.rep() + ", " + a5;
    }
}
