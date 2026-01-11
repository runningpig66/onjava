package ch20_generics;

/**
 * @author runningpig66
 * @date 2026/1/7 周三
 * @time 19:58
 * P.709 §20.10 问题 §20.10.2 实现参数化接口
 * {WillNotCompile}
 * <p>
 * 一个类无法实现同一个泛型接口的两种变体。由于类型擦除的缘故，这两个变体其实是相同的接口。以下示例便演示了这样的冲突：
 * <p>
 * 因为类型擦除将 Payable<Employee> 和 Payable<Hourly> 擦除为相同的裸类型 Payable，
 * 所以上述代码意味着你会将同一个接口实现两次，因此 Hourly 是无法编译的。
 * 如果你在这两处用到 Payable 的地方将泛型参数移除————如同编译器在类型擦除中所做的一样————代码就可以编译了。
 */
interface Payable<T> {
}

class Employee1 implements Payable<Employee1> {
}

// error: Payable cannot be inherited with different arguments: <Hourly> and <Employee1>
class Hourly extends Employee1 implements Payable<Hourly> {
}
// 在 JVM 字节码视角下，Payable<Employee1> 与 Payable<Hourly> 均被擦除为同一个裸类型 Payable。
// 这意味着 Hourly 类试图在继承链中两次实现同一个 Payable 接口，这是 Java 无法区分的。
// 父类 Employee1 已实现接口，编译器为其生成了桥接方法 pay(Object) { check-cast to Employee1 }。
// 若允许 Hourly 实现另一变体，编译器需生成第二个桥接方法 pay(Object) { check-cast to Hourly }。
// 这将导致 Hourly 类中出现两个方法名和参数完全相同的 pay(Object) 方法，违反了 JVM 的方法唯一性规则。
// 一个类及其整个继承体系中，对于同一个泛型接口，只能绑定一种唯一的类型参数。
