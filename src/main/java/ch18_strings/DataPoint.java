package ch18_strings;

/**
 * @author runningpig66
 * @date 2025/12/13 周六
 * @time 10:59
 * P.565 §18.6 新特性：文本块
 * {NewFeature} Since JDK 15
 * <p>
 * 为了支持文本块，String 类里添加了一个新的 formatted() 方法：
 * formatted() 是一个成员方法，而不是一个像 String.format() 那样的单独的静态函数。
 * 所以除了文本块之外，也可以把它用于普通字符串。它用起来更好、更消晰，因为可以将它直接添加到字符串的后面。
 * public String formatted(Object... args)
 */
public class DataPoint {
    private String location;
    private Double temperature;

    public DataPoint(String loc, Double temp) {
        location = loc;
        temperature = temp;
    }

    @Override
    public String toString() {
        // %.2f 表示仅指定精度为 2 位，宽度不限制
        return """
                Location: %s
                Temperature: %.2f
                """.formatted(location, temperature);
    }

    public static void main(String[] args) {
        DataPoint hill = new DataPoint("Hill", 45.2);
        DataPoint dale = new DataPoint("Dale", 65.2);
        System.out.println(hill);
        System.out.println(dale);
    }
}
/* Output:
Location: Hill
Temperature: 45.20

Location: Dale
Temperature: 65.20

 */
