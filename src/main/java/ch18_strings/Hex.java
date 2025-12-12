package ch18_strings;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author runningpig66
 * @date 2025/12/12 周五
 * @time 21:02
 * P.562 §18.5 格式化输出 §18.5.6 String.format() 十六进制转储工具
 * <p>
 * 作为 String.format() 的第二个示例，让我们将二进制文件中的字节格式化为十六进制。
 * 下面这个小工具通过使用 String.format()，将一个二进制字节数组按可读的十六进制格式打印出来：
 * <p>
 * 十六进制查看器 - 将任意二进制数据（字节数组）格式化为可读的十六进制转储（Hex Dump）格式。
 * 输出格式模仿常见的十六进制编辑器，每行显示16个字节，并附带偏移量。
 * <p
 * 为了打开和读取二进制文件，我们使用了第 17 章中介绍的另一个实用工具：
 * Files.readAllBytes()。它以 byte 数组的形式返回了整个文件。
 * public static byte[] readAllBytes(Path path) throws IOException
 */
public class Hex {
    /*
     * 将字节数组格式化为十六进制字符串
     * @param data 输入的二进制数据（字节数组）
     * @return 格式化后的十六进制字符串
     */
    public static String format(byte[] data) {
        StringBuilder result = new StringBuilder(); // 用于构建最终字符串
        int n = 0; // 字节计数器，用于计算偏移量和换行
        for (byte b : data) { // 遍历每个字节
            // 1. 每 16 个字节（一行开始）打印偏移地址
            if (n % 16 == 0) {
                // %05X: 5位十六进制数，不足补零（如 00000, 00010）
                result.append(String.format("%05X: ", n));
            }

            // 2. 将当前字节格式化为两位十六进制数（%02X）
            // 一个 byte 是 8 位，范围 -128~127，但这里按无符号处理显示 00~FF
            // 一个关键技巧：更严谨的写法是 String.format("%02X ", b & 0xFF)。
            // 因为 byte 在 Java 中是有符号的（范围-128~127），b & 0xFF 能确保它被当作无符号值处理，
            // 避免负数的符号扩展问题。这是实际开发中的一个好习惯。
            result.append(String.format("%02X ", b));
            n++; // 计数器递增

            // 3. 每 16 个字节后换行（完成一行）
            if (n % 16 == 0) {
                result.append("\n");
            }
        }
        // 仅在最后一行不满 16 个字节时才追加换行，避免多余空行
        if (n % 16 != 0) {
            result.append("\n");
        }
        return result.toString();
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            // Test by displaying this class file:
            System.out.println(format(
                    Files.readAllBytes(Paths.get("build/classes/java/main/ch18_strings/Hex.class"))));
        } else {
            System.out.println(format(Files.readAllBytes(Paths.get(args[0]))));
        }
    }
}
/* Output: (First 6 Lines)
00000: CA FE BA BE 00 00 00 41 00 60 0A 00 02 00 03 07
00010: 00 04 0C 00 05 00 06 01 00 10 6A 61 76 61 2F 6C
00020: 61 6E 67 2F 4F 62 6A 65 63 74 01 00 06 3C 69 6E
00030: 69 74 3E 01 00 03 28 29 56 07 00 08 01 00 17 6A
00040: 61 76 61 2F 6C 61 6E 67 2F 53 74 72 69 6E 67 42
00050: 75 69 6C 64 65 72 0A 00 07 00 03 08 00 0B 01 00
...
 */
