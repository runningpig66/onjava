package ch25_annotations;

import onjava.atunit.Test;

/**
 * @author runningpig66
 * @date 2月26日 周四
 * @time 1:33
 * P.183 §4.4 基于注解的单元测试
 * {java -cp build/classes/java/main onjava.atunit.AtUnit
 * build/classes/java/main/ch25_annotations/AtUnitExample1.class}
 * <p>
 * 下面这个测试框架的示例是基于注解的，因此叫作 @Unit。只用一个 @Test 注解来标识出需要测试的方法，是最基础也可能是你最常用的测试形式。
 * 也可以选择让测试方法不接收参数，仅返回一个 boolean 值来表示测试是成功还是失败。你可以给测试方法起任何你喜欢的名字。
 * 同样，@Unit 注解的测试方法可以支持任何你想要的访问权限，包括 private。
 * <p>
 * methodOneTest()、m2()、m3()、failureTest() 和 anotherDisappointment() 等方法前面的 @Test 注解告诉 @Unit 要将这些方法作为单元测试来运行，
 * 它同样也会确保这些方法不接收任何参数，并且返回值为 boolean 或 void。你编写单元测试时，只需要确定测试是成功还是失败，并分别返回 true 或者 false（对于返回 boolean 值的方法）。
 */
public class AtUnitExample1 {
    public String methodOne() {
        return "This is methodOne";
    }

    public int methodTwo() {
        System.out.println("This is methodTwo");
        return 2;
    }

    @Test
    boolean methodOneTest() {
        return methodOne().equals("This is methodOne");
    }

    @Test
    boolean m2() {
        return methodTwo() == 2;
    }

    @Test
    private boolean m3() {
        return true;
    }

    // Shows output for failure:
    @Test
    boolean failureTest() {
        return false;
    }

    @Test
    boolean anotherDisappointment() {
        return false;
    }
}
/* Output:
ch25_annotations.AtUnitExample1
  . m2 This is methodTwo

  . m3
  . methodOneTest
  . failureTest (failed)
  . anotherDisappointment (failed)
(5 tests)

>>> 2 FAILURES <<<
  ch25_annotations.AtUnitExample1: failureTest
  ch25_annotations.AtUnitExample1: anotherDisappointment
 */
