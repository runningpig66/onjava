package ch27_lowlevel;

import onjava.Nap;

import java.util.concurrent.CompletableFuture;

/**
 * @author runningpig66
 * @date 3月12日 周四
 * @time 8:47
 * P.301 §6.5 原子性 §6.5.1 Josh 的序列号
 * Test SerialNumbers implementations for thread-safety
 * <p>
 * SerialNumberChecker 中含有一个持有最新一批序列号的 CircularSet，
 * 以及用于填充 CircularSet 并确保这些序列号唯一的 run() 方法：
 * <p>
 * test() 方法创建了多个 SerialNumberChecker 任务来争夺一个 SerialNumbers 对象。
 * SerialNumberChecker 任务试图生成一个重复的序列号（该过程在多核机器上会更快）。
 */
public class SerialNumberChecker implements Runnable {
    private CircularSet serials = new CircularSet(1000);
    private SerialNumbers producer;

    public SerialNumberChecker(SerialNumbers producer) {
        this.producer = producer;
    }

    @Override
    public void run() {
        while (true) {
            int serial = producer.nextSerialNumber();
            if (serials.contains(serial)) {
                System.out.println("Duplicate: " + serial);
                System.exit(0);
            }
            serials.add(serial);
        }
    }

    static void test(SerialNumbers producer) {
        for (int i = 0; i < 10; i++) {
            CompletableFuture.runAsync(new SerialNumberChecker(producer));
        }
        new Nap(4, "No duplicates detected");
    }
}
