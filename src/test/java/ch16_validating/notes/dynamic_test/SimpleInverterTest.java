package ch16_validating.notes.dynamic_test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author runningpig66
 * @date 2025/11/20 周四
 * @time 22:22
 * 仅对第一版实现 Inverter1.java 的普通测试类
 */
public class SimpleInverterTest {
    // 测试场景 1：检测基本大小写反转是否正确
    @Test
    void basicInversion() {
        Inverter1 inverter = new Inverter1();
        assertEquals("aBc", inverter.invert("AbC"));
        assertEquals("hELLO", inverter.invert("Hello"));
    }

    // 测试场景 2：检测空字符串是否同样输出空字符串
    @Test
    void emptyString() {
        Inverter1 inverter = new Inverter1();
        assertEquals("", inverter.invert(""));
    }
}
