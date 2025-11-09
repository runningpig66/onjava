package ch14_streams;

import java.util.Optional;

/**
 * @author runningpig66
 * @date 2025/11/5 周三
 * @time 7:59
 * 代码清单 P.404 Optional 类型：由 Optional 组成的流
 * <p>
 * 当使用这个流时，我们必须弄清楚如何获得 Optional 中的对象：
 */
public class StreamOfOptionals {
    public static void main(String[] args) {
        Signal.stream()
                .limit(10)
                .forEach(System.out::println);
        System.out.println(" --- ");
        Signal.stream()
                .limit(10)
                .filter(Optional::isPresent) // 只保留非 empty 的 Optional
                .map(Optional::get)
                .forEach(System.out::println);
    }
}
/* Output:
Optional[Signal(dash)]
Optional[Signal(dot)]
Optional[Signal(dash)]
Optional.empty
Optional.empty
Optional[Signal(dash)]
Optional.empty
Optional[Signal(dot)]
Optional[Signal(dash)]
Optional[Signal(dash)]
 ---
Signal(dot)
Signal(dot)
Signal(dash)
Signal(dash)
 */
