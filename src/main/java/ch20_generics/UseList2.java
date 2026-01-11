package ch20_generics;

import java.util.List;

/**
 * @author runningpig66
 * @date 2026/1/8 周四
 * @time 23:15
 * P.712 §20.10 问题 §20.10.4 重载
 * <p>
 * UseList.java 的代码是无法编译的，即使看上去很合理：由于类型擦除的缘故，重载该方法会产生相同类型的签名。
 * 相反，在被擦除的参数无法生成独有的参数列表的情况下，你需要提供各不相同的方法名；幸运的是，这类问题可以被编译器发现。
 */
public class UseList2<W, T> {
    void f1(List<T> v) {
    }

    void f2(List<W> v) {
    }
}
