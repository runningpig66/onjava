package ch27_lowlevel;

/**
 * @author runningpig66
 * @date 3月11日 周三
 * @time 3:42
 * P.295 §6.4 volatile 关键字 §6.4.3（指令）重排序和先行发生
 * <p>
 * 在对 volatile 变量的读或写操作之前出现的指令，保证会在该读或写操作之前执行。
 * 同样，任何在 volatile 变量的读或写操作之后的指令，会保证在读或写操作之后执行。例如：
 */
public class ReOrdering implements Runnable {
    int one, two, three, four, five, six;
    volatile int volaTile;

    @Override
    public void run() {
        one = 1;
        two = 2;
        three = 3;
        volaTile = 92;
        int x = four;
        int y = five;
        int z = six;
    }
}
