package onjava;

/**
 * @author runningpig66
 * @date 2025/12/25 周四
 * @time 15:27
 * P.646 §20.2 简单泛型 §20.2.1 元组库
 * <p>
 * 长度更长的元组则可以用继承的方式创建。添加更多类型的参数很简单：
 */
public class Tuple3<A, B, C> extends Tuple2<A, B> {
    public final C a3;

    public Tuple3(A a, B b, C c) {
        super(a, b);
        a3 = c;
    }

    @Override
    public String rep() {
        return super.rep() + ", " + a3;
    }
}
