package ch20_generics;

import onjava.Suppliers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author runningpig66
 * @date 2026-01-23
 * @time 22:54
 * P.738 §20.16 对于缺少（直接的）潜在类型机制的补偿 §20.16.2 将方法应用于序列
 * <p>
 * 为了测试 Apply，先创建一个 Shape 类；然后是其子类；通过这两个类，我们可以测试 Apply：
 */
public class ApplyTest {
    public static void main(String[] args) throws NoSuchMethodException {
        List<Shape> shapes = Suppliers.create(ArrayList::new, Shape::new, 3);
        Apply.apply(shapes, Shape.class.getMethod("rotate"));
        Apply.apply(shapes, Shape.class.getMethod("resize", int.class), 7);

        List<Square> squares = Suppliers.create(ArrayList::new, Square::new, 3);
        Apply.apply(squares, Shape.class.getMethod("rotate"));
        Apply.apply(squares, Shape.class.getMethod("resize", int.class), 7);

        Apply.apply(new FilledList<>(Shape::new, 3), Shape.class.getMethod("rotate"));
        Apply.apply(new FilledList<>(Square::new, 3), Shape.class.getMethod("rotate"));

        SimpleQueue<Shape> shapeQ = Suppliers.fill(new SimpleQueue<>(), SimpleQueue::add, Shape::new, 3);
        Suppliers.fill(shapeQ, SimpleQueue::add, Square::new, 3);
        Apply.apply(shapeQ, Shape.class.getMethod("rotate"));
    }
}
/* Output:
Shape 0 rotate
Shape 1 rotate
Shape 2 rotate
Shape 0 resize 7
Shape 1 resize 7
Shape 2 resize 7
Square 3 rotate
Square 4 rotate
Square 5 rotate
Square 3 resize 7
Square 4 resize 7
Square 5 resize 7
Shape 6 rotate
Shape 7 rotate
Shape 8 rotate
Square 9 rotate
Square 10 rotate
Square 11 rotate
Shape 12 rotate
Shape 13 rotate
Shape 14 rotate
Square 15 rotate
Square 16 rotate
Square 17 rotate
 */
