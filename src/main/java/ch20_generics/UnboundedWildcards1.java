package ch20_generics;

import java.util.ArrayList;
import java.util.List;

/**
 * @author runningpig66
 * @date 2026/1/2 周五
 * @time 17:38
 * P.696 §20.9 通配符 §20.9.3 无界通配符
 * <p>
 * 无界通配符（unbounded wildcard）<?> 似乎意味着“任何类型”，所以使用无界通配符似乎就等于使用某个原生类型。
 * 确实，编译器一开始似乎允许这样赋值：
 * <p>
 * 本例演示了无界通配符 <?> 的赋值规则。表面上看，<?> 代表“任意类型”，好像和原生类型 List 差不多，但实际上编译器对它们的处理有严格区别：
 * 原生类型（Raw Type）会关闭类型检查。List<?> 是“类型安全的原生类型”，仍然保留了泛型的部分检查。
 * - List<?> 在逻辑上等价于 List<? extends Object>
 * - List<?> 作为原生类型的类型安全替代品，通过限制写入操作来防止堆污染。
 * <p>
 * notes: 12-原生类型赋值给通配符：从失控到受控的转换.md
 */
public class UnboundedWildcards1 {
    // [1] 原生类型 (Raw Type)
    // 放弃了泛型类型检查机制。编译器对其不进行任何类型约束，允许写入任意对象，容易堆污染 & ClassCastException.
    static List list1;
    // [2] 无界通配符 (Unbounded Wildcard)
    // 语义：持有“某种具体但未知”类型的列表。
    // 约束：由于无法在编译期确认底层的具体类型（是 List<String> 还是 List<Integer>？），
    // 编译器强制实施“写入禁止”策略（Write Prohibition），仅允许读取为 Object。它是原生类型的现代、安全替代品。
    static List<?> list2;
    // [3] Object 上界通配符 (Upper Bounded Wildcard)
    // 语义：持有 Object 或其子类的列表。
    // 等价性：由于 Java 中所有引用类型均继承自 Object，在类型系统的协变规则下，它与 List<?> 完全等价。
    static List<? extends Object> list3;

    /**
     * 场景一：输入参数为原生类型 (Raw Type)
     * 此时传入的 list 缺乏泛型类型参数，编译器无法保证其内部元素的类型一致性。由于关闭了类型检查，原生类型可以赋值给任何 List 变体。
     */
    // Warning: Raw use of parameterized class 'List'
    static void assign1(List list) {
        // 原生 = 原生 (OK) 这种操作虽然合法，但属于“类型不安全”的传递。
        list1 = list;
        // 无界通配符<?> = 原生 (OK) 编译器允许赋值且不警告，因为这是安全的（只读），但逻辑上与 list3 = list 相同。
        // 这是一个合法的“窄化”操作（从“失控”到“受控”的关键转换）。虽然源 list 是不安全的（可能已经发生了堆污染），但赋值给 list2 后，
        // 编译器将禁止通过 list2 进行任何后续的写入操作，有效地将不安全的引用“封装”在了一个安全的视图中（止损），从而防止污染进一步扩散。
        // Tip: 当我们写 List<?> safeList = rawList; 时，这个 rawList 以前可能已经被塞了一堆乱七八糟的东西（堆污染已发生）。
        // 编译器管不了过去，但是！ 从这一行代码开始，只要你通过 safeList 这个引用来操作，编译器能担保不会有新的错误类型被塞进去。
        // 如果你想读数据，编译器也只能给你最保守的 Object，以此逼迫你进行类型检查（instanceof）。
        list2 = list;
        // 上界通配符<? extends Object> = 原生 (警告)
        // 试图将一个未检查的原生类型，赋值给一个具有明确泛型约束（虽然上界是 Object）的引用。
        // 虽然在运行时不会立即报错（因为都是 Object），但这违反了泛型的类型安全检查，因此标记为“Unchecked”。
        // Warning: Unchecked assignment: 'List' to 'List<? extends Object>'
        list3 = list;
    }

    /**
     * 场景二：输入参数为无界通配符 (<?>)
     * 此时传入的 list 已经是一个受限的、只读的安全引用。List<?> 本身是类型安全的，只能接受 null 作为元素。
     */
    /* Note: [静态类型视角]：源头（形式参数 List<?> list）是不可写的，为什么赋值给 list1 后就能写了？
    这个问题直击了 Java 泛型系统的静态类型检查（Static Type Checking）本质。简短的回答是：取决于你使用哪个引用（变量）来操作那个对象。*/
    static void assign2(List<?> list) {
        /* [静态类型视角]：在 Java 中，赋值操作不会改变对象本身，但会改变编译器看待这个对象的“视角”（即静态类型）。
        list1 (原生类型)：可以写入（但非常危险，会引发堆污染）。
        当你把受限的 List<?> 赋值给原生 list1 时，泛型系统为了向后兼容，允许了这种“类型退化”。
        编译器不再进行泛型检查。你可以调用 list1.add("Hello")，也可以调用 list1.add(123)。
        这是极度危险的。如果传入的 list 在运行时原本是一个 ArrayList<Integer>，
        而你通过 list1 强行写入了 "Hello"（String），此时堆污染（Heap Pollution）就发生了。
        虽然写入时不报错，但在未来某处尝试以 Integer 读取该元素时，会抛出 ClassCastException。
        Warning: Unchecked call to 'add(E)' as a member of raw type 'java.util.List'
        list1.add(123); */
        // 原生 = <?> (OK) 泛型转原生是为了兼容，丢失了类型信息，这是向后兼容的代价。
        list1 = list;
        /* [静态类型视角]：list2 与参数 list 的类型完全一致。赋值后，编译器依然不知道里面具体是什么类型，所以继续封锁 add() 方法。*/
        // <?> = <?> (OK)
        list2 = list;
        /* [静态类型视角]：list3 和 List<?> 逻辑等价。根据 PECS 原则（Producer Extends），
        它是生产者，只能读，不能写。编译器无法确定它是 List<String> 还是 List<Dog>，所以禁止写入任何非 null 对象。*/
        // <? extends Object> = <?> (OK) 完全等价！
        // 这直接证明了 List<?> 隐式满足 List<? extends Object> 的约束。任何“未知类型”必然属于 Object 的继承体系。
        list3 = list;
    }

    /**
     * 场景三：输入参数为 Object 上界通配符 (<? extends Object>)
     * 这实际上和 List<?> 完全等价（Object 是所有类的上界），赋值规则与 assign2 完全相同。
     */
    static void assign3(List<? extends Object> list) {
        list1 = list; // (OK)
        list2 = list; // (OK) <? extends Object> 是 <?> 的具体表现形式，赋值合法。
        list3 = list; // (OK)
    }

    public static void main(String[] args) {
        // Warning: Raw use of parameterized class 'ArrayList'
        assign1(new ArrayList());
        assign2(new ArrayList());
        assign3(new ArrayList());

        assign1(new ArrayList<>());
        assign2(new ArrayList<>());
        assign3(new ArrayList<>());

        // Both forms are acceptable as List<?>:
        List<?> wildList = new ArrayList();
        wildList = new ArrayList<>();
        assign1(wildList);
        assign2(wildList);
        assign3(wildList);
    }
}
/* Tip:
 * [编译器分级警告]：(javac -Xlint:unchecked UnboundedWildcards1.java)
 * 针对将原生类型 (Raw Type) 赋值给泛型引用，编译器实施了分级警告策略：
 * 1. 静默通过：赋值给 List<?> 或 List<? extends Object> 时，编译器将其视为安全的“只读视图”，出于兼容性考量豁免警告。
 * 2. 触发警告：赋值给具体类型 List<String> 或具体上界 List<? extends Fruit> 时，
 * 因涉及具体类型约束且存在堆污染风险，编译器会强制报 [unchecked] 警告。
 *
 * <Code>
 * List<?> list1 = new ArrayList();
 * List<? extends Object> list2 = new ArrayList();
 * List<? extends Fruit> list3 = new ArrayList();
 *
 * <Warning>
 * UnboundedWildcards1.java:122: warning: [unchecked] unchecked conversion
 *         List<? extends Fruit> list3 = new ArrayList();
 *                                       ^
 *   required: List<? extends Fruit>
 *   found:    ArrayList
 * 1 warning
 */
