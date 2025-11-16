package ch15_exceptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author runningpig66
 * @date 2025/11/16 周日
 * @time 3:25
 * 代码清单 P.463 try-with-resources 语句：新特性：try-with-resources 中的实际上的最终变量
 * {NewFeature} Since JDK 9
 * <p>
 * 最初的 try-with-resources 要求将所有被管理的变量定义在资源说明头（即 try 后面的括号列表）之中。
 * 出于某种原因，Java 团队认为这有时会显得过于笨拙。
 * JDK 9 增加了在 try 之前定义这些变量的能力，只要它们被显式地声明为最终变量，或者是实际上的最终变量即可。
 * 下面的示例对比了原来的 try-with-resources 语法和（可选的）JDK 9 语祛。
 */
public class EffectivelyFinalTWR {
    static final String root = "src/main/java/ch15_exceptions/";

    static void old() {
        try (InputStream r1 = new FileInputStream(new File(root, "TryWithResources.java"));
             InputStream r2 = new FileInputStream(new File(root, "EffectivelyFinalTWR.java"))
        ) {
            r1.read();
            r2.read();
        } catch (IOException e) {
            // Handle exceptions
        }
    }

    // 通过说明 throws IOException, jdk9() 会将异常传出来。
    // 这是因为 r1 和 r2 的定义没有像在 old() 中那样放到 try 块内。
    // 无法捕捉异常是这个新特性看起来不那么可信的一个原因。
    static void jdk9() throws IOException {
        final InputStream r1 = new FileInputStream(new File(root, "TryWithResources.java"));
        // Effectively final:
        InputStream r2 = new FileInputStream(new File(root, "EffectivelyFinalTWR.java"));
        try (r1; r2) {
            r1.read();
            r2.read();
        }
        // r1 and r2 are still in scope. Accessing either one throws an exception:
        // 在引用变量被 try-with-resources 释放之后再引用它们是可能的，正如我们在 jdk9() 的最后所看到的那样。
        // 编译器允许这么做，但是当我们在 try 块的外面访问 r1 或 r2 时，会触发异常。
        r1.read();
        r2.read();
        // 目前还不清楚这个新特性带来的好处是不是值得我们将这种检查从编译时移到运行时，
        // 同时我们也失去了 catch 子句。完全避免使用这个特性可能是个好主意。
    }

    public static void main(String[] args) {
        old();
        try {
            jdk9();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
/* Output:
java.io.IOException: Stream Closed
 */
