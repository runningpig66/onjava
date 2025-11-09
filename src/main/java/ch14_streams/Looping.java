package ch14_streams;

import static onjava.Repeat.repeat;

/**
 * @author runningpig66
 * @date 2025/10/23 周四
 * @time 22:58
 * 代码清单 P.383 使用 onjava 编写的 repeat() 工具函数取代简单的 for 循环，这样生成的循环可以说更简洁了
 */
public class Looping {
    static void hi() {
        System.out.println("Hi!");
    }

    public static void main(String[] args) {
        repeat(3, () -> System.out.println("Looping!"));
        repeat(2, Looping::hi);
    }
}
/* Output:
Looping!
Looping!
Looping!
Hi!
Hi!
 */
