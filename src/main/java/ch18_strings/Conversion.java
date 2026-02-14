package ch18_strings;

import java.math.BigInteger;
import java.util.Formatter;

/**
 * @author runningpig66
 * @date 2025/12/12 周五
 * @time 19:50
 * P.560 §18.5 格式化输出 §18.5.5 Formatter 转换 [IMP]
 * <p>
 * 以下展示了一些最常见的转换字符。下面的示例显示了这些转换的实际效果：
 * <p>
 * `d` : 整数类型（十进制）
 * `c` : Unicode 字符
 * `b` : Boolean 值
 * `s` : 字符串 (任何对象，调用其 `toString()`)
 * `f` : 定点浮点数（十进制）
 * `e` : 浮点数（科学记数法）
 * `x` : 整数类型（十六进制）
 * `h` : 哈希码（十六进制）
 * `%` : 字面量 “%”
 * `n` : 平台相关的换行符（推荐使用，而非 `\n`）
 * <p>
 * Formatter 转换符兼容性笔记：
 * <p>
 * 1. 【万能转换符 `%s`】:
 * - 可将**任何类型**转换为字符串（调用 `toString()`）。
 * - 是唯一对所有测试类型（char, int, BigInteger, double, Object, boolean）均有效的转换符。
 * <p>
 * 2. 【布尔转换符 `%b`】的“宽容”陷阱:
 * - 对于**非布尔类型**（如 char 'a'、int 121、对象），`%b` 永远输出 `true`。
 * - **仅当**参数为 `false` 或 `null` 时，`%b` 才输出 `false`。
 * - 这意味着 `%b` 并非执行“是否为真值”判断，而是“是否非 false/null”。
 * <p>
 * 3. 【字符 `char` 的特殊性】:
 * - `char` 可被 `%c`（作为字符）和 `%s`（转为字符串）格式化。
 * - 但**不能**直接用 `%d` 输出其 Unicode 码点（代码中 [1] 处）。这是一个易错点，若需码点应显式转型为 `int`。
 * <p>
 * 4. 【数值类型的明确分工】:
 * - 整型家族（int, BigInteger）: 适用 `%d`（十进制）、`%x`（十六进制）。
 * - 浮点型（double）: 适用 `%f`（定点）、`%e`（科学计数法）。
 * - **两者泾渭分明**，互用会导致 `IllegalFormatConversionException`。
 * <p>
 * 5. 【哈希转换符 `%h`】:
 * - 输出对象的 `hashCode()` 的十六进制形式（与 `Integer.toHexString(obj.hashCode())` 相同）。
 * - 对于 `null` 会输出 `null`。
 * - 注意：`%h` 与对象 `toString()` 中的哈希值部分通常一致（如示例中 `h: 16b98e56` 对应 `toString` 的 `@16b98e56`）。
 * <p>
 * 总结：Formatter 的转换符并非“智能类型转换”，而是一套**严格的格式化契约**。它明确了每种转换符所接受的精确类型，
 * 混合类型会导致运行时异常。`%s` 和 `%b` 是唯二的“宽泛”转换符，但其行为（尤其是 `%b`）可能与直觉不符，使用时需格外留意。
 */
public class Conversion {
    public static void main(String[] args) {
        Formatter f = new Formatter(System.out);

        char u = 'a';
        System.out.println("u = 'a'");
        f.format("s: %s%n", u);
        // f.format("d: %d%n", u); // [1] char 无法打印到整数
        f.format("c: %c%n", u);
        f.format("b: %b%n", u);
        // f.format("f: %f%n", u);
        // f.format("e: %e%n", u);
        // f.format("x: %x%n", u);
        f.format("h: %h%n", u);

        int v = 121;
        System.out.println("v = 121");
        f.format("d: %d%n", v);
        f.format("c: %c%n", v);
        f.format("b: %b%n", v);
        f.format("s: %s%n", v);
        // f.format("f: %f%n", v);
        // f.format("e: %e%n", v);
        f.format("x: %x%n", v);
        f.format("h: %h%n", v);

        BigInteger w = new BigInteger("50000000000000");
        System.out.println("w = new BigInteger(\"50000000000000\")");
        f.format("d: %d%n", w);
        // f.format("c: %c%n", w);
        f.format("b: %b%n", w);
        f.format("s: %s%n", w);
        // f.format("f: %f%n", w);
        // f.format("e: %e%n", w);
        f.format("x: %x%n", w);
        f.format("h: %h%n", w);

        double x = 179.543;
        System.out.println("x = 179.543");
        // f.format("d: %d%n", x);
        // f.format("c: %c%n", x);
        f.format("b: %b%n", x);
        f.format("s: %s%n", x);
        f.format("f: %f%n", x);
        f.format("e: %e%n", x);
        // f.format("x: %x%n", x);
        f.format("h: %h%n", x);

        Conversion y = new Conversion();
        System.out.println("y = new Conversion()");
        // f.format("d: %d%n", y);
        // f.format("c: %c%n", y);
        f.format("b: %b%n", y);
        f.format("s: %s%n", y);
        // f.format("f: %f%n", y);
        // f.format("e: %e%n", y);
        // f.format("x: %x%n", y);
        f.format("h: %h%n", y);

        boolean z = false;
        System.out.println("z = false");
        // f.format("d: %d%n", z);
        // f.format("c: %c%n", z);
        f.format("b: %b%n", z);
        f.format("s: %s%n", z);
        // f.format("f: %f%n", z);
        // f.format("e: %e%n", z);
        // f.format("x: %x%n", z);
        f.format("h: %h%n", z);
    }
}
/* Output:
u = 'a'
s: a
c: a
b: true
h: 61
v = 121
d: 121
c: y
b: true
s: 121
x: 79
h: 79
w = new BigInteger("50000000000000")
d: 50000000000000
b: true
s: 50000000000000
x: 2d79883d2000
h: 8842a1a7
x = 179.543
b: true
s: 179.543
f: 179.543000
e: 1.795430e+02
h: 1ef462c
y = new Conversion()
b: true
s: ch18_strings.Conversion@16b98e56
h: 16b98e56
z = false
b: false
s: false
h: 4d5
 */
