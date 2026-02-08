package ch22_enums;

import static ch22_enums.SpicinessEnum.*;

/**
 * @author runningpig66
 * @date 2月3日 周二
 * @time 12:21
 * P.003 §1.1 枚举类型的基本特性 §静态导入枚举类型
 * {java enums.Burrito2}
 */
public class Burrito2 {
    SpicinessEnum degree;

    public Burrito2(SpicinessEnum degree) {
        this.degree = degree;
    }

    @Override
    public String toString() {
        return "Burrito is " + degree;
    }

    public static void main(String[] args) {
        System.out.println(new Burrito2(NOT));
        System.out.println(new Burrito2(MEDIUM));
        System.out.println(new Burrito2(HOT));
    }
}
/* Output:
Burrito is NOT
Burrito is MEDIUM
Burrito is HOT
 */
