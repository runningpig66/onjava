package ch26_concurrent;

/**
 * @author runningpig66
 * @date 3月6日 周五
 * @time 22:05
 * P.265 §5.12 构造器并不是线程安全的
 */
public class StaticIDField implements HasID {
    private static int counter = 0;
    private int id = counter++;

    @Override
    public int getID() {
        return id;
    }
}
