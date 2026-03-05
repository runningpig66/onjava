package ch26_concurrent;

import onjava.Nap;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

/**
 * @author runningpig66
 * @date 3月3日 周二
 * @time 6:42
 * P.239 §5.10 CompletableFuture
 * <p>
 * 为了让你有个初步印象，下面先将 QuittingTasks.java 改为用 CompletableFuture 来实现：
 */
public class QuittingCompletable {
    public static void main(String[] args) {
        List<QuittableTask> tasks = IntStream.range(1, QuittingTasks.COUNT)
                .mapToObj(QuittableTask::new)
                .toList();
        // CompletableFuture：可以被（手动或自动）标记为完成的异步结果凭证。
        List<CompletableFuture<Void>> cfutures = tasks.stream()
                // static CompletableFuture<Void> runAsync(Runnable runnable)
                // runAsync: 将每个任务提交给公共 ForkJoinPool 异步执行，任务需实现 Runnable（无返回值），
                // 立即返回 CompletableFuture<Void>，用于后续追踪任务状态或等待完成。
                .map(CompletableFuture::runAsync)
                .toList();
        new Nap(1);
        tasks.forEach(QuittableTask::quit);
        // T join(): 阻塞等待每个 CompletableFuture 对应的异步任务执行完成。
        // 由于任务无返回值（Void），join() 在此仅用于确保主线程在所有任务结束后再退出，避免因主线程提前结束而中断未完成的后台任务。
        cfutures.forEach(CompletableFuture::join);
    }
}
/* Output:
8 1 17 4 5 3 16 22 23 18 19 26 27 20 29 21 30 24 33 34 35 36 37 25 39 40 41 42 43 44 28 31 47 48 32 50 51 38 45 46 55 56 57 49 59 52 53 62 54 58 65 66 67 68 69 70 71 72 60 61 63 76 64 73 74 75 77 78 79 80 81 82 87 83 84 90 91 92 93 94 85 86 88 98 89 100 95 96 103 104 97 99 101 102 105 106 107 108 109 110 115 111 112 113 114 120 116 117 118 119 121 122 123 124 125 126 127 132 133 134 128 129 137 138 130 131 135 136 139 140 141 142 143 148 144 145 146 147 149 9 12 2 11 10 6 7 15 13 14
 */
