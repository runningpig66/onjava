package ch14_streams;

/**
 * @author runningpig66
 * @date 2025/10/28 周二
 * @time 21:36
 * 代码清单 P.389 peek() 操作就是用来辅助调试的。它允许我们查看流对象而不修改它们
 */
class Peeking {
    public static void main(String[] args) throws Exception {
        FileToWords.stream("src/main/java/ch14_streams/Cheese.dat")
                .skip(21)
                .limit(4)
                .map(w -> w + " ")
                .peek(System.out::print)
                .map(String::toUpperCase)
                .peek(System.out::print)
                .map(String::toLowerCase)
                .forEach(System.out::print);
    }
}
/* Output:
Well WELL well it IT it s S s so SO so
 */
