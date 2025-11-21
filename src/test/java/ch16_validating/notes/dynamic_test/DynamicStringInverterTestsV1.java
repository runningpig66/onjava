package ch16_validating.notes.dynamic_test;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author runningpig66
 * @date 2025/11/21 周五
 * @time 0:39
 * <pre>
 * public static DynamicTest dynamicTest(String displayName, Executable executable)
 * 测试设计说明：
 * - StringInverter 是一个接口，有多个实现版本 Inverter1~4。
 * - 我们希望：对所有版本运行同一组测试场景，而不是复制粘贴 4 份测试代码（如同 ManyInvertersNaiveTest.java 那样）。
 * 做法：
 * - 用 VERSIONS 收集所有要参与测试的实现。
 * - forAllVersions(description, check)：
 *      * 遍历 VERSIONS 中的每个实现 inv；
 *      * 对每个 inv 生成一个 DynamicTest，显示名=实现类名+场景描述，
 *        执行体=check.accept(inv)（在这个实现上执行断言逻辑）；
 *      * 返回由这些 DynamicTest 组成的 Stream<DynamicTest>。
 * - 每个 @TestFactory 方法只描述“一个测试场景”的断言逻辑，（每个 @TestFactory 注解 = 一个“测试场景”）
 *   每个场景会通过 forAllVersions() 展开为 VERSIONS.size() 个动态测试
 * 效果：
 * - 每个测试场景 × 每个实现版本 → 展开为多条 DynamicTest 记录。
 * - 新增实现版本时只需要往 VERSIONS 列表里加一个实例，
 * - 新增场景只多写一个 @TestFactory，把“对一个实现怎么测”写清楚即可。
 *   所有测试场景会自动对新版本执行，无需复制测试代码。
 *   这就是“把数据（各个实现）和行为（场景）分开，把组合交给框架做”。
 * </pre>
 */
public class DynamicStringInverterTestsV1 {
    // 把目前要测试的“所有版本”的实现集中成一个列表，以后新增 Inverter5，只要往这个列表里加一项就行。
    // 测试类不依赖具体类名，只依赖接口 StringInverter.
    private static final List<StringInverter> VERSIONS = List.of(
            new Inverter1(), new Inverter2(), new Inverter3(), new Inverter4());

    /**
     * 工具方法：给“所有版本”生成 DynamicTest：
     * 给定一个“测试场景描述” + 对单个实现的测试逻辑（Consumer），>> 在 Consumer 中拿到 inv 执行断言等测试
     * 为 VERSIONS 中的每个实现生成一个 DynamicTest。>> 将测试逻辑 Consumer 运用到每个实现上生成一个 DynamicTest
     * <p>
     * 方法自述：给我本次测试的描述文字，然后把测试逻辑也给我，
     * 我会去遍历所有待执行本次测试的实现，然后把测试依次应用到每一个实现上，
     * 每次应用将形成单个 DynamicTest, 最后把所有的 DynamicTest 放到一个流中返回，
     * 结果动态测试流 Stream<DynamicTest> 正是注解 @TestFactory 批量执行测试所需要的。
     * <p>
     * DynamicTest.dynamicTest() 的作用：用代码“生成”一个相当于 @Test 的测试用例对象，
     * 这样一来我们就可以把测试用例收集到集合/Stream 中，再统一返回给 @TestFactory 批量执行。
     */
    private Stream<DynamicTest> forAllVersions(String description, Consumer<StringInverter> check) {
        // 通过 DynamicTest.dynamicTest() 方法把测试应用到 StringInverter 实现上，形成单个动态测试 DynamicTest 对象。
        // 然后通过 map() 函数映射，将 StringInverter 流 转换为 DynamicTest 流。
        return VERSIONS.stream().map(inv ->
                DynamicTest.dynamicTest(
                        inv.getClass().getSimpleName() + ": " + description,
                        () -> check.accept(inv)
                )
        );
    }

    // ===== 下面开始写具体场景 =====

    /**
     * 测试场景 1：检测基本大小写反转是否正确
     */
    @TestFactory
    Stream<DynamicTest> basicInversion() {
        return forAllVersions("basic inversion", inverter -> {
                    assertEquals("aBc", inverter.invert("AbC"));
                    assertEquals("hELLO", inverter.invert("Hello"));
                }
        );
    }

    /**
     * 测试场景 2：检测空字符串是否同样输出空字符串
     */
    @TestFactory
    Stream<DynamicTest> emptyString() {
        return forAllVersions("empty string", inverter ->
                assertEquals("", inverter.invert(""))
        );
    }
}
