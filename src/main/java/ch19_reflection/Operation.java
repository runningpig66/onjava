package ch19_reflection;

import java.util.function.Supplier;

/**
 * @author runningpig66
 * @date 2025/12/23 周二
 * @time 17:15
 * P.631 §19.8 使用 Optional §19.8.1 标签接口
 * <p>
 * Operation 包含一个描述和一个命令 [这是一种命令模式（Command pattern）]。
 * 它们被定义为对函数式接口的引用，这样你就可以将 lambda 表达式或方法引用传递给 Operation 的构造器：
 */
public class Operation {
    public final Supplier<String> description;
    public final Runnable command;

    public Operation(Supplier<String> descr, Runnable cmd) {
        description = descr;
        command = cmd;
    }
}
