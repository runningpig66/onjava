package ch15_exceptions;

/**
 * @author runningpig66
 * @date 2025/11/16 周日
 * @time 1:46
 * 代码清单 P.459 try-with-resources 语句：细节揭秘
 * <p>
 * 为了研究 try-with-resources 的底层机制，可以创建自已的实现了 AutoCloseable 接口的类。
 */
class Reporter implements AutoCloseable {
    String name = getClass().getSimpleName();

    Reporter() {
        System.out.println("Creating " + name);
    }

    @Override
    public void close() {
        System.out.println("Closing " + name);
    }
}

class First extends Reporter {
}

class Second extends Reporter {
}

public class AutoCloseableDetails {
    public static void main(String[] args) {
        try (First f = new First();
             Second s = new Second()) {
        } // 在退出 try 块时会调用两个对象的 close() 方法，而且会以与创建顺序相反的顺序关闭它们。
    }
}
/* Output:
Creating First
Creating Second
Closing Second
Closing First
 */
