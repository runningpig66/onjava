package ch15_exceptions;

/**
 * @author runningpig66
 * @date 2025/11/16 周日
 * @time 3:02
 * 代码清单 P.461 try-with-resources 语句：细节揭秘
 * <p>
 * 如果构造器都不会抛出异常，但是在 try 块中可能抛出异常，编译器又会强制我们提供一个 catch 子句。
 */
class Third extends Reporter {
}

public class BodyException {
    public static void main(String[] args) {
        try (First f = new First();
             Second s2 = new Second()
        ) {
            System.out.println("In body");
            // 注意，Third 对象永远不会得到清理。这是因为，它不是在资源说明头中创建的，所以它的清理得不到保证。
            Third t = new Third();
            new SecondExcept();
            System.out.println("End of body");
        } catch (CE e) {
            System.out.println("Caught: " + e);
        }
    }
}
/* Output:
Creating First
Creating Second
In body
Creating Third
Creating SecondExcept
Closing Second
Closing First
Caught: ch15_exceptions.CE
 */
