package ch20_generics;

/**
 * @author runningpig66
 * @date 2025/12/26 周五
 * @time 18:25
 * P.668 §20.6 类型擦除的奥秘 §20.6.1 C++ 的实现方法
 * <p>
 * 由于类型擦除的缘故，Java 编译器无法将“manipulate() 必须调用 obj 上的 f()”的这个要求，
 * 关联到“HasF 中存在 f() 方法”的这个事实上。要调用 f()，我们就必须帮助泛型类，为它指定边界，
 * 来告诉编译器只接受符合该边界的类型。这里复用了 extends 关键字。有了边界，下面的代码就可以编译了：
 * <p>
 * 边界 <T extends HasF> 声明了 T 必须是 HasF 类型或者其子类。如果符合这个条件，就可以安全地调用 obj 上的 f() 方法。
 * <p>
 * 我们说泛型类型参数会被擦除为其第一个边界（你稍后会看到，多重边界也是可以的）。我们也谈到了类型参数的擦除。
 * 编译器实际上会将类型参数替换为其被擦除后的类型，因此在下面的示例中，T 被擦除为 HasF，这就相当于在类结构体中用 HasF 替换了 T。
 */
class Manipulator2<T extends HasF> {
    private T obj;

    Manipulator2(T x) {
        obj = x;
    }

    public void manipulate() {
        obj.f();
    }
}
