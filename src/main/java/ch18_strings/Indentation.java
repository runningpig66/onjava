package ch18_strings;

/**
 * @author runningpig66
 * @date 2025/12/13 周六
 * @time 10:46
 * P.564 §18.6 新特性：文本块
 * {NewFeature} Since JDK 15
 * <p>
 * 注意开头的 """ 后面的换行符会被自动去掉，块中的公用缩进也会被去掉，所以 NONE 的结果没有缩进。
 * 如果想要保留缩进，那就移动最后的 """ 来产生所需的缩进，如下所示：
 */
public class Indentation {
    public static final String NONE = """
            XXX
            YYY
            """; // No indentation
    public static final String TWO = """
              XXX
              YYY
            """;   // Produces indent of 2
    public static final String EIGHT = """
                    XXX
                    YYY
            """;         // Produces indent of 8

    public static void main(String[] args) {
        System.out.println(NONE);
        System.out.println(TWO);
        System.out.println(EIGHT);
    }
}
/* Output:
XXX
YYY

  XXX
  YYY

        XXX
        YYY

 */
