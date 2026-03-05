package ch26_concurrent;

import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 3月5日 周四
 * @time 22:08
 * P.258 §5.10 CompletableFuture §5.10.5 异常 §1.流异常
 * <p>
 * 我们对 CompletableExceptions.java 做些改动，来看看 CompletableFuture 的异常和 Stream 的异常有何区别：
 * <p>
 * 使用 CompletableFuture 时，我们看到了从测试 A 到测试 E 的执行过程。但是使用 Stream 时，
 * 甚至直到你应用到终结操作（如 [1] 处的 forEach()）之前，什么都不会发生。
 * CompletableFuture 会执行任务，并捕捉任何异常，以备后续的结果取回。
 * 因为 Stream 的机制是在终结操作前不做任何事，所以这两者并不好直接比较。不过 Stream 肯定不会保存异常。
 */
public class StreamExceptions {
    static Stream<Breakable> test(String id, int failcount) {
        return Stream.of(new Breakable(id, failcount))
                .map(Breakable::work)
                .map(Breakable::work)
                .map(Breakable::work)
                .map(Breakable::work);
    }

    public static void main(String[] args) {
        // No operations are even applied ...
        test("A", 1);
        test("B", 2);
        Stream<Breakable> c = test("C", 3);
        test("D", 4);
        test("E", 5);
        // ... until there's a terminal operation:
        System.out.println("Entering try");
        try {
            c.forEach(System.out::println); // [1]
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
/* Output:
Entering try
Breakable_C [2]
Breakable_C [1]
Throwing Exception for C
Breakable_C failed
 */
