package ch14_streams;

import static ch14_streams.RandInts.rands;

/**
 * @author runningpig66
 * @date 2025/11/10 周一
 * @time 4:46
 * 代码清单 P.412 终结操作：选择一个元素
 * <p>
 * 返回一个包含流中第一个元素的 Optional, 如果流中没有元素，则返回 Optional.empty.
 * OptionalInt findFirst();
 * <p>
 * 返回一个包含流中某个元素的 Optional, 如果流中没有元素，则返回 Optional.empty.
 * OptionalInt findAny();
 */
public class SelectElement {
    public static void main(String[] args) {
        System.out.println(rands().findFirst().getAsInt());
        System.out.println(rands().parallel().findFirst().getAsInt());
        System.out.println(rands().findAny().getAsInt());
        System.out.println(rands().parallel().findAny().getAsInt());
    }
}
/* Output:
258
258
258
242
 */
