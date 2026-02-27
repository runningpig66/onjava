package ch25_annotations;

import onjava.atunit.Test;
import onjava.atunit.TestObjectCreate;

/**
 * @author runningpig66
 * @date 2月26日 周四
 * @time 5:02
 * P.187 §4.4 基于注解的单元测试
 * {java -cp build/classes/java/main onjava.atunit.AtUnit
 * build/classes/java/main/ch25_annotations/AtUnitExample3.class}
 * <p>
 * 在每个单元测试中，@Unit 都通过无参数的构造方法，为每个要测试的类创建了一个对象。测试会在该对象上进行，
 * 然后该对象会被丢弃，以防止各种副作用渗透到其他单元测试中。这里依赖无参数的构造方法来创建对象。
 * 如果没有无参数的构造函数，或者需要更复杂的构造函数，你需要创建一个静态方法来构建对象，并添加 @TestObjectCreate 注解，就像下面这样：
 */
public class AtUnitExample3 {
    private int n;

    public AtUnitExample3(int n) {
        this.n = n;
    }

    public int getN() {
        return n;
    }

    public String methodOne() {
        return "This is methodOne";
    }

    public int methodTwo() {
        System.out.println("This is methodTwo");
        return 2;
    }

    // @TestObjectCreate 方法必须是静态的，并且必须返回你测试的类型的对象。@Unit 程序会确保这些。
    @TestObjectCreate
    static AtUnitExample3 create() {
        return new AtUnitExample3(47);
    }

    @Test
    boolean initialization() {
        return n == 47;
    }

    @Test
    boolean methodOneTest() {
        return methodOne().equals("This is methodOne");
    }

    @Test
    boolean m2() {
        return methodTwo() == 2;
    }
}
/* Output:
ch25_annotations.AtUnitExample3
  . initialization
  . methodOneTest
  . m2 This is methodTwo

OK (3 tests)
 */
