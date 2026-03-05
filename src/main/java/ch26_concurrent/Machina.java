package ch26_concurrent;

import onjava.Nap;

/**
 * @author runningpig66
 * @date 3月4日 周三
 * @time 1:18
 * P.241 §5.10 CompletableFuture §5.10.1 基本用法
 * <p>
 * 下面这个类通过静态的 work() 方法对该类对象执行了某些操作：
 * 这其实是个有限状态机，不过它是随便写的，因为并没有分支……它只是从一条路径的头部移动到尾部。
 * work() 方法使状态机从一个状态移动到下一个状态，并请求了 100 毫秒来执行该“work”。
 */
public class Machina {
    public enum State {
        START, ONE, TWO, THREE, END;

        State step() {
            if (equals(END)) return END;
            return values()[ordinal() + 1];
        }
    }

    private State state = State.START;
    private final int id;

    public Machina(int id) {
        this.id = id;
    }

    public static Machina work(Machina m) {
        System.out.print("currentThread-" + Thread.currentThread().getName() + ": ");
        if (!m.state.equals(State.END)) {
            new Nap(1);
            m.state = m.state.step();
        }
        System.out.println(m);
        return m;
    }

    @Override
    public String toString() {
        return "Machina" + id + ": " + (state.equals(State.END) ? "complete" : state);
    }
}
