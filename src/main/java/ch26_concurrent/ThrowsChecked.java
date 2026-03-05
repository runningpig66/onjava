package ch26_concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 3月5日 周四
 * @time 22:24
 * P.258 §5.10 CompletableFuture §5.10.5 异常 §2.检查型异常
 * <p>
 * CompletableFuture 和并行 Stream 都不支持包含检查型异常的操作。
 * 因此，你必须在调用操作的时候处理检查型异常，而这会明显降低代码的优雅性：
 * <p>
 * 如果你想像使用 nochecked() 的引用一样，使用 withchecked() 的方法引用，编译器就会在 [1] 和 [2] 处报错。
 * 所以，你必须写出 lambda 表达式（或者写一个不会抛出异常的包装方法）。
 */
public class ThrowsChecked {
    class Checked extends Exception {
    }

    static ThrowsChecked nochecked(ThrowsChecked tc) {
        return tc;
    }

    static ThrowsChecked withchecked(ThrowsChecked tc) throws Checked {
        return tc;
    }

    static void testStream() {
        Stream.of(new ThrowsChecked())
                .map(ThrowsChecked::nochecked)
                // .map(ThrowsChecked::withchecked) // [1]
                .map(tc -> {
                    try {
                        return withchecked(tc);
                    } catch (Checked e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    static void testCompletableFuture() {
        CompletableFuture.completedFuture(new ThrowsChecked())
                .thenApply(ThrowsChecked::nochecked)
                // .thenApply(ThrowsChecked::withchecked); // [2]
                .thenApply(tc -> {
                    try {
                        return withchecked(tc);
                    } catch (Checked e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
