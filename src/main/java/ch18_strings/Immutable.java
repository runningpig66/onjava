package ch18_strings;

/**
 * @author runningpig66
 * @date 2025/12/11 周四
 * @time 12:39
 * P.547 §18.1 不可变的字符串
 * <p>
 * String 类的对象是不可变的。如果查看它的 JDK 文档你就会发现，该类中每个看起来似乎会修改 String 值的方法，
 * 实际上都创建并返回了一个全新的 String 对象。该对象包含了修改的内容。而原始的 String 则保持不变。
 */
public class Immutable {
    public static String upcase(String s) {
        return s.toUpperCase();
    }

    public static void main(String[] args) {
        String q = "howdy";
        System.out.println(q); // howdy
        String qq = upcase(q);
        System.out.println(qq); // HOWDY
        System.out.println(q); // howdy
    }
}
/* Output:
howdy
HOWDY
howdy
 */
