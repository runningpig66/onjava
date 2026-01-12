package ch20_generics;

/**
 * @author runningpig66
 * @date 2026/1/12 周一
 * @time 16:56
 * P.715 §20.11 自限定类型 §20.11.2 自限定
 * <p>
 * BasicHolder 可以将任何类型作为其泛型参数，如下所示：
 */
class Other {
}

class BasicOther extends BasicHolder<Other> {
}

public class Unconstrained {
    public static void main(String[] args) {
        BasicOther b = new BasicOther();
        BasicOther b2 = new BasicOther();
        b.set(new Other());
        Other other = b.get();
        b.f();
    }
}
/* Output:
Other
 */
