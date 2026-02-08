package onjava;

import java.util.Random;

/**
 * @author runningpig66
 * @date 2月3日 周二
 * @time 17:55
 * P.010 §1.6 随机选择
 */
public class Enums {
    private static Random rand = new Random(47);

    public static <T extends Enum<T>> T random(Class<T> ec) {
        return random(ec.getEnumConstants());
    }

    public static <T> T random(T[] values) {
        return values[rand.nextInt(values.length)];
    }
}
