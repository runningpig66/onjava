package ch20_generics;

import org.jspecify.annotations.NonNull;

/**
 * @author runningpig66
 * @date 2026/1/8 周四
 * @time 23:32
 * P.712 §20.10 问题 §20.10.5 基类会劫持接口
 * <p>
 * 假设你有一个 Pet（宠物）类，并且通过实现 Comparable 接口，实现了和其他 Pet 对象进行比较的能力：
 */
public class ComparablePet implements Comparable<ComparablePet> {
    @Override
    public int compareTo(@NonNull ComparablePet arg) {
        return 0;
    }
}
// 由于泛型擦除，Comparable<ComparablePet> 接口在字节码中仅声明 compareTo(Object) 方法，
// 这与源码中具体定义的 compareTo(ComparablePet) 方法签名不匹配，导致无法在 JVM 层面直接构成重写。
// 为此，编译器自动生成一个桥接方法以适配接口契约（标记为 synthetic bridge），其签名与接口要求一致（compareTo(Object)）。
// 在该桥接方法内部，首先通过 CHECKCAST 指令将传入的 Object 参数安全转换为 ComparablePet 类型，
// 随后转发调用至实际编写的业务方法。这样既满足了接口的字节码契约，又在运行时保持了泛型的类型安全与多态行为。

/* 字节码（结果经过了编辑）
// signature Ljava/lang/Object;Ljava/lang/Comparable<Lch20_generics/ComparablePet;>;
// declaration: ch20_generics/ComparablePet implements java.lang.Comparable<ch20_generics.ComparablePet>
public class ch20_generics/ComparablePet implements java/lang/Comparable {
  //- 业务方法重写：源码中显式定义的逻辑，参数类型精确为 ComparablePet。
  public compareTo(Lch20_generics/ComparablePet;)I
   L0
    ICONST_0
    IRETURN
   L1
    MAXSTACK = 1
    MAXLOCALS = 2

  //- 桥接方法：编译器自动生成 (SYNTHETIC | BRIDGE)，用于适配擦除后的接口方法 compareTo(Object)。
  public synthetic bridge compareTo(Ljava/lang/Object;)I
   L0
    ALOAD 0
    ALOAD 1
    CHECKCAST ch20_generics/ComparablePet
    //- 方法转发：类型检查通过后，调用上述具体的业务方法。
    INVOKEVIRTUAL ch20_generics/ComparablePet.compareTo (Lch20_generics/ComparablePet;)I
    IRETURN
   L1
    MAXSTACK = 2
    MAXLOCALS = 2
}
 */
