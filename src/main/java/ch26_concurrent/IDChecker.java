package ch26_concurrent;

import com.google.common.collect.Sets;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 3月6日 周五
 * @time 22:07
 * P.265 §5.12 构造器并不是线程安全的
 * <p>
 * MakeObjects 类是个 Supplier，通过 get() 方法来生成 List<Integer>。该 List 是通过从每个 HasID 对象中提取 id 而生成的。
 * test() 方法创建了两个并行的 CompletableFuture 来运行 MakeObjects 的 supplier，然后接收两者的结果，
 * 并通过 Guava 库 Sets.intersection() 来找出这两个 List<Integer> 中有多少个 id 是相同的（Guava 比 retainAll() 要快很多）。
 * <p>
 * notes: race-condition-and-thread-confinement.md
 */
public class IDChecker {
    public static final int SIZE = 100_000;

    static class MakeObjects implements Supplier<List<Integer>> {
        private Supplier<HasID> gen;

        MakeObjects(Supplier<HasID> gen) {
            this.gen = gen;
        }

        @Override
        public List<Integer> get() {
            return Stream.generate(gen)
                    .limit(SIZE)
                    .map(HasID::getID)
                    .collect(Collectors.toList());
        }
    }

    public static void test(Supplier<HasID> gen) {
        CompletableFuture<List<Integer>>
                groupA = CompletableFuture.supplyAsync(new MakeObjects(gen)),
                groupB = CompletableFuture.supplyAsync(new MakeObjects(gen));
        groupA.thenAcceptBoth(groupB, (a, b) ->
                System.out.println(Sets.intersection(
                        Sets.newHashSet(a),
                        Sets.newHashSet(b)).size())).join();
    }
}
