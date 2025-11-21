package ch16_validating.notes.dynamic_test;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/11/21 周五
 * @time 4:01
 * 动态测试版本 2：用 Function<StringInverter, Boolean> 表达“属性是否成立”。
 * 原来 (DynamicStringInverterTestsV1)：Consumer<StringInverter> —— 不返回值，内部自己 assertEquals。
 * 现在：Function<StringInverter, Boolean> —— 返回 true/false，由外层统一抛 AssertionError。
 * 好处：性质函数本身就可以在别处复用，比如以后你想在别的测试类里也用；逻辑更接近“数学上的性质测试”。
 * <p>
 * - StringInverter 有多个实现（Inverter1~4）；
 * - 我们只想写“测试条件/性质”（property），让所有版本都跑这一套条件；
 * - testVersions() 把“描述 + 性质函数”套在 VERSIONS 里的每个实现上，生成一组 DynamicTest。
 */
public class DynamicStringInverterTests {

    private static final List<StringInverter> VERSIONS = List.of(
            new Inverter1(), new Inverter2(), new Inverter3(), new Inverter4());

    /**
     * 核心工具方法：
     * - description：本次测试场景的说明文本；
     * - property：给定一个 StringInverter，返回“这个实现是否满足某个性质”的布尔值。
     * <p>
     * 对 VERSIONS 中的每个实现都生成一个 DynamicTest：
     * - 测试名 = 实现类名 + 场景描述；
     * - 测试体 = 调用 property.apply(inv)，如果返回 false 就抛出 AssertionError 让测试失败。
     * <p>
     * 这一版的思想：把“测试”抽象成一个 boolean 性质（property），而不是直接在这里写断言。
     */
    private Stream<DynamicTest> testVersions(String description, Function<StringInverter, Boolean> property) {
        return VERSIONS.stream().map(inv ->
                DynamicTest.dynamicTest(
                        // 每个 DynamicTest 的显示名称：
                        inv.getClass().getSimpleName() + " : " + description,
                        () -> {
                            // 对当前实现 inv 执行“性质检查”
                            Boolean ok = property.apply(inv);
                            // 如果性质不成立，则显式抛出 AssertionError，让该 DynamicTest 标记为失败
                            if (!ok) {
                                throw new AssertionError("Test failed for " + inv.getClass().getSimpleName() + " : " + description);
                            }
                        }
                )
        );
    }

    // 安全比较字符串，避免 null 抛异常
    private static boolean isEqual(String a, String b) {
        return a == null ? b == null : a.equals(b);
    }

    // ===== 具体场景（每个 @TestFactory = 一个“性质/场景”） =====

    /**
     * 测试场景 1：检测基本大小写反转是否正确
     */
    @TestFactory
    Stream<DynamicTest> basicInversion_shouldSucceed() {
        return testVersions("basic inversion (should succeed)", inverter ->
                isEqual("aBc", inverter.invert("AbC")) &&
                        isEqual("hELLO", inverter.invert("Hello"))
        );
    }

    /**
     * 测试场景 2：检测空字符串是否同样输出空字符串
     */
    @TestFactory
    Stream<DynamicTest> emptyString_shouldSucceed() {
        return testVersions("empty string (should succeed)", inverter ->
                isEqual("", inverter.invert("")));
    }
}
