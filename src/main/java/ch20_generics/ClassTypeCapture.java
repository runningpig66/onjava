package ch20_generics;

/**
 * @author runningpig66
 * @date 2025/12/29 周一
 * @time 21:42
 * P.676 §20.7 对类型擦除的补偿
 * <p>
 * 由于类型擦除的缘故，我们失去了在泛型代码中执行某些操作的能力。任何需要在运行时知道确切类型的操作都无法运行：
 * <p>
 * 你可以偶尔在编程时绕过这些问题，但是有时你必须通过引入类型标签（type tag）来补偿类型擦除导致的损失。
 * 这意味着要在类型表达式中显式地为你要使用的类型传入一个 Class 对象。
 * <p>
 * 举例来说，在前面的程序中，由于类型信息被擦除了，因此使用 instanceof 的尝试失败了。
 * 而类型标签则可以提供动态的 isInstance() 能力；编译器保证了类型标签能够和泛型参数匹配。
 */
class Building {
}

class House extends Building {
}

public class ClassTypeCapture<T> {
    Class<T> kind;

    public ClassTypeCapture(Class<T> kind) {
        this.kind = kind;
    }

    public boolean f(Object arg) {
        return kind.isInstance(arg);
    }

    public static void main(String[] args) {
        ClassTypeCapture<Building> ctt1 = new ClassTypeCapture<>(Building.class);
        System.out.println(ctt1.f(new Building()));
        System.out.println(ctt1.f(new House()));
        ClassTypeCapture<House> ctt2 = new ClassTypeCapture<>(House.class);
        System.out.println(ctt2.f(new Building()));
        System.out.println(ctt2.f(new House()));
    }
}
/* Output:
true
true
false
true
 */
