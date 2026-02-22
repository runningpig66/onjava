package ch24_collectiontopics;

import java.util.BitSet;
import java.util.Random;

/**
 * @author runningpig66
 * @date 2月23日 周一
 * @time 2:28
 * P.158 §3.15 Java 1.0/1.1 的集合类 §3.15.4 BitSet
 * Demonstration of BitSet
 */
public class Bits {
    // 辅助打印方法，用于直观展示 BitSet 内部的位状态。BitSet 的索引机制是从最低位（第 0 位）开始递增的。
    // 本方法按索引从 0 到 size()-1 的顺序进行打印，因此输出的二进制串是“低位在左，高位在右”。
    // 这与日常书写或计算器显示的常规二进制表示（高位在左，低位在右）在视觉上刚好相反。
    // 若要将其转换为实际的数值二进制形式，需将提取出的对应长度的字符串倒序查看。
    public static void printBitSet(BitSet b) {
        System.out.println("bits: " + b);
        StringBuilder bbits = new StringBuilder();
        for (int j = 0; j < b.size(); j++)
            // boolean get(int bitIndex): 获取指定索引位的状态，true 代表该位被置为 1，false 代表为 0。
            bbits.append(b.get(j) ? "1" : "0");
        System.out.println("bit pattern: " + bbits);
    }

    public static void main(String[] args) {
        Random rand = new Random(47);

        // 1. 演示将 byte（8 位）存入 BitSet
        // Take the LSB of nextInt():
        byte bt = (byte) rand.nextInt();
        BitSet bb = new BitSet();
        // 从高位到低位逐个扫描原始数值的二进制位
        for (int i = 7; i >= 0; i--)
            // 构造探测掩码 (1 << i)，通过按位与运算 (&) 提取特定位的值
            if (((1 << i) & bt) != 0)
                bb.set(i); // 若该位为 1，则将 BitSet 中对应的索引位置为 true
            else
                bb.clear(i); // 若该位为 0，则置为 false
        System.out.println("byte value: " + bt);
        printBitSet(bb);

        // 2. 演示将 short（16 位）存入 BitSet
        short st = (short) rand.nextInt();
        BitSet bs = new BitSet();
        for (int i = 15; i >= 0; i--)
            if (((1 << i) & st) != 0)
                bs.set(i);
            else
                bs.clear(i);
        System.out.println("short value: " + st);
        printBitSet(bs);

        // 3. 演示将 int（32 位）存入 BitSet
        int it = rand.nextInt();
        BitSet bi = new BitSet();
        for (int i = 31; i >= 0; i--)
            if (((1 << i) & it) != 0)
                bi.set(i);
            else
                bi.clear(i);
        System.out.println("int value: " + it);
        printBitSet(bi);

        // 4. 演示 BitSet 的自动扩容机制 (动态扩展底层 long[] 数组)
        // BitSet 底层基于 long 数组实现，最小容量通常为 64 位。
        // Test bitsets >= 64 bits:
        BitSet b127 = new BitSet();
        b127.set(127); // 设置超出默认 64 位容量的索引，底层数组会自动扩容
        System.out.println("set bit 127: " + b127);
        BitSet b255 = new BitSet(65); // 显式指定初始容量
        b255.set(255); // 即使设置了初始容量，强行 set 超出范围的索引依然会触发自动扩容
        System.out.println("set bit 255: " + b255);
        BitSet b1023 = new BitSet(512);
        b1023.set(1023);
        b1023.set(1024);
        System.out.println("set bit 1023: " + b1023);
    }
}
/* Output:
byte value: -107
bits: {0, 2, 4, 7}
bit pattern: 1010100100000000000000000000000000000000000000000000000000000000
short value: 1302
bits: {1, 2, 4, 8, 10}
bit pattern: 0110100010100000000000000000000000000000000000000000000000000000
int value: -2014573909
bits: {0, 1, 3, 5, 7, 9, 11, 18, 19, 21, 22, 23, 24, 25, 26, 31}
bit pattern: 1101010101010000001101111110000100000000000000000000000000000000
set bit 127: {127}
set bit 255: {255}
set bit 1023: {1023, 1024}
 */
