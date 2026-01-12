package ch20_generics;

/**
 * @author runningpig66
 * @date 2026/1/12 周一
 * @time 15:51
 * P.715 §20.11 自限定类型 §20.11.1 奇异递归泛型
 * <p>
 * 可以将 BasicHolder 用于 CRG 中：
 * <p>
 * 此处要注意一个要点：Subtype 这个新类接收参数和返回值的类型都是 Subtype，而不只是基类 BasicHolder。
 * 这便是 CRG 的精髓了：基类用子类替换了其参数。这意味着泛型基类变成了一种为其子类实现通用功能的模板，
 * 但是所实现的功能会将派生类型用于所有的参数和返回值。也就是说，最终类中使用的是具体的类型，而不是基类。
 * 因此在 Subtype 中，set() 的参数和 get() 的返回值类型均为确切的 Subtype。
 */
class Subtype extends BasicHolder<Subtype> {
}

public class CRGWithBasicHolder {
    public static void main(String[] args) {
        Subtype st1 = new Subtype(),
                st2 = new Subtype();
        st1.set(st2);
        Subtype st3 = st1.get();
        st1.f();
    }
}
/* Output:
Subtype
 */
