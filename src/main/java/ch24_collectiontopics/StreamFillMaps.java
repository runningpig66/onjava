package ch24_collectiontopics;

import onjava.Count;
import onjava.Pair;
import onjava.Rand;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2月17日 周二
 * @time 3:00
 * P.110 §3.6 填充集合 §3.6.2 使用 Suppliers 来填充 Map [IMP]
 * <p>
 * TODO 在 Java 8+ 中，向函数式接口传递 Lambda、方法引用或匿名内部类时，底层的处理机制有着本质区别。重点研究以下关键字与概念：
 * 1. invokedynamic 指令：Java 7 引入、Java 8 极度依赖的字节码指令，用于推迟方法解析到运行时。
 * 2. LambdaMetafactory 类：JVM 运行时的“代理生成器”，负责动态生成实现了目标函数式接口的轻量级代理类。
 * 3. 捕获型 vs 非捕获型 Lambda：决定了 LambdaMetafactory 是生成一个全局单例对象，还是每次执行都分配新对象（严重影响性能和状态）。
 * 4. 逃逸分析：JVM JIT 编译器的优化技术，研究底层代理对象是如何在年轻代被极速回收，从而抵消 Lambda 对象分配的性能开销的。
 */
class Letters implements Supplier<Pair<Integer, String>> {
    private int number = 1;
    private char letter = 'A';

    @Override
    public Pair<Integer, String> get() {
        return new Pair<>(number++, "" + letter++);
    }
}

public class StreamFillMaps {
    public static void main(String[] args) {
        // 写法 1：基于 invokedynamic 的 Lambda 延迟执行机制。编译器会将此处的 Lambda 表达式解糖为当前类的一个私有方法，
        // 并在运行时通过 invokedynamic 指令和 LambdaMetafactory 动态生成一个 Supplier 接口的轻量级代理类。
        // 由于流水线每次请求元素时都会重复执行该解糖后的方法体代码，即反复触发 new Letters().get()，
        // 这导致 JVM 在堆内存中为流的每一个元素都发生了一次全新的对象分配与销毁。因为包裹状态的对象未能复用，
        // 其内部状态在每次生成后即被强制重置，最终输出的结果仅为无尽重复的初始值。
        Map<Integer, String> collect1 = Stream.generate(() -> new Letters().get()) // IDE: Supplier<? extends Pair<Integer, String>> s
                .limit(1)
                .collect(Collectors.toMap(Pair::key, Pair::value));
        System.out.println(collect1);
        // 写法 2：基于方法句柄绑定的构造器引用。编译器在此处进行了静态类型推断，
        // 将 Letters::new 直接匹配为 Supplier<Letters>（即无参且返回 Letters 实例的函数），
        // 从而悄无声息地将 Stream 的泛型签名从 Stream<Pair> 突变为了 Stream<Letters>。
        // 运行时，LambdaMetafactory 将 Supplier 的 get() 方法绑定到 Letters 类的构造器方法句柄上，
        // 导致流的每次迭代都在实例化全新的 Letters 对象（全局状态丢失）。此外，为了强行适配异变后的数据类型，
        // toMap 中被迫对同一个 letters 实例分别调用了两次 get()（一次取 key，一次取 value）。
        // 这不仅违背了 Pair 作为信使一次性传递键值对的设计初衷，还会导致对象内部状态在单次采集中被消耗两次，引发键值错位（如输出 1=B）的深层逻辑污染。
        Map<Integer, String> collect2 = Stream.generate(Letters::new) // IDE: Supplier<? extends Letters> s
                .limit(1)
                .collect(Collectors.toMap(letters -> letters.get().key, letters -> letters.get().value));
        System.out.println(collect2);
        // 写法 3（标准正解）：基于单例引用的提前分配模式。此写法采用传统的对象实例化机制，new Letters() 表达式在方法入栈前即被计算，
        // JVM 堆内存中严格保证仅分配了一次 Letters 实例。随后传递给 Stream.generate() 的仅仅是该单一对象的内存地址引用。
        // 在整个流水线的拉取计算过程中，底层始终基于这同一个内存地址反复调用其 get() 方法。
        // 因此，该对象的生命周期贯穿了整个流的操作周期，其内部维护的成员变量状态得以被完美保留与累加，最终正确输出状态递增的连续序列。
        Map<Integer, String> m = Stream.generate(new Letters()) // IDE: Supplier<? extends Pair<Integer, String>> s
                .limit(11)
                .collect(Collectors.toMap(Pair::key, Pair::value));
        System.out.println(m);

        // Two separate Suppliers:
        Rand.String rs = new Rand.String(3);
        Count.Character cc = new Count.Character();

        Map<Character, String> mcs = Stream.generate(() -> Pair.make(cc.get(), rs.get()))
                .limit(8)
                .collect(Collectors.toMap(Pair::key, Pair::value));
        System.out.println(mcs);

        // A key Supplier and a single value:
        Map<Character, String> mcs2 = Stream.generate(() -> Pair.make(cc.get(), "Val"))
                .limit(8)
                .collect(Collectors.toMap(Pair::key, Pair::value));
        System.out.println(mcs2);
    }
}
/* Output:
{1=A}
{1=B}
{1=A, 2=B, 3=C, 4=D, 5=E, 6=F, 7=G, 8=H, 9=I, 10=J, 11=K}
{b=btp, c=enp, d=ccu, e=xsz, f=gvg, g=mei, h=nne, i=elo}
{p=Val, q=Val, j=Val, k=Val, l=Val, m=Val, n=Val, o=Val}
 */
