package ch26_concurrent;

import onjava.Timer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * @author runningpig66
 * @date 2月28日 周六
 * @time 7:32
 * P.218 §5.7 并行流
 */
public class ParallelPrime {
    static final int COUNT = 100_000;

    public static boolean isPrime(long n) {
        return LongStream.rangeClosed(2, (long) Math.sqrt(n))
                .noneMatch(i -> n % i == 0);
    }

    public static void main(String[] args) throws IOException {
        Timer timer = new Timer();
        List<String> primes = LongStream.iterate(2, i -> i + 1)
                .parallel() // [1] 当注释掉 [1] parallel() 这一行后，耗时大概是使用 parallel() 时的 3 倍。
                .filter(ParallelPrime::isPrime)
                .limit(COUNT)
                .mapToObj(Long::toString)
                .collect(Collectors.toList());
        System.out.println(timer.duration());
        Files.write(Paths.get("src/main/java/ch26_concurrent/primes.txt"), primes, StandardOpenOption.CREATE);
    }
}
/* Output:
228
 */
