[TOC]

# 笔记：并发环境下的竞态条件与线程封闭

### 【上篇】并发架构中的状态陷阱：对象共享的工程隐患分析

在并发编程中，评估一段代码是否具备线程安全性，其核心标准在于是否存在不受保护的“可变共享状态（Mutable Shared State）”。本篇笔记以 `IDChecker` 类为例，通过推演代码演进过程中的潜在故障，分析在向多线程传递任务对象时，为何应当谨慎对待对象共享，以及防御性实例化在架构设计中的必要性。

#### 1. 场景引入：`IDChecker.test()` 中的双重实例化问题

在原始的 `IDChecker.test()` 方法中，存在一个值得关注的代码结构：方法接收了一个 `Supplier<HasID> gen` 参数，并在向两个后台子线程分发任务时，为每个线程分别实例化了一个独立的 `MakeObjects` 对象。

```java
public static void test(Supplier<HasID> gen) {
    CompletableFuture<List<Integer>>
            groupA = CompletableFuture.supplyAsync(new MakeObjects(gen)),
            groupB = CompletableFuture.supplyAsync(new MakeObjects(gen));
    // ... 后续合并逻辑
}
```

从代码逻辑上看，两个 `MakeObjects` 实例内部持有的其实是同一个 `gen` 引用。这往往会引发一个架构上的疑问：既然底层核心的 `gen` 对象是共享的，且 `MakeObjects` 在当前上下文中并未包含其他实例变量，为何不直接实例化一个 `MakeObjects` 对象并将其引用同时传递给两个子线程？这种双重实例化的做法是否属于冗余？

#### 2. 故障推演：共享单实例引发的竞态条件

为了验证上述疑问，我们可以引入一个常见的业务迭代场景：假设未来需要统计每个 `MakeObjects` 实例在生成批量数据时所消耗的总时间。开发人员可能会在 `MakeObjects` 类中增加一个 `long` 类型的实例变量 `totalTime`，并在 `get()` 方法中进行耗时累加。

**修改后的 `MakeObjects` 类：**

```java
static class MakeObjects implements Supplier<List<Integer>> {
    private Supplier<HasID> gen;
    public long totalTime = 0; // 新增：用于统计总耗时的实例变量

    MakeObjects(Supplier<HasID> gen) {
        this.gen = gen;
    }

    @Override
    public List<Integer> get() {
        long start = System.currentTimeMillis();
        
        List<Integer> result = Stream.generate(gen)
                .limit(IDChecker.SIZE)
                .map(HasID::getID)
                .collect(Collectors.toList());
                
        long end = System.currentTimeMillis();
        totalTime += (end - start); // 风险点：非原子操作的累加
        
        return result;
    }
}
```

如果此时在 `test` 方法中，违反了为每个线程分配独立任务实例的原则，改为共享同一个 `MakeObjects` 对象：

**采用单实例共享的调用方式（反例）：**

```java
public static void test(Supplier<HasID> gen) {
    // 仅实例化一次
    MakeObjects sharedTask = new MakeObjects(gen); 
    
    // 两个线程共享同一个 sharedTask 实例
    CompletableFuture<List<Integer>>
            groupA = CompletableFuture.supplyAsync(sharedTask), 
            groupB = CompletableFuture.supplyAsync(sharedTask); 
    // ...
}
```

#### 3. 原理解析：更新丢失（Lost Update）的底层机制

在上述单实例共享架构下，一旦程序运行，将不可避免地触发并发故障。其根本原因在于 `totalTime += (end - start);` 并非原子操作。在 CPU 指令层面，该操作包含读取当前值、在寄存器中执行加法、将新值写回主内存三个独立步骤。

当线程 A 和线程 B 同时执行各自的 `get()` 方法时，若两者处理耗时相近（例如各耗时 50 毫秒），极易发生以下指令交错：

1. 线程 A 执行到 `totalTime += 50;` 时，底层发生读取（此时 `totalTime` 为 0）。
2. 几乎在同一时刻，线程 B 也执行到 `totalTime += 50;`，底层也读取到 `totalTime` 为 0。
3. 线程 A 在自己的寄存器中计算 `0 + 50 = 50` 并写入主内存。
4. 线程 B 同样在本地计算 `0 + 50 = 50` 并写入主内存，无意中覆盖了线程 A 的写入结果。

最终，`totalTime` 的实际值为 50 毫秒，而预期的正确逻辑总耗时应为 100 毫秒。在此场景中，由于单实例共享，`totalTime` 演变成了一个不受保护的“可变共享状态”，从而导致了典型的竞态条件（Race Condition）和更新丢失（Lost Update）问题。

#### 4. 架构反思：防御性编程与物理隔离

回顾原作者采用的“双重实例化”方案（为 `groupA` 和 `groupB` 各自 `new MakeObjects(gen)`），其工程价值在于实施了防御性编程。在双重实例化模式下，即使后续维护者在类中加入了类似 `totalTime` 的实例变量，程序依然能够保持绝对的线程安全性。因为线程 A 修改的是实例 1 内存地址上的变量，线程 B 修改的是实例 2 内存地址上的变量。两者的状态在物理堆内存中实现了完全隔离，互不干扰。

**结论**：在构建并发系统时，将包含（或未来可能包含）实例变量的任务对象直接共享给多个线程是一项高风险设计。为每个并发执行流显式分配独立的实例对象，可以建立物理级别的内存隔离机制。这种设计有效切断了任务对象在后续业务迭代中，因无意间被添加可变状态而引发并发故障的技术隐患，是保障系统健壮性的标准工程实践。

### 【下篇】并发架构的进阶博弈：无状态共享与线程封闭

在探讨了包含可变状态的对象在并发共享中所引发的工程隐患后，我们需要进一步分析 `IDChecker.test()` 架构中的另一个核心事实：尽管代码为每个子线程分别实例化了 `MakeObjects` 对象，但这两个对象内部实际引用的 `gen` 参数（即 `GuardedIDField::new` 方法引用）却是同一个实例。本篇笔记将深入解析为何共享 `gen` 对象是安全的，并在此基础上对比“基于原子类的状态共享”与“线程封闭”两种架构设计的底层性能博弈。

#### 1. 核心释疑：无状态对象（Stateless Object）的安全共享机制

在 `IDChecker.test()` 的调用逻辑中，传递给子线程的 `gen` 实例本质上是一个由编译器和 JVM 动态生成的 `Supplier<HasID>` 实现对象。其特殊性在于，该对象是一个绝对的无状态对象。

从物理内存和 JVM 运行机制来看，`gen` 对象内部没有任何实例变量供外界读取或修改。其暴露的 `get()` 方法仅仅包含一条简单的对象创建指令（等效于执行 `return new GuardedIDField();`）。当多个并发子线程同时调用 `gen.get()` 时，方法的执行上下文（局部变量、操作数栈等）是完全分配在各自线程私有的虚拟机栈（JVM Stack）上的。由于指令流在执行过程中不会触碰或修改该对象所在堆内存地址的任何内部状态，因此，无论多少个线程并发调用该对象的实例方法，都不会发生竞态条件。

**结论**：在并发系统设计中，共享“无状态”的对象（如纯粹的函数式接口实例、不可变对象常量）是天然线程安全的。这解释了为何两个并行的流水线可以安全地共用同一个生成器引用。

#### 2. 架构探讨：基于 Atomic 类的单实例共享改造

结合上篇笔记中的统计耗时场景，如果业务架构强制要求只实例化一个 `MakeObjects` 对象，并将其共享给所有子线程以累计全局耗时，我们需要引入更严格的并发保护机制。常规的 `long` 类型累加存在更新丢失的问题，在实际工程中，通常会通过 `java.util.concurrent.atomic` 包中的原子类来进行改造。

**使用 `AtomicLong` 改造后的线程安全代码：**

```java
import java.util.concurrent.atomic.AtomicLong;

static class MakeObjects implements Supplier<List<Integer>> {
    private Supplier<HasID> gen;
    // 使用 AtomicLong 替代基本数据类型 long，确保累加操作的原子性
    public AtomicLong totalTime = new AtomicLong(0); 

    MakeObjects(Supplier<HasID> gen) {
        this.gen = gen;
    }

    @Override
    public List<Integer> get() {
        long start = System.currentTimeMillis();
        
        List<Integer> result = Stream.generate(gen)
                .limit(IDChecker.SIZE)
                .map(HasID::getID)
                .collect(Collectors.toList());
                
        long end = System.currentTimeMillis();
        // 底层采用 CAS 机制进行安全的并发累加
        totalTime.addAndGet(end - start); 
        
        return result;
    }
}
```

在上述改造中，`MakeObjects` 成为一个包含了线程安全可变状态的对象。当在 `test()` 方法中向两个 `CompletableFuture` 传入同一个 `MakeObjects` 实例时，代码是严谨且线程安全的。

#### 3. 终极权衡：CAS 竞争开销与线程封闭（Thread Confinement）

虽然使用 `AtomicLong` 解决了单实例共享带来的正确性问题，但在极高并发的工业级场景中，这种架构依然面临底层硬件级别的性能妥协。

**方案 A：单实例共享 + `AtomicLong` 的性能隐患**
`AtomicLong` 底层依赖于无锁的 CAS（Compare-And-Swap）机制。当多个线程同时尝试对同一内存地址进行 `addAndGet` 操作时，底层的硬件指令会引发激烈的内存总线竞争。更新失败的线程被迫在 `while` 循环中不断自旋重试，产生额外的 CPU 空转开销。此外，多个运行在不同 CPU 核心上的线程频繁修改同一内存地址，将触发底层的缓存一致性协议（如 MESI），导致各核心的高速缓存行（Cache Line）频繁失效。这种内存竞争会在一定程度上拖累程序的整体吞吐量。

**方案 B：原作者的架构选择——线程封闭**
重新审视原作者向两个子线程分别传递新建的 `MakeObjects` 实例的做法。该设计的核心并发思想为**线程封闭（Thread Confinement）**。在这种架构下，如果需要在实例中增加耗时统计，每个线程实际上只是在单线程上下文中操作各自私有的普通 `long` 变量。由于数据被隔离在各自的线程所属对象内，底层完全消除了 CAS 自旋和缓存失效的开销。各子线程能够以真正的无锁并行状态全速执行。待所有 `CompletableFuture` 执行完毕并返回后，主线程只需在 `join()` 之后对收集到的多份局部耗时结果进行一次简单的累加操作（类似于 Map-Reduce 思想）。

**总结**：在并发架构的方案抉择中，利用原子类或锁机制来“保护共享状态”通常是退而求其次的方案。系统设计的最高指导原则应当是：尽可能通过防御性拷贝或线程封闭技术来“避免共享状态”。消除共享即消除了竞争，从而能够在确保绝对线程安全的同时，获得物理硬件层面最大化的执行效率。
