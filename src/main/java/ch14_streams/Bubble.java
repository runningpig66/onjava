package ch14_streams;

/**
 * @author runningpig66
 * @date 2025/10/19 周日
 * @time 15:18
 * 代码清单 P.384 下面是 Bubble 类，本章前面的示例中曾用过。注意，它包含了自己的静态生成器方法
 */
public class Bubble {
    public final int i;

    public Bubble(int n) {
        i = n;
    }

    @Override
    public String toString() {
        return "Bubble(" + i + ")";
    }

    private static int count = 0;

    public static Bubble bubbler() {
        return new Bubble(count++);
    }
}
