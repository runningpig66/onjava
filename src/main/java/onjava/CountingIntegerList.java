package onjava;

import java.util.AbstractList;

/**
 * @author runningpig66
 * @date 2月17日 周二
 * @time 20:26
 * P.113 §3.7 使用享元自定义 Collection 和 Map
 * List of any length, containing sample data
 * {java onjava.CountingIntegerList}
 * <p>
 * 下面定义了一个可以为任意大小的 List，从效果上看，它相当于用 Integer 预先初始化了。
 * 要从 AbstractList 创建一个只读的 List，必须实现 get() 和 size() 两个方法：
 */
public class CountingIntegerList extends AbstractList<Integer> {
    private int size;

    public CountingIntegerList() {
        size = 0;
    }

    // 只有当我们想限制这个 List 的长度时（正如我们在 main() 中所做的那样），size 的值才是重要的。
    // 即使在这种情况下，get() 也可能会产生任意的值。
    public CountingIntegerList(int size) {
        this.size = size < 0 ? 0 : size;
    }

    // 这是享元模式的一个很好的示例。get() 会在我们请求时“计算”值，所以并没有需要存储和初始化的实际底层 List 结构。
    @Override
    public Integer get(int index) {
        return index;
    }

    @Override
    public int size() {
        return size;
    }

    public static void main(String[] args) {
        CountingIntegerList cil = new CountingIntegerList(30);
        System.out.println(cil);
        System.out.println(cil.get(500));
    }
}
/* Output:
[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29]
500
 */
