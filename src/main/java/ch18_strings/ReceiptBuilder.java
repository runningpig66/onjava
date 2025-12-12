package ch18_strings;

import java.util.Formatter;

/**
 * @author runningpig66
 * @date 2025/12/12 周五
 * @time 15:48
 * P.559 §18.5 格式化输出 §18.5.4 格式说明符
 * <p>
 * 如果想要在插入数据时控制间距和对齐方式，你需要更详细的格式说明符。下面是它的通用语法：
 * %[argument_index$][flags][width][.precision]conversion
 * Formatter 格式说明符详解 (语法: %[索引$][标志][宽度][.精度]转换)
 * <p>
 * 1. 【argument_index$】参数索引 (可选)
 * 示例: `%1$s` 表示使用第一个参数。用于重复使用或重新排序参数。
 * 你的代码中未使用，参数按默认顺序对应。
 * <p>
 * 2. 【flags】标志 (可选) - 控制输出外观
 * `-` : 左对齐（默认右对齐）。你的收据中“Item”列使用了此标志。
 * `0` : 用零而非空格填充数字的宽度。
 * `+` : 总是显示数字的正负号。
 * `,` : 对数字使用本地化的千位分隔符。
 * <p>
 * 3. 【width】宽度 (可选) - 字段的最小字符数
 * 用于保证各列对齐。若数据实际长度小于宽度，则用空格（或标志`0`）填充；
 * 若大于宽度，则完整输出，不会截断。你的代码中 `15`, `5`, `10` 均为宽度。
 * <p>
 * 4. 【.precision】精度 (可选) - 对不同类型的最大限制
 * 对字符串(`s`): 最大字符数（超长则从开头截断）。
 * 你的 `%-15.15s` 中精度 `.15` 将 "Jack's Magic Beans" 截断为 "Jack's Magic Be"。
 * 对浮点数(`f`): 小数位数（会四舍五入或补零）。
 * 你的 `%10.2f` 中精度 `.2` 将 5.1 格式化为 "5.10"。
 * 注意: 精度对整数(`d`)无效，使用会抛异常。
 * <p>
 * 5. 【conversion】转换字符 (必需) - 决定参数的格式化类型
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
 */
public class ReceiptBuilder {
    private double total = 0;
    private Formatter f = new Formatter(new StringBuilder());

    public ReceiptBuilder() {
        f.format("%-15s %5s %10s%n", "Item", "Qty", "Price");
        f.format("%-15s %5s %10s%n", "----", "---", "-----");
    }

    public void add(String name, int qty, double price) {
        // 格式字符串分解： "%-15.15s %5d %10.2f%n"
        // %  : 格式说明符的开始
        // -  : 【标志】左对齐（默认为右对齐）
        // 15 : 【宽度】该字段输出的最小字符数为15，不足则用空格填充
        // .15: 【精度】对于字符串`s`，表示最多输出15个字符（超长则从开头截断）
        // s  : 【转换字符】表示参数应被格式化为字符串
        // %5d: 将整数格式化为最小宽度为5、右对齐的十进制整数
        // %10.2f: 将浮点数格式化为最小宽度10、保留2位小数、右对齐的定点数
        f.format("%-15.15s %5d %10.2f%n", name, qty, price);
        total += price * qty;
    }

    public String build() {
        f.format("%-15s %5s %10.2f%n", "Tax", "", total * 0.06);
        f.format("%-15s %5s %10s%n", "", "", "-----");
        f.format("%-15s %5s %10.2f%n", "Total", "", total * 1.06);
        return f.toString();
    }

    public static void main(String[] args) {
        ReceiptBuilder receiptBuilder = new ReceiptBuilder();
        receiptBuilder.add("Jack's Magic Beans", 4, 4.25);
        receiptBuilder.add("Princess Peas", 3, 5.1);
        receiptBuilder.add("Three Bears Porridge", 1, 14.29);
        System.out.println(receiptBuilder.build());
    }
}
/* Output:
Item              Qty      Price    // 标题行，宽度保证了对齐
----              ---      -----
Jack's Magic Be     4       4.25    // `%-15.15s`: 宽度15保证列宽，精度15截断过长名称
Princess Peas       3       5.10    // `%10.2f`: 宽度10保证价格列对齐，精度2确保两位小数
Three Bears Por     1      14.29
Tax                         2.80
                           -----
Total                      49.39
 */
