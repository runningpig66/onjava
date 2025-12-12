package ch18_strings;

import java.io.PrintStream;
import java.util.Formatter;

/**
 * @author runningpig66
 * @date 2025/12/12 周五
 * @time 12:10
 * P.557 §18.5 格式化输出 §18.5.3 Formatter 类
 * <p>
 * Java 中所有的格式化功能都由 java.util 包里的 Formatter 类处理。
 * 你可以将 Formatter 视为一个转换器，将格式化字符串和数据转换为想要的结果。
 * 当创建一个 Formatter 对象时，你可以将信息传递给构造器，来表明希望将结果输出到哪里：
 * <p>
 * 本类中 Formatter 的输出行为取决于其构造参数。当包装 PrintStream（如 System.out）时，format() 会立即输出；
 * 当包装 StringBuilder 时，format() 仅将内容追加至内存缓冲区，直至调用 toString() 才生成完整字符串。
 * 此差异体现了格式化逻辑与输出目标的解耦设计。——前者用于即时反馈（如日志），后者用于灵活构建复杂字符串（如报表）。
 */
public class Turtle {
    private String name;
    private Formatter f;

    public Turtle(String name, Formatter f) {
        this.name = name;
        this.f = f;
    }

    public void move(int x, int y) {
        f.format("%s The Turtle is at (%d,%d)%n", name, x, y);
    }

    public static void main(String[] args) {
        PrintStream outAlias = System.out;
        Turtle tommy = new Turtle("Tommy", new Formatter(System.out));
        Turtle terry = new Turtle("Terry", new Formatter(outAlias));
        tommy.move(0, 0);
        terry.move(4, 8);
        tommy.move(3, 4);
        terry.move(2, 5);
        tommy.move(3, 3);
        terry.move(3, 3);
    }
}
/* Output:
Tommy The Turtle is at (0,0)
Terry The Turtle is at (4,8)
Tommy The Turtle is at (3,4)
Terry The Turtle is at (2,5)
Tommy The Turtle is at (3,3)
Terry The Turtle is at (3,3)
 */
