package ch25_annotations;

import onjava.atunit.Test;

/**
 * @author runningpig66
 * @date 2月26日 周四
 * @time 2:55
 * P.184 §4.4 基于注解的单元测试
 * Creating non-embedded tests
 * {java -cp build/classes/java/main onjava.atunit.AtUnit
 * build/classes/java/main/ch25_annotations/AUExternalTest.class}
 * <p>
 * 如果将测试方法嵌入类中对你来说并不适用，那么你就无须这么做。要创建非嵌入式的测试，最简单的方法是使用继承：
 * <p>
 * 继承的唯一潜在缺点是，你会失去访问被测试类中 private 方法的能力。如果你不希望这样，则可以将该方法设为 protected，
 * 或者添加一个非私有的 @TestProperty 方法来调用该私有方法（本章稍后介绍的 AtUnitRemover 工具会从产品代码中剥离 @TestProperty 方法）。
 */
public class AUExternalTest extends AtUnitExample1 {
    @Test
    boolean tMethodOne() {
        return methodOne().equals("This is methodOne");
    }

    @Test
    boolean tMethodTwo() {
        return methodTwo() == 2;
    }
}
/* Output:
ch25_annotations.AUExternalTest
  . tMethodOne
  . tMethodTwo This is methodTwo

OK (2 tests)
 */
