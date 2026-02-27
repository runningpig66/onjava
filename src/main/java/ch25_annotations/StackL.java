package ch25_annotations;

import java.util.LinkedList;

/**
 * @author runningpig66
 * @date 2月27日 周五
 * @time 2:29
 * P.191 §4.4 基于注解的单元测试 §4.4.1 在 @Unit 中使用泛型
 * A stack built on a LinkedList
 * <p>
 * 泛型会带来一个特别的问题，因为你无法“笼统地测试”，而只能对特定的类型参数或参数集合进行测试。
 * 解决这个问题的办法很简单：从泛型类的某个具体版本继承一个测试类。下面的示例简单地实现了一个栈：
 */
public class StackL<T> {
    private LinkedList<T> list = new LinkedList<>();

    public void push(T v) {
        list.addFirst(v);
    }

    public T top() {
        return list.getFirst();
    }

    public T pop() {
        return list.removeFirst();
    }
}
