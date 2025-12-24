package ch19_reflection;

import onjava.Null;

import java.util.List;

/**
 * @author runningpig66
 * @date 2025/12/23 周二
 * @time 17:10
 * P.630 §19.8 使用 Optional §19.8.1 标签接口
 * <p>
 * 如果你使用的是接口而不是具体类，那么就可以使用 DynamicProxy 来自动生成 Null。
 * 假设有一个 Robot 接口，它定义了名称、模型以及一个描述了自身功能的 List<Operation>：
 */
public interface Robot {
    String name();

    String model();

    List<Operation> operations();

    static void test(Robot r) {
        if (r instanceof Null) {
            System.out.println("[Null Robot]");
        }
        System.out.println("Robot name: " + r.name());
        System.out.println("Robot model: " + r.model());
        for (Operation operation : r.operations()) {
            System.out.println(operation.description.get());
            operation.command.run();
        }
    }
}
