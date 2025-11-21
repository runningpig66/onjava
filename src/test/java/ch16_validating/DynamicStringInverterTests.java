package ch16_validating;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author runningpig66
 * @date 2025/11/20 周四
 * @time 16:23
 * P.499 §16.3 测试驱动开发
 * <pre>
 * public static <T> Stream<DynamicTest> stream(
 *          Iterator<T> inputGenerator,
 * 			Function<? super T, String> displayNameGenerator,
 * 		    ThrowingConsumer<? super T> testExecutor
 * )
 * </pre>
 * 在这个例子中，所有本来会被复制的代码都被合并到了 testVersions() 中。
 * 迭代器生成的对象是 StringInverter 的不同实现，
 * 这些不同实现演示了我们是如何一步步添加新功能、最终满足所有测试要求的：
 * notes: StringInverter.md 为什么要引入动态测试？（※重要）
 */
public class DynamicStringInverterTests {
    // Combine operations to prevent code duplication: 组合操作来防止重复代码：
    Stream<DynamicTest> testVersions(String id, Function<StringInverter, String> test) {
        List<StringInverter> versions = Arrays.asList(
                new Inverter1(), new Inverter2(), new Inverter3(), new Inverter4());
        return DynamicTest.stream(
                // Params 1: 一组对象的选代器。每组测试的对象都是不同的。
                // 该送代器生成的对象可以是任何类型，但每次只生成一个对象，
                // 因此对于不同的多个项目，你必须人为地将它们打包成一个类型。
                versions.iterator(),
                // Params 2: 一个 Function, 它从迭代器中获取对象并生成一个字符串来描述这个测试。
                inverter -> inverter.getClass().getSimpleName(),
                // Params 3: 一个 Consumer, 它接受来自迭代器的对象，并包含了基于该对象的测试代码。
                inverter -> {
                    System.out.println(inverter.getClass().getSimpleName() + ": " + id);
                    try {
                        if (test.apply(inverter) != "fail") {
                            System.out.println("Success");
                        }
                    } catch (Exception | Error e) {
                        System.out.println("Exception: " + e.getMessage());
                    }
                }
        );
    }

    String isEqual(String lval, String rval) {
        if (lval.equals(rval)) {
            return "success";
        }
        System.out.println("FAIL: " + lval + " != " + rval);
        return "fail";
    }

    @BeforeAll
    static void startMsg() {
        System.out.println(">>> Starting DynamicStringInverterTests <<<");
    }

    @AfterAll
    static void endMsg() {
        System.out.println(">>> Finished DynamicStringInverterTests <<<");
    }

    // 用 @TestFactory 注解标注过的每个方法都会生成一个 DynamicTest 对象的流（通过 testVersions()），
    // JUnit 会像执行常规的 @Test 方法一样执行流里的每个测试。
    // 场景 1: 用一整句包含大小写字母和标点的字符串，验证 invert() 是否能把整句的大小写完全按预期翻转。
    @TestFactory
    Stream<DynamicTest> basicInversion1() {
        String in = "Exit, Pursued by a Bear.";
        String out = "eXIT, pURSUED BY A bEAR.";
        return testVersions(
                "Basic inversion (should succeed)",
                inverter -> isEqual(inverter.invert(in), out)
        );
    }

    // 场景 2: 用单个字符 "X" 作为测试用例，故意给出“X 反转后还是 X”这种错误期望，
    // 用来演示：在 TDD 过程中，这个场景一开始应该是会失败的。
    @TestFactory
    Stream<DynamicTest> basicInversion2() {
        return testVersions(
                "Basic inversion (should fail)",
                inverter -> isEqual(inverter.invert("X"), "X")
        );
    }

    // 场景 3: 准备一串“应该被视为非法”的字符（数字和特殊符号），
    // 逐个传给 invert(), 检查实现是否会对这些非法字符做出正确反应（按设计，它们不该被当成正常可处理的输入）。
    @TestFactory
    Stream<DynamicTest> disallowedCharacters() {
        String disallowed = ";-_()*&^%$#@!~`0123456789";
        return testVersions("Disallowed characters", inverter -> {
                    String result = disallowed.chars().mapToObj(c -> {
                        String cc = Character.toString((char) c);
                        try {
                            inverter.invert(cc);
                            return "";
                        } catch (RuntimeException e) {
                            return cc; // 记录单个非法字符
                        }
                    }).collect(Collectors.joining("")); // 汇总所有非法字符
                    if (result.length() == 0) {
                        return "success";
                    }
                    System.out.println("Bad characters: " + result);
                    return "fail";
                }
        );
    }

    // 场景 4: 对一组“允许的字符”（26 个字母 + 空格 + 逗号 + 点号）做往返测试：
    // lowcase → invert() 应得到 upcase,
    // upcase  → invert() 应得到 lowcase, 验证所有允许字符都被正确翻转。
    @TestFactory
    Stream<DynamicTest> allowedCharacters() {
        String lowcase = "abcdefghijklmnopqrstuvwxyz ,.";
        String upcase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ ,.";
        return testVersions("Allowed characters (should succeed)", inverter -> {
                    assertEquals(upcase, inverter.invert(lowcase));
                    assertEquals(lowcase, inverter.invert(upcase));
                    return "success";
                }
        );
    }

    // 场景 5: 构造一个长度 > 30 的字符串，验证对于“超过最大长度”的输入，各个实现是否按约定直接拒绝（抛异常）。
    @TestFactory
    Stream<DynamicTest> lengthNoGreaterThan30() {
        String str = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
        assertTrue(str.length() > 30);
        return testVersions("Length must be less than 31 (throws exception)",
                inverter -> inverter.invert(str)
        );
    }

    // 场景 6: 构造一个长度 < 31 的字符串，验证在长度上限之内的输入会被各个实现正常接受并处理（不应该抛异常）。
    @TestFactory
    Stream<DynamicTest> lengthLessThan31() {
        String str = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
        assertTrue(str.length() < 31);
        return testVersions("Length must be less than 31 (should succeed)",
                inverter -> inverter.invert(str)
        );
    }
}
/* Output
Inverter1: Basic inversion (should succeed)
FAIL: Exit, Pursued by a Bear. != eXIT, pURSUED BY A bEAR.
Inverter2: Basic inversion (should succeed)
Success
Inverter3: Basic inversion (should succeed)
Success
Inverter4: Basic inversion (should succeed)
Success
Inverter1: Basic inversion (should fail)
Success
Inverter2: Basic inversion (should fail)
FAIL: x != X
Inverter3: Basic inversion (should fail)
FAIL: x != X
Inverter4: Basic inversion (should fail)
FAIL: x != X
Inverter1: Disallowed characters
Success
Inverter2: Disallowed characters
Success
Inverter3: Disallowed characters
Success
Inverter4: Disallowed characters
Bad characters: ;-_()*&^%$#@!~`0123456789
Inverter1: Allowed characters (should succeed)
Exception: expected: <ABCDEFGHIJKLMNOPQRSTUVWXYZ ,.> but was: <abcdefghijklmnopqrstuvwxyz ,.>
Inverter2: Allowed characters (should succeed)
Success
Inverter3: Allowed characters (should succeed)
Success
Inverter4: Allowed characters (should succeed)
Success
Inverter1: Length must be less than 31 (throws exception)
Success
Inverter2: Length must be less than 31 (throws exception)
Success
Inverter3: Length must be less than 31 (throws exception)
Exception: argument too long!
Inverter4: Length must be less than 31 (throws exception)
Exception: argument too long!
Inverter1: Length must be less than 31 (should succeed)
Success
Inverter2: Length must be less than 31 (should succeed)
Success
Inverter3: Length must be less than 31 (should succeed)
Success
Inverter4: Length must be less than 31 (should succeed)
Success
 */
