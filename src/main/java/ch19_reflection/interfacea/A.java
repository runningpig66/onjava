package ch19_reflection.interfacea;

/**
 * @author runningpig66
 * @date 2025/12/24 周三
 * @time 21:52
 * P.633 §19.9 接口和类型信息
 * <p>
 * interface 关键字的一个重要目标是允许程序员隔离组件，从而减少耦合。
 * 如果只和接口通信，那么就可以实现这一目标，但是通过类型信息可能会绕过它————接口并不一定保证解耦。假设我们从一个接口开始：
 */
public interface A {
    void f();
}
