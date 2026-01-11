package ch20_generics;

import org.jspecify.annotations.NonNull;

/**
 * @author runningpig66
 * @date 2026/1/8 周四
 * @time 23:43
 * P.713 §20.10 问题 §20.10.5 基类会劫持接口
 * {WillNotCompile}
 * <p>
 * 我们有理由试图将比较类型的范围缩小到 ComparablePet 的子类中。举例来说，Cat（猫）应该只能和其他类型的 Cat 进行比较：
 * 遗憾的是，这行不通。一旦为 Comparable 确定了 ComparablePet 参数，其他的实现类就再也不能和 ComparablePet 之外的对象进行比较了。
 */
// 一旦基类确定了泛型参数，所有子类都必须被迫接受这个参数，无法改变，也无法窄化。这就是“劫持”。参数不能协变（不能变窄）。子类必须能处理父类能处理的一切。
class Cat extends ComparablePet implements Comparable<Cat> {
    @Override
    public int compareTo(@NonNull Cat arg) {
        return 0;
    }
}
// HijackedInterface.java:15: error: Comparable cannot be inherited with different arguments: <Cat> and <ComparablePet>
// class Cat extends ComparablePet implements Comparable<Cat> {
// ^
// HijackedInterface.java:16: error: method does not override or implement a method from a supertype
//     @Override
//     ^
// 2 errors

// "基类劫持"现象的本质，是泛型擦除机制与面向对象多态原则（里氏替换原则 LSP）在字节码层面的不可调和矛盾。
// 父类 ComparablePet 已通过泛型参数确立了“接受任意 ComparablePet 实例”的宽泛契约，
// 并在字节码中生成了包含 CHECKCAST ComparablePet 指令的桥接方法 compareTo(Object)。
// 若允许子类 Cat 实现 Comparable<Cat>，则意味着子类试图将输入参数的合法范围窄化，这在逻辑上破坏了多态安全性：
// 客户端完全有权使用父类引用将一个 Hamster 传给 Cat 实例，若子类强行生成包含 CHECKCAST Cat 的桥接方法，必将导致运行时的 ClassCastException。
// 由于 JVM 无法容忍在一个类中存在两个签名完全相同（均为 compareTo(Object)）但内部强转逻辑互斥（一个转为 Pet，一个转为 Cat）的桥接方法，
// 编译器被迫从源头禁止此类定义，导致一旦父类确定了泛型参数，该参数即被永久锁定，所有子类只能被动继承而无法窄化。

// 父类 ComparablePet 定义了 compareTo(ComparablePet) 方法，这相当于对外确立了一个宽契约：
// 承诺它可以与任何 ComparablePet（包括其子类实例）进行比较。子类 Cat 在继承父类的同时，也必须继承并遵守这一契约。
// 在泛型擦除后，桥接方法 compareTo(Object) 成为强制执行该契约的技术手段：
// 父类桥接方法中硬编码的 CHECKCAST ComparablePet 指令，正是该契约在字节码层面的固化签名。

// 如果允许 Cat 生成自己的桥接方法并执行 CHECKCAST Cat，则意味着子类试图收窄父类方法的参数范围（即参数类型发生协变），
// 这直接违反了面向对象的多态安全原则（里氏替换原则要求参数类型只能逆变或不变）。
// 从实现角度看，父类已通过桥接方法占据了虚方法表（vtable）中 compareTo(Object) 的槽位，并锁定了其类型检查逻辑。
// 子类无法在同一槽位中植入与之冲突的检查逻辑，否则将破坏整个继承体系的方法分发表结构。

// 因此，“基类劫持”本质上是契约继承一致性与字节码实现唯一性共同作用的结果：
// 一旦父类通过泛型参数确立了接口的调用范围，所有子类都必须沿用该范围，无法在编译后生成另一个签名相同但内部类型约束更窄的桥接方法。
