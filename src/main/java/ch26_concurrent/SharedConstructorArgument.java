package ch26_concurrent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author runningpig66
 * @date 3月7日 周六
 * @time 3:20
 * P.266 §5.12 构造器并不是线程安全的
 * <p>
 * 构造器甚至有种更巧妙的办法来共享状态，即通过构造器参数：
 * <p>
 * 构造器参数共享隐患与并发安全性分析：本测试类演示了并发编程中一种隐蔽的故障模式：
 * 通过构造器参数泄露共享的可变状态，从而导致原本看似线程安全的不可变对象在实例化阶段数据被污染。
 * 1. 表面安全的类设计与参数陷阱
 * SharedUser 类将成员变量声明为 private final int id。从类的内部结构来看，这种基于 final 的设计提供了不可变性的保证。
 * 然而，故障的根源在于外部参数的注入：main 方法中实例化了一个全局共享的 Unsafe 对象，并通过 Lambda 表达式传递给了所有并行任务。
 * 这导致所有 SharedUser 对象的初始化逻辑，都强依赖于同一个不受保护的共享数据源。
 * 2. 构造器并非原子操作
 * 在 Java 并发理论中，“对象创建是线程安全的”这一概念，仅指代 JVM 在堆内存中为对象分配物理空间的底层动作。
 * 对象的构造器（对应字节码中的 <init> 实例初始化方法）在执行时，并不具备任何“不可中断”的特权。
 * 构造器内部逻辑的执行完全受制于操作系统的线程调度机制（时间片轮转），随时可能发生线程的上下文切换。
 * 3. 竞态条件与中断推演
 * 当后台子线程并发执行 new SharedUser(unsafe) 时，具体的物理中断与指令重叠过程如下：
 * - 线程 A 首先在堆内存中成功申请到了 SharedUser 的物理内存空间（此阶段是绝对线程安全的）。
 * - 线程 A 开始执行构造器内部的字节码，遇到 sa.get() 调用，指令指针进入 Unsafe 对象的上下文。
 * - 线程 A 准备执行 return i++。它刚将主内存中的 i 值（假设当前为 0）读取到 CPU 寄存器中作为即将返回的值，此时 CPU 时间片耗尽。
 * - 操作系统触发中断，强制挂起线程 A。此时，那个尚未完成构造的 SharedUser 对象被连同线程上下文一起冻结在内存中。
 * - 线程 B 获得时间片切入执行，同样进入构造器逻辑。它从主内存中读取到的 i 值依然是 0，它将 0 赋值给自己的 id，随后顺利完成加 1 操作并将 1 写回主内存。
 * - 线程 A 恢复运行，但它无法感知主内存的变化。由于是后置递增，它会将寄存器中冻结的旧值（0）直接作为 sa.get() 的返回值，
 * 赋值给当前对象的 final 字段 id。随后，它继续在本地寄存器完成 0+1 的计算，并将 1 再次写回主内存（无意中覆盖了线程 B 的写入）。构造器至此宣告结束。
 * 最终导致多个物理内存独立的 SharedUser 对象，在构造结束时被赋予了完全相同的重复 ID（均为 0）。
 * - 结论：对象的内部修饰符（如 final）无法防御通过参数引入的外部状态污染。
 * “对象创建安全”仅仅保证了 JVM 层面对象内存分配的独立性（即“地基”的安全），以及内部封闭数据的初始化安全。
 * 只要构造器的执行逻辑（或调用的外部方法）触碰了不受保护的共享可变状态，那么在争抢该共享资源时引发的所有并发故障
 * （如指令交错、线程中断、更新丢失等竞态条件）就会在对象的初始化阶段爆发，从而破坏并发安全性。
 */
interface SharedArg {
    int get();
}

class Unsafe implements SharedArg {
    private int i = 0;

    @Override
    public int get() {
        return i++;
    }
}

class Safe implements SharedArg {
    private static AtomicInteger counter = new AtomicInteger();

    @Override
    public int get() {
        return counter.getAndIncrement();
    }
}

class SharedUser implements HasID {
    private final int id;

    SharedUser(SharedArg sa) {
        id = sa.get();
    }

    @Override
    public int getID() {
        return id;
    }
}

public class SharedConstructorArgument {
    public static void main(String[] args) {
        Unsafe unsafe = new Unsafe();
        // 语法细节补充：Lambda 表达式的行为传递与延迟执行
        // 在调用 IDChecker.test(() -> new SharedUser(unsafe)) 或传入 StaticIDField::new 时，极易产生“对象在传参时已被预先实例化”的错觉。
        // 实际上，传递给 test 方法的并非一个已创建的 SharedUser 对象引用，而是一个由编译器动态生成的、实现了 Supplier<HasID> 接口的实例。
        // 该实例仅仅封装了对象实例化的逻辑代码（即定义了制造行为，而非制造结果）。
        // 真正的对象实例化动作 (new 操作) 处于延迟执行状态。只有当后台并发流水线（如 Stream.generate）
        // 在子线程内部显式调用该 Supplier 实例的 get() 方法时，构造代码才会在对应的线程栈上下文中被真实触发。
        // 这种“延迟执行”机制使得一条简单的 Lambda 表达式，能够在后台多个子线程中被循环调用，从而并发创建出数十万个物理隔离的 SharedUser 实例。
        // 而正是由于这数十万次并发的延迟构造都绑定了同一个外部的 unsafe 引用，最终导致了初始化阶段的并发资源竞争。
        IDChecker.test(() -> new SharedUser(unsafe));
        Safe safe = new Safe();
        IDChecker.test(() -> new SharedUser(safe));
    }
}
/* Output:
57202
0
 */
