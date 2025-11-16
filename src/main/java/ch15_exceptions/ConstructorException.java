package ch15_exceptions;

/**
 * @author runningpig66
 * @date 2025/11/16 周日
 * @time 2:48
 * 代码清单 P.460 try-with-resources 语句：细节揭秘
 * <p>
 * 如果某个构造器抛出了异常，又会怎么样呢？
 */
class CE extends Exception {
}

class SecondExcept extends Reporter {
    SecondExcept() throws CE {
        super();
        throw new CE();
    }
}

public class ConstructorException {
    public static void main(String[] args) {
        try (First f = new First();
             SecondExcept s = new SecondExcept();
             Second s2 = new Second()
        ) {
            System.out.println("In body");
        } catch (CE e) {
            System.out.println("Caught: " + e);
        }
    }
}
// 不出所料，First 顺利创建，而 SecondExcept 在创建过程中抛出了一个异常。
// 请注意，SecondExcept 的 close() 方法没有被调用。这是因为如果构造器失败了，
// 我们不能假定可以在这个对象上安全地执行任何操作，包括关闭它在内。
// 因为 SecondExcept 抛出了异常，所以 Second 对象 s2 从未被创建，也不会被清理。
/* Output:
Creating First
Creating SecondExcept
Closing First
Caught: ch15_exceptions.CE
 */
