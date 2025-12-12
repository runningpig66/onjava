package ch18_strings;

import generics.coffee.Coffee;
import generics.coffee.CoffeeSupplier;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/12/12 周五
 * @time 10:51
 * P.553 §18.3 无意识的递归
 * <p>
 * 和其他类一样，Java 的标准集合最终也是从 Object 继承而来的，所以它们也包含了一个 toString() 方法。
 * 这个方法在集合中被重写，这样它生成的结果字符串就能表示容器自身，以及该容器持有的所有对象。
 * 以 ArrayList.toString() 为例，它会遍历 ArrayList 的元素并为每个元素调用 toString() 方法：
 */
public class ArrayListDisplay {
    public static void main(String[] args) {
        List<Coffee> coffees = Stream.generate(new CoffeeSupplier())
                .limit(10)
                .collect(Collectors.toList());
        System.out.println(coffees);
    }
}
/* Output:
[Americano 0, Latte 1, Americano 2, Mocha 3, Mocha 4, Breve 5, Americano 6, Latte 7, Cappuccino 8, Cappuccino 9]
 */
