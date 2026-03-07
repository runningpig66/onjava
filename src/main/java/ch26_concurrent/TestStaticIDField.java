package ch26_concurrent;

/**
 * @author runningpig66
 * @date 3月6日 周五
 * @time 22:57
 * P.266 §5.12 构造器并不是线程安全的
 * <p>
 * 并发环境下构造器引发数据冲突的原因分析：本测试类通过并发执行 StaticIDField::new 证实：在多线程环境中，
 * 如果构造器内部依赖了未受保护的共享状态，将会生成大量状态重复（ID 冲突）的对象。其底层原因主要包含以下三点：
 * 1. 静态共享状态暴露
 * StaticIDField 类中的 counter 变量被 static 修饰，属于类级别的共享变量。无论在堆内存中实例化多少个对象，
 * 所有线程在执行实例变量初始化逻辑（id = counter++）时，操作的都是主内存中的同一个 counter 地址。这构成了典型的共享资源竞争。
 * 2. 非原子操作导致竞态条件 (Race Condition)
 * 自增操作 counter++ 并非原子操作。在字节码和 CPU 指令集层面，它包含三个独立的步骤：
 * 读取（Read）、修改（Modify）、写入（Write）。在多线程并行时，这些指令会发生交错执行。
 * 例如：线程 A 读取 counter 值为 100，在执行加 1 操作前发生上下文切换；此时线程 B 也读取到 100，完成加 1 并写入 101；
 * 随后线程 A 恢复执行，继续将基于旧值计算的 101 写入内存。这种指令交错导致了“更新丢失”，使得两个不同线程创建的对象被赋予了相同的 id。
 * 3. 内存可见性 (Memory Visibility) 缺失
 * counter 变量未被 volatile 关键字修饰，也没有使用同步锁机制。根据 Java 内存模型 (JMM)，
 * 各个运行在不同 CPU 核心上的线程，会优先从其私有的高速缓存（L1/L2 Cache）中读取和修改 counter 的值，而不保证立即同步回主内存。
 * 这导致线程无法及时看到其他线程对 counter 的最新修改，长时间基于缓存中的过期脏数据进行自增，从而在宏观上表现为产生大批量连续的重复 ID。
 * - 结论：这个小节标题叫“构造器并不是线程安全的”，其实是一种警示。在堆内存里分配对象空间的动作本身是安全的，
 * 但如果构造器的内部逻辑（或成员变量的初始化）依赖了不受保护的共享状态（比如这里的 static 变量），那么在并发环境下，生成出来的对象状态就会被彻底污染。
 */
public class TestStaticIDField {
    public static void main(String[] args) {
        // 语法细节补充：方法引用（或 Lambda 表达式）与目标类型推断 (Target Typing)
        // IDChecker.test() 的形参签名严格要求传入 Supplier<HasID>。根据 Java 泛型的不变性 (Invariance) 原则，
        // 若显式声明一个 Supplier<StaticIDField> 对象并尝试传入，将导致编译期类型不兼容错误。
        // 此处直接传递 StaticIDField::new 能够编译通过，得益于 Java 8 的目标类型推断机制：
        // 1. 当方法引用作为参数传递时，它在独立状态下并没有具体的泛型类型。
        // 2. 编译器会根据上下文的目标类型（即形参 Supplier<HasID>），在底层直接合成一个
        // 实现 Supplier<HasID> 接口的匿名/动态类实例。该合成实例精准匹配了形参所需的泛型类型。
        // 3. 在该合成实例的 get() 方法内部，实际执行了 return new StaticIDField()。
        // 由于 StaticIDField 实现了 HasID 接口，这符合常规的面向对象多态（向上转型）规则。
        // 结论：编译器跳过了泛型类型的转换限制，通过直接匹配接口契约并利用返回值多态，“就地合法化”了该方法引用的传递。
        IDChecker.test(StaticIDField::new);
    }
}
/* Output: (Example)
46350
 */
