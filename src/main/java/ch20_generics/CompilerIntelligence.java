package ch20_generics;

import java.util.Arrays;
import java.util.List;

/**
 * @author runningpig66
 * @date 2026/1/1 周四
 * @time 16:07
 * P.692 §20.9 通配符 §20.9.1 编译器有多聪明？
 * <p>
 * 现在，你可能会以为你被禁止调用任何带有参数的方法，但是看看下面这个示例：
 */
/*
 * 【笔记：编译器的“智慧”与方法签名的决定性作用】
 * 本示例用于打破一个常见的刻板印象：“使用了 <? extends T> 通配符后，所有涉及泛型的方法都无法调用”。
 * 事实并非如此，编译器并不是通过智能分析业务逻辑来判断安全性，而是严格依据【方法签名】进行检查。
 * -----------------------------------------------------------------------------
 * 1. 为什么 contains() 和 indexOf() 可以调用？
 * - 查看 List API 文档可知，这些方法的签名是 `boolean contains(Object o)`。
 * - 它们的参数类型是 Object，而不是泛型参数 E。
 * - 因为不涉及泛型参数 E，通配符 (? extends Fruit) 的边界限制对它们不起作用。
 * - 只要传入的对象是 Object（任何对象都是），编译器就会放行。
 * -----------------------------------------------------------------------------
 * 2. 为什么 add() 不能调用？
 * - add 方法的签名是 `boolean add(E e)`。
 * - 参数类型直接使用了泛型 E。
 * - 当 E 被通配符 ? capture 时，编译器无法确定具体的子类型，为了防止堆污染，必须禁止调用。
 * -----------------------------------------------------------------------------
 * 结论：
 * 编译器的“聪明”其实是一种“死板”的规则执行。它只看 API 设计：
 *
 * 1. 如果参数是 Object（如查询类方法 contains）：
 * 通配符不影响使用，因为 Object 签名不受泛型边界约束。
 * 2. 如果参数是 E（如写入类方法 add）：
 * 通配符会封锁入口。因为编译器无法确认具体的子类类型，禁止写入以保证安全。
 * 3. 如果返回值是 E（如读取类方法 get）：
 * 通配符允许其安全返回。虽然 E 被替换成了 ? extends Fruit（具体子类未知），
 * 但编译器可以确信返回值“至少是 Fruit”（上界已知）。
 * 这符合数据的流向逻辑：作为消费者（写入）时 E 是危险的，但作为生产者（读取）时 E 是安全的。
 *
 * 这也反映了 Java 集合框架从非泛型（Java 1.2）过渡到泛型（Java 5）时的历史遗留设计特性，
 * 以及 PECS（Producer Extends, Consumer Super）原则的底层逻辑。
 */
public class CompilerIntelligence {
    public static void main(String[] args) {
        List<? extends Fruit> flist = Arrays.asList(new Apple());
        Apple a = (Apple) flist.get(0); // No Warning
        flist.contains(new Apple()); // Argument is 'Object'; false
        flist.indexOf(new Apple()); // Argument is 'Object'; -1
    }
}
