package ch15_exceptions;

/**
 * @author runningpig66
 * @date 2025/11/14 周五
 * @time 22:21
 * 代码清单 P.444 使用 finally 执行清理：finally 是干什么用的
 */
public class Switch {
    private boolean state = false;

    public boolean read() {
        return state;
    }

    public void on() {
        state = true;
        System.out.println(this);
    }

    public void off() {
        state = false;
        System.out.println(this);
    }

    @Override
    public String toString() {
        return state ? "on" : "off";
    }
}
