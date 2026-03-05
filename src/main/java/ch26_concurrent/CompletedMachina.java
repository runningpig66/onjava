package ch26_concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author runningpig66
 * @date 3月4日 周三
 * @time 2:22
 * P.241 §5.10 CompletableFuture §5.10.1 基本用法
 * <p>
 * 还可以利用 CompletableFuture，通过 completedFuture() 方法来包装一个对象：
 */
public class CompletedMachina {
    public static void main(String[] args) {
        // static <U> CompletableFuture<U> completedFuture(U value)
        // completedFuture：创建并返回一个已经是“完成（Completed）”状态的 CompletableFuture。它包含的值就是你传入的参数对象。
        // 作用：通常用于将同步的、已存在的数据，包装成异步的 CompletableFuture 形式，以便与异步 API 的签名进行对接或参与链式调用。
        // 因为 cf 在创建时就已经是完成状态，所以这里的 get() 方法会瞬间返回结果。当前 main 线程绝对不会在此发生任何物理层面的挂起或阻塞。
        CompletableFuture<Machina> cf = CompletableFuture.completedFuture(new Machina(0));
        try {
            Machina m = cf.get(); // Doesn't block 不会阻塞，因为 Future 已经完成
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
