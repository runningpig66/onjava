package ch20_generics;

import onjava.Suppliers;

import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2026-01-27 周二
 * @time 17:53
 * P.740 §20.16 对于缺少（直接的）潜在类型机制的补偿 §20.16.2 将方法应用于序列
 * <p>
 * 大部分时候你应该首要考虑使用 Java 8 的函数式方式。只在某些仅有反射可以处理的特殊需求场景下，才考虑使用反射。
 * 下面重写了 ApplyTest.java，并利用到了 Java 8 的流和函数工具：有了 Java 8，就不再需要 Apply.apply() 了。
 */
public class ApplyFunctional {
    public static void main(String[] args) {
        Stream.of(Stream.generate(Shape::new).limit(2),
                        Stream.generate(Square::new).limit(2))
                // 尽管 Java 缺少函数式语言中常见的 flatten()，但还是可以用 flatMap(c -> c) 起到同样的作用。
                .flatMap(c -> c) // flatten into one stream
                .peek(Shape::rotate)
                .forEach(s -> s.resize(7));

        new FilledList<>(Shape::new, 2).forEach(Shape::rotate);
        new FilledList<>(Square::new, 2).forEach(Shape::rotate);

        SimpleQueue<Shape> shapeQ = Suppliers.fill(new SimpleQueue<>(), SimpleQueue::add, Shape::new, 2);
        Suppliers.fill(shapeQ, SimpleQueue::add, Square::new, 2);
        shapeQ.forEach(Shape::rotate);
    }
}
/* Output:
Shape 0 rotate
Shape 0 resize 7
Shape 1 rotate
Shape 1 resize 7
Square 2 rotate
Square 2 resize 7
Square 3 rotate
Square 3 resize 7
Shape 4 rotate
Shape 5 rotate
Square 6 rotate
Square 7 rotate
Shape 8 rotate
Shape 9 rotate
Square 10 rotate
Square 11 rotate
 */
