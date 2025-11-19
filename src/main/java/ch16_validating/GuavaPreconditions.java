package ch16_validating;

import java.util.function.Consumer;

import static com.google.common.base.Preconditions.*;

/**
 * @author runningpig66
 * @date 2025/11/20 周四
 * @time 1:04
 * 代码清单 P.495 前置条件：使用 Guava 里的前置条件
 * Demonstrating Guava Preconditions
 * <p>
 * 在 16.2.1 节“放宽 DbC 的限制”中。我指出前置条件是 DbC 中不应该删除的部分，因为它会检查方法参数的有效性。
 * 这是你无法控制的，因此你确实需要检查它们。由于 Java 默认禁用断言，因此最好还是使用始终验证方法参数的其他库。
 * Google 的 Guava 库包含了一组很好的前置条件测试，这些测试不仅易于使用，而且提供了具有描述性的良好命名。
 * 在下面的示例中，你可以看到所有这些测试的简单用法。库设计者建议静态导人这些前置条件：
 * <p>
 * notes: GuavaPreconditions.md
 * 关于在 Android APP 中的 DbC 和主流库。
 */
public class GuavaPreconditions {
    // 注意我们通过 test() 方法消除了多少重复代码
    static void test(Consumer<String> c, String s) {
        try {
            System.out.println(s);
            c.accept(s);
            System.out.println("Success");
        } catch (Exception e) {
            String type = e.getClass().getSimpleName();
            String msg = e.getMessage();
            System.out.println(type + (msg == null ? "" : ": " + msg));
        }
    }

    public static void main(String[] args) {
        // TODO 为什么 UnaryOperator 可以当作 Consumer 来用？
        // test(Preconditions::checkNotNull, "X");

        test(s -> s = checkNotNull(s), "X"); // 把机器和材料都传过去
        test(s -> s = checkNotNull(s), null);
        test(s -> s = checkNotNull(s, "s was null"), null);
        test(s -> s = checkNotNull(s, "s was null, %s %s", "arg2", "arg3"), null);

        // checkArgument(boolean) 使用布尔表达式来对参数进行更具体的测试，并在失败时抛出 IllegalArgumentException.
        test(s -> checkArgument(s == "Fozzie"), "Fozzie");
        test(s -> checkArgument(s == "Fozzie"), "X");
        test(s -> checkArgument(s == "Fozzie"), null);
        test(s -> checkArgument(s == "Fozzie", "Bear Left!"), null);
        test(s -> checkArgument(s == "Fozzie", "Bear Left! %s Right!", "Frog"), null);

        // checkState() 会测试对象的状态（例如，不变项检查），而不是检查参数，并在失败时抛出 IllegalStateException.
        // checkArgument & checkState 两者都是检查 boolean，但用异常类型区分“是参数错了还是状态错了”。
        test(s -> checkState(s.length() > 6), "Mortimer");
        test(s -> checkState(s.length() > 6), "Mort");
        test(s -> checkState(s.length() > 6), null);

        // public static int checkElementIndex(int index, int size)
        // 保证其第一个参数是一个 List、String 或数组的有效元素索引，这个 List、String 或数组的大小由第二个参数指定。
        // 用于访问元素的下标，比如 list.get(i)
        // 合法范围：0 <= i < size
        // 对长度为 6 的字符串，合法下标是 0..5，i == 6 一定非法
        test(s -> checkElementIndex(6, s.length()), "Robert");
        test(s -> checkElementIndex(6, s.length()), "Bob");
        test(s -> checkElementIndex(6, s.length()), null);

        // public static int checkPositionIndex(int index, int size)
        // 确定它的第一个参数是否在 0 和第二个参数（包括）的范围内。
        // 用于“位置/光标”，表示元素之间的缝，比如插入位置、子区间边界
        // 合法范围：0 <= i <= size   (注意可以等于 size)
        // 对长度为 6 的字符串，合法位置是 0..6，i == 6 表示“最后一个字符之后”的位置，是合法的
        test(s -> checkPositionIndex(6, s.length()), "Robert");
        test(s -> checkPositionIndex(6, s.length()), "Bob");
        test(s -> checkPositionIndex(6, s.length()), null);

        // public static void checkPositionIndexes(int start, int end, int size)
        // 保证 [first_arg, second_arg) 是一个 List、String 或数组的有效子范围，而这个 List、String 或数组的大小是由第三个参数指定的。
        // 检查的是一对合法“位置”索引：0 ≤ start ≤ end ≤ size
        // subList(start, end) 在此基础上使用左闭右开元素区间 [start, end), 其中 end 虽然是 exclusive，但取值仍然可以等于 size。
        test(s -> checkPositionIndexes(0, 6, s.length()), "Hieronymus");
        test(s -> checkPositionIndexes(0, 10, s.length()), "Hieronymus");
        test(s -> checkPositionIndexes(0, 11, s.length()), "Hieronymus");
        test(s -> checkPositionIndexes(-1, 6, s.length()), "Hieronymus");
        test(s -> checkPositionIndexes(7, 6, s.length()), "Hieronymus");
        test(s -> checkPositionIndexes(0, 6, s.length()), null);
    }
}
/* Output:
X
Success
null
NullPointerException
null
NullPointerException: s was null
null
NullPointerException: s was null, arg2 arg3
Fozzie
Success
X
IllegalArgumentException
null
IllegalArgumentException
null
IllegalArgumentException: Bear Left!
null
IllegalArgumentException: Bear Left! Frog Right!
Mortimer
Success
Mort
IllegalStateException
null
NullPointerException: Cannot invoke "String.length()" because "s" is null
Robert
IndexOutOfBoundsException: index (6) must be less than size (6)
Bob
IndexOutOfBoundsException: index (6) must be less than size (3)
null
NullPointerException: Cannot invoke "String.length()" because "s" is null
Robert
Success
Bob
IndexOutOfBoundsException: index (6) must not be greater than size (3)
null
NullPointerException: Cannot invoke "String.length()" because "s" is null
Hieronymus
Success
Hieronymus
Success
Hieronymus
IndexOutOfBoundsException: end index (11) must not be greater than size (10)
Hieronymus
IndexOutOfBoundsException: start index (-1) must not be negative
Hieronymus
IndexOutOfBoundsException: end index (6) must not be less than start index (7)
null
NullPointerException: Cannot invoke "String.length()" because "s" is null
 */
