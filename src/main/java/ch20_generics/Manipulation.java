package ch20_generics;

/**
 * @author runningpig66
 * @date 2025/12/26 周五
 * @time 18:11
 * P.668 §20.6 类型擦除的奥秘 §20.6.1 C++ 的实现方法
 * {WillNotCompile}
 * <p>
 * 如果继续将示例剩余的部分也用 Java 实现，则会无法编译：
 * <p>
 * 由于类型擦除的缘故，Java 编译器无法将“manipulate() 必须调用 obj 上的 f()”的这个要求，
 * 关联到“HasF 中存在 f() 方法”的这个事实上。要调用 f()，我们就必须帮助泛型类，为它指定边界，
 * 来告诉编译器只接受符合该边界的类型。这里复用了 extends 关键字。有了边界，下面的代码就可以编译了。
 */
class Manipulator<T> {
    private T obj;

    Manipulator(T x) {
        obj = x;
    }

    public void manipulate() {
        // Error: cannot find symbol: method f():
        obj.f();
    }
}

public class Manipulation {
    public static void main(String[] args) {
        HasF hf = new HasF();
        Manipulator<HasF> manipulator = new Manipulator<>(hf);
        manipulator.manipulate();
    }
}
