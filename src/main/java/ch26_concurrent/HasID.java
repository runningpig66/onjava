package ch26_concurrent;

/**
 * @author runningpig66
 * @date 3月6日 周五
 * @time 22:03
 * P.264 §5.12 构造器并不是线程安全的
 */
public interface HasID {
    int getID();
}
