package ch14_streams;

import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/11/5 周三
 * @time 7:12
 * 代码清单 P.404 Optional 类型：由 Optional 组成的流
 * <p>
 * 假设有一个可能会生成 null 值的生成器。如果使用这个生成器创建了一个流，
 * 我们自然想将这些元素包在 Optional 中。它看上去应该是这样的：
 */
public class Signal {
    private final String msg;

    public Signal(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "Signal(" + msg + ")";
    }

    static Random rand = new Random(47);

    public static Signal morse() {
        switch (rand.nextInt(4)) {
            case 1:
                return new Signal("dot");
            case 2:
                return new Signal("dash");
            default:
                return null;
        }
    }

    public static Stream<Optional<Signal>> stream() {
        return Stream.generate(Signal::morse)
                .map(signal -> Optional.ofNullable(signal));
    }
}
