// onjava/Repeat.java
// (c)2021 MindView LLC: see Copyright.txt
// We make no guarantees that this code is fit for any purpose.
// Visit http://OnJava8.com for more book information.
package onjava;

import static java.util.stream.IntStream.range;

/**
 * @author runningpig66
 * @date 2025/10/23 周四
 * @time 22:56
 * 代码清单 P.383 为了取代简单的 for 循环，这里有一个 repeat() 工具函数
 */
public class Repeat {
    public static void repeat(int n, Runnable action) {
        range(0, n).forEach(i -> action.run());
    }
}
