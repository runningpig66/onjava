package ch13_functional;

import java.util.function.IntSupplier;

/**
 * @author runningpig66
 * @date 2025/10/12 周日
 * @time 10:04
 * 代码清单 P.36
 * 内部类作为闭包
 */
public class AnonymousClosure {
    IntSupplier makeFun(int x) {
        int i = 0;
        // Same rules apply:
        // Variables are accessed from within inner class, needs to be final or effectively final
        // i++; // Not "effectively final"
        // x++; // Ditto
        return new IntSupplier() {
            @Override
            public int getAsInt() {
                return x + i;
            }
        };
    }
}
