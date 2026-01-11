package ch20_generics;

import org.jspecify.annotations.NonNull;

/**
 * @author runningpig66
 * @date 2026/1/9 周五
 * @time 0:23
 * P.713 §20.10 问题 §20.10.5 基类会劫持接口
 * <p>
 * 从 Hamster 能看出，可以重复实现 ComparablePet 中的相同接口。只要接口是完全相同的即可，包括参数类型。
 * 不过，这和只是在基类中重写接口（如 Gecko 中所示）没什么区别了。
 */
// 父类 ComparablePet 实现了 Comparable<ComparablePet>，因此有一个桥接方法 compareTo(Object)，它调用自己的业务方法 compareTo(ComparablePet)。
// 子类 Hamster 继承了 ComparablePet，并且也显式声明实现了 Comparable<ComparablePet>（尽管父类已经实现，但这是允许的，只要泛型参数相同）。
// Hamster 的桥接方法虽然覆盖了父类的方法（或者说复用了），但它没有收窄参数范围，没有破坏父类的契约。所以在字节码层面，这被视为合法的重写（Override）。
class Hamster extends ComparablePet implements Comparable<ComparablePet> {
    @Override
    public int compareTo(@NonNull ComparablePet arg) {
        return 0;
    }
}
// 由于泛型擦除，接口 Comparable<ComparablePet> 在字节码层面仅声明了 compareTo(Object) 方法。
// 当子类 Hamster 继承 ComparablePet 并重写其业务方法 compareTo(ComparablePet) 时，
// 必须重新生成对应的桥接方法 compareTo(Object)。这一生成行为确保了多态性的正确实现：
// 新生成的桥接方法会执行与父类相同的类型转换（CHECKCAST ComparablePet），
// 但随后通过 INVOKEVIRTUAL 指令调用 Hamster 自身重写的业务逻辑，
// 从而更新了虚方法表中的分发入口，保证通过接口调用时能正确路由到子类的最新实现。

/* 字节码（结果经过了编辑）
// signature Lch20_generics/ComparablePet;Ljava/lang/Comparable<Lch20_generics/ComparablePet;>;
// declaration: ch20_generics/Hamster extends ch20_generics.ComparablePet implements java.lang.Comparable<ch20_generics.ComparablePet>
class ch20_generics/Hamster extends ch20_generics/ComparablePet implements java/lang/Comparable {
  //- 业务方法重写：覆盖了父类 ComparablePet 的同名方法，实现了针对 Hamster 的具体比较逻辑。
  public compareTo(Lch20_generics/ComparablePet;)I
   L0
    ICONST_0
    IRETURN
   L1
    MAXSTACK = 1
    MAXLOCALS = 2

  //- 桥接方法重写：由于子类显式实现了接口且重写了业务方法，编译器强制重新生成桥接方法以更新虚方法表（vtable）中的入口。
  // SYNTHETIC：该标志表示此方法并非直接来自源代码，而是由编译器为满足语言规范或内部实现需求而自动注入的。
  // BRIDGE：该标志特指这是一个桥接方法，其核心作用是在泛型擦除后，弥合类型签名差异，以确保多态调用与类型安全。
  public synthetic bridge compareTo(Ljava/lang/Object;)I
   L0
    ALOAD 0
    ALOAD 1
    CHECKCAST ch20_generics/ComparablePet
    //- 路由更新：虽泛型参数未变（仍强转为 ComparablePet），但 INVOKEVIRTUAL 指令已更新为指向子类 Hamster 的具体实现。
    INVOKEVIRTUAL ch20_generics/Hamster.compareTo (Lch20_generics/ComparablePet;)I
    IRETURN
   L1
    MAXSTACK = 2
    MAXLOCALS = 2
}
 */

// Or just:

class Gecko extends ComparablePet {
    @Override
    public int compareTo(@NonNull ComparablePet arg) {
        return 0;
    }
}
// Gecko 类并未显式声明实现 Comparable 接口，但由于其重写了父类 ComparablePet 的业务方法 compareTo(ComparablePet)，
// 编译器仍会强制生成对应的桥接方法 compareTo(Object)。这是因为桥接方法的生成逻辑与业务方法的重写行为直接绑定：
// 只要子类提供了自己的具体实现，编译器就必须更新底层用于接口调用的桥接方法，
// 将其内部的 INVOKEVIRTUAL 指令指向子类自身的方法版本，从而确保运行时多态分发能正确路由至最新的业务逻辑。

/* 字节码（结果经过了编辑）
class ch20_generics/Gecko extends ch20_generics/ComparablePet {
  //- 业务方法重写：Gecko 虽未显式重申接口，但覆盖了父类 ComparablePet 的比较逻辑。
  public compareTo(Lch20_generics/ComparablePet;)I
   L0
    ICONST_0
    IRETURN
   L1
    MAXSTACK = 1
    MAXLOCALS = 2

  //- 桥接方法重写：只要子类重写了泛型业务方法，编译器就必须生成此桥接方法，防止父类的旧桥接方法调用到错误的逻辑。
  public synthetic bridge compareTo(Ljava/lang/Object;)I
   L0
    ALOAD 0
    ALOAD 1
    CHECKCAST ch20_generics/ComparablePet
    //- 路由更新：INVOKEVIRTUAL 指令的目标地址被修正为 Gecko.compareTo，确保多态分发命中子类。
    INVOKEVIRTUAL ch20_generics/Gecko.compareTo (Lch20_generics/ComparablePet;)I
    IRETURN
   L1
    MAXSTACK = 2
    MAXLOCALS = 2
}
 */
