package ch25_annotations;

import onjava.atunit.Test;

/**
 * @author runningpig66
 * @date 2月27日 周五
 * @time 2:34
 * P.191 §4.4 基于注解的单元测试 §4.4.1 在 @Unit 中使用泛型
 * Applying @Unit to generics
 * {java -cp build/classes/java/main onjava.atunit.AtUnit
 * build/classes/java/main/ch25_annotations/StackLStringTst.class}
 * <p>
 * 如果要测试字符串的版本，则从 StackL<String> 继承一个测试类：
 * <p>
 * 继承的唯一潜在缺点是，你会失去访问被测试类中 private 方法的能力。如果你不希望这样，则可以将该方法设为 protected，
 * 或者添加一个非私有的 @TestProperty 方法来调用该私有方法（本章稍后介绍的 AtUnitRemover 工具会从产品代码中剥离 @TestProperty 方法）。
 */
public class StackLStringTst extends StackL<String> {
    @Test
    void tPush() {
        push("one");
        assert top().equals("one");
        push("two");
        assert top().equals("two");
    }

    @Test
    void tPop() {
        push("one");
        push("two");
        assert pop().equals("two");
        assert pop().equals("one");
    }

    @Test
    void tTop() {
        push("A");
        push("B");
        assert top().equals("B");
        assert top().equals("B");
    }
}
/* Output:
ch25_annotations.StackLStringTst
  . tPop
  . tTop
  . tPush
OK (3 tests)
 */
