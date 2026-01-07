package ch20_generics;

import java.util.ArrayList;
import java.util.List;

/**
 * @author runningpig66
 * @date 2026/1/1 周四
 * @time 2:21
 * P.691 §20.9 通配符
 * {WillNotCompile}
 * <p>
 * 对数组的这种赋值并不是那么可怕，因为你能够在运行时发现插入了一个不恰当的类型。但是泛型的主要目的之一是要让这样的错误检查提前到编译时。
 * 那么如果试着用泛型集合代替数组，会发生什么呢？
 * <p>
 * notes: 10-类型兼容性：数组协变 vs 泛型不变 vs 通配符.md
 */
public class NonCovariantGenerics {
    // 对于“读”，协变是合理的。对于“写”，协变是危险的。Java 普通泛型（List<Fruit>）的选择是：
    // 为了防止“写”出问题，我宁可牺牲“读”的灵活性。所以我一刀切，禁止 List<Apple> 赋值给 List<Fruit>。
    // Compile Error: incompatible types:
    List<Fruit> flist = new ArrayList<Apple>();
}
