package ch16_validating;

import static java.lang.Character.*;

/**
 * @author runningpig66
 * @date 2025/11/20 周四
 * @time 16:38
 * P.502 §16.3 测试驱动开发
 * <p>
 * 在 Inverter1 的基础上，接下来实现大小写反转操作：
 */
public class Inverter2 implements StringInverter {
    @Override
    public String invert(String str) {
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            result += isUpperCase(c) ? toLowerCase(c) : toUpperCase(c);
        }
        return result;
    }
}
