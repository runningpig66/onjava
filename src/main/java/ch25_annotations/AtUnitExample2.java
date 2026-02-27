package ch25_annotations;

import onjava.atunit.Test;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author runningpig66
 * @date 2月26日 周四
 * @time 4:17
 * P.185 §4.4 基于注解的单元测试
 * Assertions and exceptions can be used in @Tests
 * {java -cp build/classes/java/main onjava.atunit.AtUnit
 * build/classes/java/main/ch25_annotations/AtUnitExample2.class}
 * <p>
 * 和 JUnit 不同，这里并没有专门的“assert”（断言）方法，而是使用了 @Test 方法的第二种形式，
 * 返回了 void（或者 boolean，如果你仍然希望在这里返回 true 或 false）。如果要验证成功，你可以使用 Java 的断言语句。
 * Java 断言一般在 java 命令行指令中由 -ea 标签来启用，但是 @Unit 会自动启用断言。
 * 如果要表示失败，你甚至可以使用异常。@Unit 的设计目标之一是尽可能不增加语法复杂度，而 Java 断言和异常则是报告错误所必需的。
 * 如果测试方法引发了失败断言或者异常，则会被视为测试失败，但是 @Unit 并不会因此阻塞——它会持续运行，直到所有的测试都运行完毕。如下例所示：
 */
public class AtUnitExample2 {
    public String methodOne() {
        return "This is methodOne";
    }

    public int methodTwo() {
        System.out.println("This is methodTwo");
        return 2;
    }

    @Test
    void assertExample() {
        assert methodOne().equals("This is methodOne");
    }

    @Test
    void assertFailureExample() {
        assert 1 == 2 : "What a surprise!";
    }

    @Test
    void exceptionExample() throws IOException {
        try (FileInputStream fis = new FileInputStream("nofile.txt")) {
        } // Throws
    }

    @Test
    boolean assertAndReturn() {
        // Assertion with message:
        assert methodTwo() == 2 : "methodTwo must equal 2";
        return methodOne().equals("This is methodOne");
    }
}
/* Output:
ch25_annotations.AtUnitExample2
  . assertFailureExample java.lang.AssertionError: What a surprise!
(failed)
  . assertExample
  . exceptionExample java.io.FileNotFoundException: nofile.txt (系统找不到指定的文件。)
(failed)
  . assertAndReturn This is methodTwo

(4 tests)

>>> 2 FAILURES <<<
  ch25_annotations.AtUnitExample2: assertFailureExample
  ch25_annotations.AtUnitExample2: exceptionExample
 */
