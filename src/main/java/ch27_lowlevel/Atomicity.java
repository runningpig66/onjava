package ch27_lowlevel;

import onjava.TimedAbort;

import java.util.concurrent.CompletableFuture;

/**
 * @author runningpig66
 * @date 3月11日 周三
 * @time 6:31
 * P.299 §6.5 原子性
 * <p>
 * 现在可以创建测试了，以独立任务的方式启动 run()，然后读取值，检查它是否是偶数：
 * <p>
 * notes: 多线程可见性失效分析：编译期指令重构与物理缓存隔离.md
 */
public class Atomicity {
    public static void test(IntTestable it) {
        new TimedAbort(4, "No failures found");
        CompletableFuture.runAsync(it);
        while (true) {
            int val = it.getAsInt();
            if (val % 2 != 0) {
                System.out.println("failed with: " + val);
                System.exit(0);
            }
        }
    }
}
