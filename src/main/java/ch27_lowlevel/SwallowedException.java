package ch27_lowlevel;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author runningpig66
 * @date 3月9日 周一
 * @time 3:01
 * P.282 §6.2 捕获异常
 * <p>
 * 下面这个示例可能会出乎你的意料：
 * 这段代码什么都没输出（不过如果你将 submit() 替换为 execute()，便会看到异常）。这说明在线程内部抛出异常很需要技巧，也需要特别仔细的操作。
 * 你无法捕获已逃逸线程的异常。一旦异常逃逸到任务的 run() 方法之外，便会扩散到控制台，除非采用专门的步骤来捕获这种不正确的异常。
 * <p>
 * 对比 ExecutorService 中 submit() 与 execute() 两种任务提交方式的异常处理差异：
 * 1. submit() 的异常封装机制：
 * 为了支持后续获取任务执行结果或异常，线程池框架会主动拦截并捕获任务内部抛出的 RuntimeException。
 * 异常被隐式封装在返回的 Future 对象中，子线程正常退出，不会向控制台输出任何错误信息。
 * 若调用方未通过 future.get() 或 future.exceptionNow() 等方法主动提取，异常将被彻底隐藏（即发生“静默吞噬”）。
 * 2. execute() 的异常逃逸机制：
 * 定位为纯粹的任务执行，不提供返回值追踪与异常封装机制。由于缺乏底层的 try-catch 拦截，任务抛出的异常会直接击穿线程边界（即“逃逸”）。
 * 此时，该异常将交由 Java 底层的 UncaughtExceptionHandler（未捕获异常处理器）接管，其默认行为是将完整的异常堆栈打印至控制台。
 * - 总结：在并发环境中，若采用 submit() 提交任务，必须严格配套 Future 的结果/异常检查机制；
 * 若采用 execute()，则需警惕异常逃逸，通常需要结合自定义的 UncaughtExceptionHandler 进行全局捕获。
 */
public class SwallowedException {
    public static void main(String[] args) {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        // submit() 会捕获任务内部抛出的异常，并将其封装在 Future 对象中，防止异常直接输出到控制台。
        Future<?> future = exec.submit(() -> {
            throw new RuntimeException("子任务执行过程中发生运行时异常");
        });

        // 方案 A: 传统方式 (Java 5+) - 通过 get() 阻塞当前线程并捕获包装异常
        try {
            future.get();
        } catch (InterruptedException e) {
            System.out.println("主线程在等待过程中被中断");
        } catch (ExecutionException e) {
            // get() 抛出的是被包装的 ExecutionException，真实的异常可通过 getCause() 提取
            System.out.println("外层包装类型: " + e.getClass().getSimpleName());
            System.out.println("原始异常类型: " + e.getCause().getClass().getSimpleName());
            System.out.println("原始异常信息: " + e.getCause().getMessage());
        }

        // 循环检查任务是否已结束（无论是正常结束、异常结束还是被取消，isDone 都会返回 true）。
        while (!future.isDone()) {
            Thread.yield();
        }

        // 方案 B: 现代方式 (Java 19+) - 基于状态机的非阻塞提取
        // 因为上方的 get() 已经阻塞等待了任务结束，此时 future 的状态必然是已完成的。
        // 检查 future.state() 确认任务失败后，可安全提取原始异常，无需处理受检异常。
        if (future.state() == Future.State.FAILED) {
            // default Throwable exceptionNow() 获取并返回任务执行过程中抛出的原始异常。
            // 调用此方法前，必须确保任务的当前状态为已失败 (FAILED)，如果任务处于成功完成 (SUCCESS)、已取消 (CANCELLED)
            // 或仍在运行 (RUNNING) 等其他状态，调用该方法将直接抛出 IllegalStateException 异常。
            // 这是 Java 19 引入的现代非阻塞 API，通常在通过 isDone() 或 state() 确认任务结束后配合调用。
            // 它允许开发者直接提取底层的原始异常，避免了传统 get() 方法强制处理 InterruptedException 和 ExecutionException 的繁琐代码。
            Throwable rootCause = future.exceptionNow();
            System.out.println("通过 exceptionNow() 提取的异常类型: " + rootCause.getClass().getSimpleName());
            System.out.println("异常信息: " + rootCause.getMessage());
        }

        /*exec.execute(() -> {
            throw new RuntimeException();
        });*/

        exec.shutdown();
    }
}
/* Output:
外层包装类型: ExecutionException
原始异常类型: RuntimeException
原始异常信息: 子任务执行过程中发生运行时异常
通过 exceptionNow() 提取的异常类型: RuntimeException
异常信息: 子任务执行过程中发生运行时异常
 */
