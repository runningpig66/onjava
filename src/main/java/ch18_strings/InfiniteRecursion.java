package ch18_strings;

import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/12/12 周五
 * @time 11:10
 * P.554 §18.3 无意识的递归
 * Accidental recursion
 * {ThrowsException}
 * {VisuallyInspectOutput} Throws very long exception
 * <p>
 * 如果你希望 toString() 打印对象的内存地址，使用 this 来实现似乎是合情合理的：
 * 如果创建一个 InfiniteRecursion 对象，然后将其打印出来，你会得到一个很长的异常栈。
 */
public class InfiniteRecursion {
    @Override
    public String toString() {
        // 编译器看到一个 String 后面跟着一个 + 和一个不是 String 的东西，它就试图将这个 this 转换为一个 String。
        // 这个转换是通过调用 toString() 来完成的，而这样就产生了一个递归调用。
        // 如果真的想打印对象的地址，可以直接调用 Object 的 toString() 方法来实现。
        // 因此，这里不应该使用 this，而应该使用 super.toString()。
        return "InfiniteRecursion address: " + this;
    }

    public static void main(String[] args) {
        Stream.generate(InfiniteRecursion::new)
                .limit(10)
                .forEach(System.out::println);
    }
}
/* Output:
Exception in thread "main" java.lang.StackOverflowError
	at java.base/java.lang.String.valueOf(String.java:4465)
	at ch18_strings.InfiniteRecursion.toString(InfiniteRecursion.java:24)
	at java.base/java.lang.String.valueOf(String.java:4465)
	at ch18_strings.InfiniteRecursion.toString(InfiniteRecursion.java:24)
	at java.base/java.lang.String.valueOf(String.java:4465)
	at ch18_strings.InfiniteRecursion.toString(InfiniteRecursion.java:24)
	...
 */
