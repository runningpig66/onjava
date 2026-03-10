package ch27_lowlevel;

import onjava.TimedAbort;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

/**
 * @author runningpig66
 * @date 3月9日 周一
 * @time 21:45
 * P.287 §6.3 共享资源
 * <p>
 * 任何 IntGenerator 都可以用下面这个 EvenChecker 类进行测试：
 */
public class EvenChecker implements Runnable {
    private IntGenerator generator;
    private final int id;

    public EvenChecker(IntGenerator generator, int id) {
        this.generator = generator;
        this.id = id;
    }

    @Override
    public void run() {
        while (!generator.isCanceled()) {
            int val = generator.next();
            if (val % 2 != 0) {
                System.out.println(val + " not even!");
                generator.cancel(); // Cancels all EvenCheckers
            }
        }
    }

    // Test any IntGenerator:
    public static void test(IntGenerator gp, int count) {
        List<CompletableFuture<Void>> checkers = IntStream.range(0, count)
                .mapToObj(i -> new EvenChecker(gp, i))
                .map(CompletableFuture::runAsync)
                // .collect(Collectors.toList());
                .toList();
        checkers.forEach(CompletableFuture::join);
    }

    // 启动多线程并发测试，并配置四秒的强制超时退出机制。引入 TimedAbort 的原因是为了防止完全正确的生成器导致整个测试程序无限挂起。
    // 在并发测试中，如果传入的生成器存在线程安全问题，工作线程通常会很快读取到奇数，随后触发全局取消操作，所有并发任务正常结束并释放主线程。
    // 然而，如果传入的生成器是完全线程安全的，它将永远只输出偶数。在这种情况下，工作线程永远无法满足发现奇数的退出条件，
    // 从而陷入无限循环，主线程也会因为持续等待这些子任务而永久阻塞。为了避免测试流程因此卡死，TimedAbort 会在后台开始四秒倒计时。
    // 如果程序在四秒内没有因为发现奇数而正常结束，定时器将执行系统级别的退出指令强行终止整个 JVM 进程，并打印未发现奇数的提示，从而保证测试程序能够按时结束。
    // Default value for count:
    public static void test(IntGenerator gp) {
        new TimedAbort(4, "No odd numbers discovered");
        test(gp, 10);
    }
}
