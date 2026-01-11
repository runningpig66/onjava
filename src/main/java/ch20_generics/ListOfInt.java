package ch20_generics;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author runningpig66
 * @date 2026/1/7 周三
 * @time 16:50
 * P.707 §20.10 问题 §20.10.1 基本类型不可作为类型参数
 * Autoboxing compensates for the inability to use primitives in generics
 * <p>
 * 本章前面提到过，Java 泛型的限制之一，是你无法将基本类型作为类型参数。因此你无法创建例如 ArrayList<int> 这样的类型。
 * <p>
 * 解决的办法是使用基本类型的包装类，并结合自动装箱机制。如果你创建一个 ArrayList<Integer>，并使用该集合中的 int 类型元素，
 * 便会发现自动装箱机制会自动处理 int 和 Integer 间的双向转换。因此这基本上就相当于你拥有了 ArrayList<int>。
 */
public class ListOfInt {
    public static void main(String[] args) {
        List<Integer> li = IntStream.range(38, 48)
                .boxed() // Converts ints to Integers
                .collect(Collectors.toList());
        System.out.println(li);
    }
}
/* Output:
[38, 39, 40, 41, 42, 43, 44, 45, 46, 47]
 */
