package ch13_functional;

import java.util.function.Function;

/**
 * @author runningpig66
 * @date 2025/10/11 周六
 * @time 14:26
 * 代码清单 P.363 高阶函数 - 在方法的参数列表中接受（并使用）函数
 */
class One {
}

class Two {
}

public class ConsumeFunction {
    static Two consume(Function<One, Two> onetwo) {
        return onetwo.apply(new One());
    }

    public static void main(String[] args) {
        Two two = consume(one -> new Two());
    }
}
