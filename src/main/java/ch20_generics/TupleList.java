package ch20_generics;

import onjava.Tuple4;

import java.util.ArrayList;

/**
 * @author runningpig66
 * @date 2025/12/26 周五
 * @time 15:24
 * P.663 §20.5 构建复杂模型
 * Combining generic types to make complex generic types
 * <p>
 * 泛型有个重要的好处，即具有简单且安全地创建复杂模型的能力。举例来说，我们可以很容易地创建一个元组 list：
 * 这样可以生成相当强大的数据结构．而无须太多代码。
 */
public class TupleList<A, B, C, D> extends ArrayList<Tuple4<A, B, C, D>> {
    public static void main(String[] args) {
        TupleList<Vehicle, Amphibian, String, Integer> t1 = new TupleList<>();
        t1.add(TupleTest2.h());
        t1.add(TupleTest2.h());
        t1.forEach(System.out::println);
    }
}
/* Output:
(ch20_generics.Vehicle@1be6f5c3, ch20_generics.Amphibian@6b884d57, hi, 47)
(ch20_generics.Vehicle@7cca494b, ch20_generics.Amphibian@7ba4f24f, hi, 47)
 */
