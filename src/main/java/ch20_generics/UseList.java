package ch20_generics;

import java.util.List;

/**
 * @author runningpig66
 * @date 2026/1/8 周四
 * @time 22:56
 * P.712 §20.10 问题 §20.10.4 重载
 * {WillNotCompile}
 * <p>
 * 下面这样的代码是无法编译的，即使看上去很合理：由于类型擦除的缘故，重载该方法会产生相同类型的签名。
 */
public class UseList<W, T> {
    void f(List<T> v) {
    }

    void f(List<W> v) {
    }
    // UseList.java:16: error: name clash: f(List<W>) and f(List<T>) have the same erasure
    // void f(List<W> v) {
    //      ^
    //     where W,T are type-variables:
    //     W extends Object declared in class UseList
    // T extends Object declared in class UseList
    //     1 error
}
