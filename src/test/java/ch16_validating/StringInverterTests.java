package ch16_validating;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author runningpig66
 * @date 2025/11/21 周五
 * @time 20:45
 * P.504 §16.3 测试驱动开发
 * <p>
 * DynamicStringInverterTests.java 用于展示在 TDD 中 StringInverter 不同实现版本的开发过程。
 * 通常来说，只需要编写如下所示的测试，并修改单个 StringInverter 类。直到它满足所有测试为止：
 */
public class StringInverterTests {
    StringInverter inverter = new Inverter4();

    @BeforeAll
    static void startMsg() {
        System.out.println(">>> Starting InverterTests <<<");
    }

    @AfterAll
    static void endMsg() {
        System.out.println(">>> Finished InverterTests <<<");
    }

    @Test
    void basicInversion1() {
        String in = "Exit, Pursued by a Bear.";
        String out = "eXIT, pURSUED BY A bEAR.";
        assertEquals(out, inverter.invert(in));
    }

    @Test
    void basicInversion2() {
        assertThrows(Error.class, () -> assertEquals("X", inverter.invert("X")));
    }

    @Test
    void disallowedCharacters() {
        String disallowed = ";-_()*&^%$#@!~`0123456789";
        String result = disallowed.chars().mapToObj(c -> {
            String cc = Character.toString((char) c);
            try {
                inverter.invert(cc);
                return "";
            } catch (RuntimeException e) {
                return cc;
            }
        }).collect(Collectors.joining(""));
        assertEquals(disallowed, result);
    }

    @Test
    void allowedCharacters() {
        String lowcase = "abcdefghijklmnopqrstuvwxyz ,.";
        String upcase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ ,.";
        assertEquals(upcase, inverter.invert(lowcase));
        assertEquals(lowcase, inverter.invert(upcase));
    }

    @Test
    void lengthNoGreaterThan30() {
        String str = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
        assertTrue(str.length() > 30);
        assertThrows(RuntimeException.class, () -> inverter.invert(str));
    }

    @Test
    void lengthLessThan31() {
        String str = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
        assertTrue(str.length() < 31);
        inverter.invert(str);
    }
}
