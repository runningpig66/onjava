package ch13_functional;

import java.util.function.Function;

/**
 * @author runningpig66
 * @date 2025/10/11 周六
 * @time 14:32
 * 代码清单 P.364 高阶函数 - 根据所接受的函数生成一个新函数
 */
class I {
    @Override
    public String toString() {
        return "I";
    }
}

class O {
    @Override
    public String toString() {
        return "O";
    }
}

public class TransformFunction {
    static Function<I, O> transform(Function<I, O> in) {
        return in.andThen(o -> {
            System.out.println(o);
            return o;
        });
    }

    public static void main(String[] args) {
        Function<I, O> f2 = transform(i -> {
            System.out.println(i);
            return new O();
        });
        O o = f2.apply(new I());
    }
}
/* Output:
I
O
 */
