package ch18_strings;

/**
 * @author runningpig66
 * @date 2025/12/12 周五
 * @time 11:58
 * P.557 §18.5 格式化输出 §18.5.2 System.out.format()
 * <p>
 * Java 5 引入的 format() 方法可用于 PrintStream 或 PrintWriter 对象（可以在进阶卷第 7 章中了解更多信息），
 * 因此也可直接用于 System.out。format() 方法模仿了 C 语言的 printf() 方法。如果你比较怀旧的话，
 * 也可以直接使用 printf() 方法，它用起来很方便。内部直接调用了 format() 来实现。下面是一个简单的例子：
 */
public class SimpleFormat {
    public static void main(String[] args) {
        int x = 5;
        double y = 5.332542;
        // The old way:
        System.out.println("Row 1: [" + x + " " + y + "]");
        // The new way:
        System.out.format("Row 1: [%d %f]%n", x, y);
        // or
        System.out.printf("Row 1: [%d %f]%n", x, y);
    }
}
/* Output:
Row 1: [5 5.332542]
Row 1: [5 5.332542]
Row 1: [5 5.332542]
 */
