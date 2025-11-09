package ch14_streams;

/**
 * @author runningpig66
 * @date 2025/11/10 周一
 * @time 5:24
 * 代码清单 P.41 终结操作：获得流相关的信息
 * <p>
 * 获得流中元素的数量
 * long count();
 * <p>
 * 通过 Comparator 确定这个中的“最大”元素
 * Optional<T> min(Comparator<? super T> comparator);
 * <p>
 * 通过 Comparator 确定这个流中的“最小”元素
 * Optional<T> max(Comparator<? super T> comparator)
 */
public class Informational {
    public static void main(String[] args) throws Exception {
        final String filePath = "src/main/java/ch14_streams/Cheese.dat";
        System.out.println(FileToWords.stream(filePath).count());
        System.out.println(FileToWords.stream(filePath)
                .min(String.CASE_INSENSITIVE_ORDER)
                .orElse("NONE"));
        System.out.println(FileToWords.stream(filePath)
                .max(String.CASE_INSENSITIVE_ORDER)
                .orElse("NONE"));
    }
}
/* Output:
32
a
you
 */
