package ch23_references;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author runningpig66
 * @date 2月11日 周三
 * @time 22:08
 * P.087 §2.4 不可变类
 * A changeable wrapper class
 * <p>
 * 如果你确实需要一个类来持有一个可以被修改的基本类型，就必须自己创建。幸好这很简单：
 */
class IntValue {
    private int n;

    IntValue(int x) {
        n = x;
    }

    public int getValue() {
        return n;
    }

    public void setValue(int n) {
        this.n = n;
    }

    public void increment() {
        n++;
    }

    @Override
    public String toString() {
        return Integer.toString(n);
    }
}

public class MutableInteger {
    public static void main(String[] args) {
        List<IntValue> v = IntStream.range(0, 10)
                .mapToObj(IntValue::new)
                .collect(Collectors.toList());
        System.out.println(v);
        v.forEach(IntValue::increment);
        System.out.println(v);
    }
}
/* Output:
[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
 */
