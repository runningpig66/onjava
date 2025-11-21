package ch16_validating.notes.dynamic_test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author runningpig66
 * @date 2025/11/20 周四
 * @time 22:35
 * 现在我们有 4 个实现。分别对 4个版本 Inverter1 Inverter2 Inverter3 Inverter4 的朴素测试写法：复制粘贴地狱。
 * <p>
 * 也就是说：我们想用同样的输入数据和期待数据，对多个版本不同的方法进行测试，
 * 我们不得不分别手动调用各个待测试的方法，然后传入相同的输入数据和期待数据。
 * GPT: 同一个场景（要带入同样的输入和预期输出），要对不同实现重复写一大堆 @Test 方法，
 * 现在只是 2 个测试场景，我们在业务方面因为某种原因同时保留了迭代的 4 个实现版本，测试就要写 8 个 @Test;
 * 如果现实里是 10 个场景、10 个实现，就是 100 个 @Test，而逻辑几乎一模一样——只差类名不同。
 * 这就是书里说的“所有版本都要跑同一套测试”时的痛点。
 */
public class ManyInvertersNaiveTest {
    // 测试场景 1：检测基本大小写反转是否正确
    @Test
    void inverter1_basic() {
        Inverter1 inverter = new Inverter1();
        assertEquals("aBc", inverter.invert("AbC"));
    }

    @Test
    void inverter2_basic() {
        Inverter2 inverter = new Inverter2();
        assertEquals("aBc", inverter.invert("AbC"));
    }

    @Test
    void inverter3_basic() {
        Inverter3 inverter = new Inverter3();
        assertEquals("aBc", inverter.invert("AbC")); // 会失败 expected: <aBc> but was: <ABC>
    }

    @Test
    void inverter4_basic() {
        Inverter4 inverter = new Inverter4();
        assertEquals("aBc", inverter.invert("AbC"));
    }

    // 测试场景 2：检测空字符串是否同样输出空字符串
    @Test
    void inverter1_empty() {
        Inverter1 inverter = new Inverter1();
        assertEquals("", inverter.invert(""));
    }

    @Test
    void inverter2_empty() {
        Inverter2 inverter = new Inverter2();
        assertEquals("", inverter.invert(""));
    }

    @Test
    void inverter3_empty() {
        Inverter3 inverter = new Inverter3();
        assertEquals("", inverter.invert(""));
    }

    @Test
    void inverter4_empty() {
        Inverter4 inverter = new Inverter4();
        assertEquals("", inverter.invert(""));
    }
}
