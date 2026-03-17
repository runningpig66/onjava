package ch21_arrays;

import onjava.ArrayShow;

import java.util.SplittableRandom;

/**
 * @author runningpig66
 * @date 3月16日 周一
 * @time 3:13
 * P.755 §21.3 返回数组
 * Returning arrays from methods
 * <p>
 * 而在 Java 中，你可以直接返回一个数组。你永远不需要操心数组的内存管理——只要数组还在使用，它就一直存在，
 * 而垃圾收集器则会在你使用完之后自动将它回收。下面演示如何返回一个 String 类型的数组：
 */
public class IceCreamFlavors {
    private static SplittableRandom rand = new SplittableRandom(47);
    static final String[] FLAVORS = {
            "Chocolate", "Strawberry", "Vanilla Fudge Swirl", "Mint Chip",
            "Mocha Almond Fudge", "Rum Raisin", "Praline Cream", "Mud Pie"
    };

    // 值得注意的是，在 flavorSet() 中随机选择口味时，会保证不会选到已被选过的口味，这是在 do 循环中实现的，其会持续地随机选择口味，
    // 直到选出的口味不在 picked（用于保存已选过的口味）中（也可以使用字符串比较的方式来检查随机选出的口味是否已在 result 中）。
    public static String[] flavorSet(int n) {
        if (n > FLAVORS.length) {
            throw new IllegalArgumentException();
        }
        String[] results = new String[n];
        boolean[] picked = new boolean[FLAVORS.length];
        for (int i = 0; i < n; i++) {
            int t;
            do {
                t = rand.nextInt(FLAVORS.length);
            } while (picked[t]);
            results[i] = FLAVORS[t];
            picked[t] = true;
        }
        return results;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 7; i++) {
            ArrayShow.show(flavorSet(3));
        }
    }
}
/* Output:
[Praline Cream, Mint Chip, Vanilla Fudge Swirl]
[Strawberry, Vanilla Fudge Swirl, Mud Pie]
[Chocolate, Strawberry, Vanilla Fudge Swirl]
[Rum Raisin, Praline Cream, Chocolate]
[Mint Chip, Rum Raisin, Mocha Almond Fudge]
[Mocha Almond Fudge, Mud Pie, Vanilla Fudge Swirl]
[Mocha Almond Fudge, Mud Pie, Mint Chip]
 */
