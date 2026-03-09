package ch27_lowlevel;

import onjava.Nap;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author runningpig66
 * @date 3月8日 周日
 * @time 4:49
 * P.280 §6.1 什么是线程？ §6.1.2 我可以创建多少线程
 * {ExcludeFromGradle} Takes a long time or hangs
 * {java -Xss88K -Xmx32M ch27_lowlevel.ThreadSize}
 * <p>
 * JVM 运行期内存分配标志 (VM Options):
 * -Xss<size> : 设置单个线程的 Java 虚拟机栈大小 (Thread Stack Size)。该参数决定了方法调用的最大深度（栈帧数量）。
 * -Xmx<size> : 设置 JVM 堆内存的最大可用容量 (Maximum Heap Size)。该参数决定了系统能够存放的对象实例总量的物理上限。
 * <p>
 * Thread 对象中占据空间最多的部分是储存待执行方法的 Java 栈。注意 Thread 对象的大小会随不同的操作系统而不同。
 * 下面这段代码测试了这一点，它会不断地创建 Thread 对象，直到 JVM 内存耗尽。
 * <p>
 * 只要你一直向 CachedThreadPool 传入任务，它就会不断地创建 Thread。
 * 向 execute() 传入 Dummy 对象即可开启任务，分配一个新的 Thread（如果没有现成可用的 Thread）。
 * Nap() 的参数必须足够大，这样任务就不会进入完成阶段，使得现有的 Thread 被释放，以执行新任务。
 * 只要不断传入任务并且使任务无法完成，CachedThreadPool 就最终会耗尽内存。
 */
public class ThreadSize {
    // 1. 预分配 2MB 的内存保留区
    // 在程序启动时，先在堆内存中占领一块无用的空间。当真正发生 OOM 时，立刻释放这块空间，从而为异常处理代码（如打印错误日志）腾出宝贵的“呼吸空间”。
    private static byte[] reserveMemory = new byte[2 * 1024 * 1024];

    static class Dummy extends Thread {
        @Override
        public void run() {
            new Nap(300);
        }
    }

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        int count = 0;
        try {
            while (true) {
                exec.execute(new Dummy());
                count++;
            }
        } catch (Error e) {
            // 2. 发生 OOM 时，立刻将保留区引用置空，瞬间释放 2MB 堆内存
            reserveMemory = null;
            // 此时堆内存已有充足空间，可以安全地执行字符串创建和打印操作
            System.out.println(e.getClass().getSimpleName() + ": " + count);
            System.exit(0);
        } finally {
            exec.shutdown();
        }
    }
}
/* Output:
OutOfMemoryError: 42536
 */
