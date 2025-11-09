package ch14_streams;

import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/11/1 周六
 * @time 23:12
 * 代码清单 P.393 在应用 map() 期间组合流：
 * flatMap() 会做两件事：接受生成流的函数，并将其应用于传入元素（就像 map() 所做的那样）,
 * 然后再将每个流“扁平化”处理。将其展开为元素。所以传出来的就都是元素了。
 * <p>
 * 为了了解它是如何工作的，我们虚构了一个用于 map 的没有实际意义的函数，
 * 它接受一个 Integer, 生成由 String 组成的流：
 */
public class StreamOfStreams {
    public static void main(String[] args) {
        Stream.of(1, 2, 3)
                .map(i -> Stream.of("Gonzo", "Kermit", "Beaker"))
                .map(e -> e.getClass().getName())
                .forEach(System.out::println);
    }
}
/* Output:
java.util.stream.ReferencePipeline$Head
java.util.stream.ReferencePipeline$Head
java.util.stream.ReferencePipeline$Head
 */
