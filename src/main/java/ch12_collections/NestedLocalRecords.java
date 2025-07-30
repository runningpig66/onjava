package ch12_collections;

/**
 * @author runningpig66
 * @date 2025-07-29
 * @time 上午 0:40
 * {NewFeature} Since JDK 16
 */
public class NestedLocalRecords {
    /*
    嵌套在类中的 record 默认隐含 static 修饰符，这是因为 record 作为独立的数据载体，
    设计上不应依赖外部类的实例状态。静态修饰确保 record 不会持有外部类的引用，
    避免意外访问外部成员，保证其简洁、独立和不可变的特性。
    这与嵌套的 interface、enum 一样，都是默认 static 的。
    Java 规范也明确规定，嵌套 record 隐式为 static，即使显式加 static 也只是冗余。
     */
    record Nested(String s) {
    }

    void method() {
        /*
        但方法内部定义的 record（局部 record）不允许添加 static 修饰符，
        因为 Java 语法规定所有局部类型（包括普通类、接口、record）都不能加 static，
        static 只用于类体内的嵌套类型。在方法作用域下，这些类型本身就不依赖外部类或实例，无需静态修饰。

        局部成员不可以是static的
        static 只能用于类体内的成员（如静态变量、静态方法、静态内部类/接口/record），
        不能用于方法中的局部变量和局部类型（包括局部类、局部接口、局部 record）。
        因为 static 成员属于整个类，可以在没有对象的情况下访问，
        而局部变量和局部类型只在方法执行时才存在，生命周期与类实例和类本身无关，不能用 static 修饰。
         */
        record Local(String s) {
        }
    }
}
