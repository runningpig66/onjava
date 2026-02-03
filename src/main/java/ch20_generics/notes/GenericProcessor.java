package ch20_generics.notes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
/**
 * @author runningpig66
 * @date 2026-01-24
 * @time 1:56
 * <p>
 * notes: 15-Java泛型类型推断机制的实验记录.md
 */

/**
 * 基础抽象类：几何图形
 */
abstract class Shape {
    public abstract void draw();
}

/**
 * 具体子类：圆形
 */
class Circle extends Shape {
    @Override
    public void draw() {
        System.out.println("Drawing a Circle");
    }
}

/**
 * 具体子类：矩形
 */
class Rectangle extends Shape {
    @Override
    public void draw() {
        System.out.println("Drawing a Rectangle");
    }
}

public class GenericProcessor {
    /**
     * 方法 A：严格类型匹配 (Strict / Invariant) 泛型定义：<T, S extends Iterable<T>>
     * 类型约束分析：
     * 1. 参数 seq 的类型 S 必须是 Iterable<T>。这意味着集合元素的类型必须严格等于 T。
     * 2. 参数 processor 的类型必须是 Consumer<T>。这意味着消费者的处理能力必须严格针对 T。
     * 结论：此签名强制要求“集合元素类型”与“消费者处理类型”完全一致（Invariant）。
     */
    public static <T, S extends Iterable<T>> void applyStrict(S seq, Consumer<T> processor) {
        System.out.println("[Strict Mode] Processing sequence...");
        for (T item : seq) {
            processor.accept(item);
        }
    }

    /**
     * 方法 B：柔性协变匹配 (Flexible / Covariant) 泛型定义：<T, S extends Iterable<? extends T>>
     * 类型约束分析：
     * 1. 参数 seq 的类型 S 必须是 Iterable<? extends T>。这意味着集合中的元素类型可以是 T，也可以是 T 的任意子类型（Covariance，协变）。
     * 2. 参数 processor 的类型必须是 Consumer<T>。这意味着 T 代表了消费者的处理基准类型。
     * 结论：此签名允许解耦。集合提供的是 T 的子类（生产者），而消费者处理的是 T 本身（父类）。
     * 编译器会推断出一个最通用的 T（通常是父类），使得子类集合可以被父类消费者处理。
     */
    public static <T, S extends Iterable<? extends T>> void applyFlexible(S seq, Consumer<T> processor) {
        System.out.println("[Flexible Mode] Processing sequence...");
        for (T item : seq) {
            processor.accept(item);
        }
    }

    public static void main(String[] args) {
        // 1. 数据准备
        // 创建一个具体类型的集合：List<Circle>
        List<Circle> circleList = new ArrayList<>();
        circleList.add(new Circle());
        circleList.add(new Circle());

        // 创建一个通用的消费者：Consumer<Shape> 该消费者可以处理任何 Shape 及其子类
        Consumer<Shape> shapeDrawer = new Consumer<Shape>() {
            @Override
            public void accept(Shape shape) {
                // 通用处理逻辑
                System.out.print("Processor received: ");
                shape.draw();
            }
        };

        // 创建一个专用的消费者：Consumer<Circle>
        Consumer<Circle> circleDrawer = new Consumer<Circle>() {
            @Override
            public void accept(Circle circle) {
                System.out.print("Specific Processor received: ");
                circle.draw();
            }
        };

        System.out.println("====== 实验开始 ======");

        // ---------------------------------------------------------
        // 场景一：同构类型调用 (List<Circle> + Consumer<Circle>)
        // ---------------------------------------------------------
        // 结果：两种方法均可通过编译。
        // 推断：T = Circle。集合提供 Circle，消费者接受 Circle。完全匹配。

        // 未填实参提示：@NotNull Iterable<Object> seq, Consumer<Object> processor
        // 填入实参提示：@NotNull List<Circle> seq, Consumer<Circle> processor
        applyStrict(circleList, circleDrawer);

        // 未填实参提示：@NotNull Iterable<?> seq, Consumer<Object> processor
        // 填入实参提示：@NotNull List<Circle> seq, Consumer<Circle> processor
        applyFlexible(circleList, circleDrawer);

        // ---------------------------------------------------------
        // 场景二：异构类型调用 (List<Circle> + Consumer<Shape>)
        // 结果：仅 applyFlexible 可通过编译。
        // ---------------------------------------------------------

        // 2.1 测试 applyFlexible (推荐写法)
        // 未填实参提示：@NotNull Iterable<?> seq, Consumer<Object> processor
        // 填入实参1提示：@NotNull List<Circle> seq, Consumer<Circle> processor
        // 填入实参2提示：@NotNull List<Circle> seq, Consumer<Shape> processor
        applyFlexible(circleList, shapeDrawer);
        /* * [编译器推断过程解析 - applyFlexible]
         * 方法签名：applyFlexible(S seq, Consumer<T> processor)
         * 约束 1 (来自 seq): S (List<Circle>) matches Iterable<? extends T>
         * => 推导出：Circle <: T (Circle 必须是 T 的子类型)
         * 约束 2 (来自 processor): Consumer<Shape> matches Consumer<T>
         * => 推导出：T = Shape
         * * 综合判断：Circle 是 Shape 的子类型吗？
         * 答案：是。
         * 最终推断结果：T = Shape。
         * 逻辑：允许将子类集合 (Circle) 传递给父类处理逻辑 (Shape)。
         */

        // 2.2 测试 applyStrict (严格写法)
        // 未填实参提示：@NotNull Iterable<Object> seq, Consumer<Object> processor
        // 填入实参1提示：@NotNull List<Circle> seq, Consumer<Circle> processor
        //- applyStrict(circleList, shapeDrawer);
        /* * [编译器推断错误解析 - applyStrict]
         * 方法签名：applyStrict(S seq, Consumer<T> processor)
         * 约束 1 (来自 seq): Iterable<Circle> matches Iterable<T>
         * => 根据 Java 泛型的不变性 (Invariance)，T 必须严格等于 Circle。
         * => 此时锁定 T = Circle。
         * * 约束 2 (来自 processor): 传入了 Consumer<Shape>，但要求 Consumer<T>。
         * => 既然 T 已被锁定为 Circle，方法需要 Consumer<Circle>。
         * => 实际传入的是 Consumer<Shape>。
         * * 冲突：Consumer<Shape> 不是 Consumer<Circle> 的子类型（泛型不变性）。
         * 结果：类型不匹配 (Types are incompatible)。
         */

        System.out.println("====== 实验结束 ======");
    }
}
/* Output:
====== 实验开始 ======
[Strict Mode] Processing sequence...
Specific Processor received: Drawing a Circle
Specific Processor received: Drawing a Circle
[Flexible Mode] Processing sequence...
Specific Processor received: Drawing a Circle
Specific Processor received: Drawing a Circle
[Flexible Mode] Processing sequence...
Processor received: Drawing a Circle
Processor received: Drawing a Circle
====== 实验结束 ======
 */
