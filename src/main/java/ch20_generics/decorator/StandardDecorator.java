package ch20_generics.decorator;

/**
 * @author runningpig66
 * @date 2026/1/15 周四
 * @time 17:02
 * P.728 §20.14 混型 §20.14.3 使用装饰器模式
 * <p>
 * 标准装饰器模式示例
 * 展示如何通过组合与继承，动态地为对象增强功能。
 */
// 1. 定义顶层接口
// 定义系统的基本抽象能力，所有组件（核心组件和装饰器）均需实现此接口。
interface Printer {
    void print();
}

// 2. 定义核心组件
// 基础实现类（被装饰者），提供最原始的核心功能，不包含任何修饰逻辑。
class BasicPrinter implements Printer {
    @Override
    public void print() {
        // 输出核心业务内容
        System.out.print("Hello");
    }
}

// 3. 定义装饰器基类：既是 Printer，又持有 Printer。
// 充当中间层，通过组合持有被装饰对象的引用，并将默认行为委托给该对象。
abstract class PrinterDecorator implements Printer {
    // 持有被装饰组件的引用（可以是核心组件，也可以是其他装饰器）
    protected Printer next;

    public PrinterDecorator(Printer next) {
        this.next = next;
    }

    @Override
    public void print() {
        // 默认行为：将请求直接转发给下一层组件
        next.print();
    }
}

// 4. 定义具体装饰器 (1)
// 在调用原有功能的基础上，增强时间戳输出功能。
class TimeStampedPrinter extends PrinterDecorator {
    public TimeStampedPrinter(Printer next) {
        super(next);
    }

    @Override
    public void print() {
        // 调用原有功能
        super.print();
        // 增强行为：在原有内容后追加时间戳
        System.out.print(" [Time: " + System.currentTimeMillis() + "]");
    }
}

// 4. 定义具体装饰器 (2)
// 在调用原有功能的前后，增强边框格式化功能。
class BorderPrinter extends PrinterDecorator {
    public BorderPrinter(Printer next) {
        super(next);
    }

    @Override
    public void print() {
        // 增强行为：前置处理（添加左边框）
        System.out.print("^ ");
        // 调用原有功能
        super.print();
        // 增强行为：后置处理（添加右边框）
        System.out.print(" $");
    }
}

// 5. 客户端调用示例
// 展示单一组件使用、单层装饰以及多层嵌套装饰的效果。
public class StandardDecorator {
    public static void main(String[] args) {
        // 场景一：仅使用核心组件
        System.out.println("--- 基础调用 ---");
        BasicPrinter p1 = new BasicPrinter();
        p1.print();
        System.out.println();

        // 场景二：单层装饰（核心组件 + 时间戳）
        System.out.println("\n--- 单层装饰 ---");
        Printer p2 = new TimeStampedPrinter(new BasicPrinter());
        p2.print();
        System.out.println();

        // 场景三：多层装饰（核心组件 + 时间戳 + 边框）
        // 通过嵌套组合，同时获得两种增强功能，且调用者无需关注具体层级结构
        System.out.println("\n--- 多层装饰 ---");
        Printer p3 = new BorderPrinter(new TimeStampedPrinter(new BasicPrinter()));
        p3.print();
        System.out.println();
    }
}
/* Output:
--- 基础调用 ---
Hello

--- 单层装饰 ---
Hello [Time: 1768471114588]

--- 多层装饰 ---
^ Hello [Time: 1768471114597] $

 */
