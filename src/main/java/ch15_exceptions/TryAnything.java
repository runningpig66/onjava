package ch15_exceptions;

/**
 * @author runningpig66
 * @date 2025/11/16 周日
 * @time 1:57
 * 代码清单 P.460 try-with-resources 语句：细节揭秘
 * {WillNotCompile}
 * <p>
 * 假设我们在资源说明头定义了一个对象，它并没有实现 AutoCloseable 接口：
 */
class Anything {
}

public class TryAnything {
    public static void main(String[] args) {
        // 不出所料，Java 不允许我们这么做，会给出一条编译错误信息。
        try (Anything a = new Anything()) {
        }
    }
}
/* Error Output:
TryAnything.java:18: error: incompatible types: try-with-resources not applicable to variable type
        try (Anything a = new Anything()) {
                      ^
    (Anything cannot be converted to AutoCloseable)
1 error
 */
