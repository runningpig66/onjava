package ch24_equalshashcode;

import java.util.Random;

/**
 * @author runningpig66
 * @date 2月12日 周四
 * @time 17:54
 * P.460 §C.2 哈希和哈希码
 * Predicting the weather
 */
public class Prediction {
    private static Random rand = new Random(47);

    @Override
    public String toString() {
        return rand.nextBoolean() ? "Six more weeks of Winter!" : "Early Spring!";
    }
}
