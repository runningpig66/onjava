package ch20_generics;

import java.util.HashMap;
import java.util.Map;

/**
 * @author runningpig66
 * @date 2026/1/3 周六
 * @time 15:52
 * P.698 §20.9 通配符 §20.9.3 无界通配符
 * <p>
 * 下面这个示例演示了无界通配符的一种重要用途。在处理多重泛型参数的时候，有时需要在将参数初始化为某种具体类型时，允许其中某个参数可以是任何类型：
 * <p>
 * 不过你可以再次看到，在 Map<?,?> 中全都是无界通配符的情况下，编译器看起来并不会将其和原始类型 Map 区分开。
 * 另外，从 UnboundedWildcards1.java 中可以看出，编译器对 List<?> 和 List<? extends Object> 的处理方式并不相同。
 * <p>
 * 令人疑惑的是，编译器并不总是关心两者间（例如 List 和 List<?>）的区别，因此它们看起来可以是同一类事物。
 * 确实，由于泛型参数会被擦除为其第一个边界类型，List<?> 会看起来等同于 List<Object>，
 * 而 List 也实际上相当于 List<Object>————只是这两种说法都并不完全正确。
 * List 实际上是指“持有任意 Object 类型的原生 List”，而 List<?> 是指“持有某种具体类型的非原生 List”，但我们并不知道是什么类型。
 * <p>
 * notes: 12-原生类型赋值给通配符：从失控到受控的转换.md
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class UnboundedWildcards2 {
    static Map map1;
    // 无界通配符 Map<?, ?>：逻辑上等同于原生 Map，但编译器会将其视为只读的安全视图（禁止写入，防止污染）。
    static Map<?, ?> map2;
    // 半通配模式：部分无界通配符 Map<String, ?>：Key 确定，Value 未知
    // Key (String)：类型明确，支持类型安全的键操作（如 keySet(), get(String)）。
    // Value (?)：类型不确定，读取时退化为 Object，且禁止写入非 null 值。
    static Map<String, ?> map3;

    static void assign1(Map map) {
        map1 = map;
    }

    static void assign2(Map<?, ?> map) {
        map2 = map;
    }

    static void assign3(Map<String, ?> map) {
        map3 = map;
    }

    public static void main(String[] args) {
        assign1(new HashMap());
        // 编译器静默通过：Map<?, ?> 兼容性强，且不涉及具体类型约束，原生类型被视为安全的只读视图。
        assign2(new HashMap());
        // 编译器警告：原生类型缺乏约束，无法保证 Key 必须是 String，存在堆污染风险。
        assign3(new HashMap());
        /*
        > Task :compileJava
        UnboundedWildcards2.java:31: warning: [unchecked] unchecked method invocation:
        method assign3 in class UnboundedWildcards2 is applied to given types
                assign3(new HashMap());
               ^
        required: Map<String,?>
        found:    HashMap
        UnboundedWildcards2.java:31: warning: [unchecked] unchecked conversion
        assign3(new HashMap());
                ^
        required: Map<String,?>
        found:    HashMap
        2 warnings
        */
        assign1(new HashMap<>());
        assign2(new HashMap<>());
        // 菱形语法智能推断：HashMap<>() 根据目标类型自动推断为 HashMap<String, Object>()。
        assign3(new HashMap<>());
    }
}
