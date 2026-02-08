package ch22_enums;

import onjava.Enums;

/**
 * @author runningpig66
 * @date 2月7日 周六
 * @time 10:42
 * P.036 §1.11 多路分发 §1.11.1 使用枚举类型分发
 * Common tools for RoShamBo examples
 */
public class RoShamBo {
    public static <T extends Competitor<T>> void match(T a, T b) {
        System.out.println(a + " vs. " + b + ": " + a.compete(b));
    }

    public static <T extends Enum<T> & Competitor<T>> void play(Class<T> rsbClass, int size) {
        for (int i = 0; i < size; i++) {
            match(Enums.random(rsbClass), Enums.random(rsbClass));
        }
    }
}
