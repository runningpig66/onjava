package ch14_streams;

import onjava.Operation;

import java.util.Arrays;

/**
 * @author runningpig66
 * @date 2025/10/28 周二
 * @time 20:25
 * 代码清单 P.386 Arrays 类中包含了名为 stream() 的静态方法，可以将数组转换为流
 */
public class MetalWork2 {
    public static void main(String[] args) {
        Arrays.stream(new Operation[]{
                () -> Operation.show("Heat"),
                () -> Operation.show("Hammer"),
                () -> Operation.show("Twist"),
                () -> Operation.show("Anneal")
        }).forEach(Operation::execute);
    }
}
/* Output:
Heat
Hammer
Twist
Anneal
 */
