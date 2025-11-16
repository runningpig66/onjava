package ch15_exceptions;

/**
 * @author runningpig66
 * @date 2025/11/16 周日
 * @time 3:10
 * 代码清单 P.462 try-with-resources 语句：细节揭秘
 * <p>
 * 最后，让我们看看 close() 方法会抛出异常的情况。
 */
class CloseException extends Exception {
}

class Reporter2 implements AutoCloseable {
    String name = getClass().getSimpleName();

    Reporter2() {
        System.out.println("Creating " + name);
    }

    @Override
    public void close() throws CloseException {
        System.out.println("Closing " + name);
    }
}

class Closer extends Reporter2 {
    @Override
    public void close() throws CloseException {
        super.close();
        throw new CloseException();
    }
}

public class CloseExceptions {
    public static void main(String[] args) {
        try (First f = new First();
             Closer c = new Closer();
             Second s = new Second()
        ) {
            System.out.println("In body");
        } catch (CloseException e) {
            System.out.println("Caught: " + e);
        }
    }
}
// 请注意，因为这三个对象都被创建出来了，所以它们又都以相反的顺序被关闭了，即使 Closer.close() 抛出了异常。
/* Output:
Creating First
Creating Closer
Creating Second
In body
Closing Second
Closing Closer
Closing First
Caught: ch15_exceptions.CloseException
 */
