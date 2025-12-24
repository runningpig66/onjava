package ch19_reflection.packageaccess;

import ch19_reflection.interfacea.A;

/**
 * @author runningpig66
 * @date 2025/12/24 周三
 * @time 22:27
 * P.635 §19.9 接口和类型信息
 * <p>
 * 一种解决方案是直接声明，如果程序员决定使用实际的类而不是接口，他们就得自己承担后果。
 * 在许多情况下这可能是合理的，但如果事实并非如此，你就需要实施更严格的控制。
 * <p>
 * 最简单的方法是使用包访问权限来实现，这样包外的客户就看不到它了：类 HiddenC 是这个包唯一的 public 部分，
 * 调用它时会生成一个 A 接口。即使 makeA() 返回了一个 C 类型，在包外仍然不能使用除 A 外的任何事物，因为你不能在包外命名 C。
 */
class C implements A {

    @Override
    public void f() {
        System.out.println("public C.f()");
    }

    public void g() {
        System.out.println("public C.g()");
    }

    void u() {
        System.out.println("package C.u()");
    }

    protected void v() {
        System.out.println("protected C.v()");
    }

    private void w() {
        System.out.println("private C.w()");
    }
}

public class HiddenC {
    public static A makeA() {
        return new C();
    }
}
