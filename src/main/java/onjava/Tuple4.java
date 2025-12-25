package onjava;

/**
 * @author runningpig66
 * @date 2025/12/25 周四
 * @time 15:30
 * P.646 §20.2 简单泛型 §20.2.1 元组库
 * <p>
 * 长度更长的元组则可以用继承的方式创建。添加更多类型的参数很简单：
 */
public class Tuple4<A, B, C, D> extends Tuple3<A, B, C> {
    public final D a4;

    public Tuple4(A a, B b, C c, D d) {
        super(a, b, c);
        a4 = d;
    }

    @Override
    public String rep() {
        return super.rep() + ", " + a4;
    }
}
