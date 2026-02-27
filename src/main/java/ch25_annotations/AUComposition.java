package ch25_annotations;

import onjava.atunit.Test;

/**
 * @author runningpig66
 * @date 2月26日 周四
 * @time 3:09
 * P.184 §4.4 基于注解的单元测试
 * Creating non-embedded tests
 * {java -cp build/classes/java/main onjava.atunit.AtUnit
 * build/classes/java/main/ch25_annotations/AUComposition.class}
 * <p>
 * 你也可以用组合的方式来创建非嵌入的测试：
 * <p>
 * 这里给每个测试都创建了一个 AUComposition 对象，因此也为每个测试都创建了一个新的 testObject 成员。
 */
public class AUComposition {
    AtUnitExample1 testObject = new AtUnitExample1();

    @Test
    boolean tMethodOne() {
        return testObject.methodOne().equals("This is methodOne");
    }

    @Test
    boolean tMethodTwo() {
        return testObject.methodTwo() == 2;
    }
}
/* Output:
ch25_annotations.AUComposition
  . tMethodOne
  . tMethodTwo This is methodTwo

OK (2 tests)
 */
