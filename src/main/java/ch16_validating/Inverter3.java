package ch16_validating;

import static java.lang.Character.*;

/**
 * @author runningpig66
 * @date 2025/11/20 周四
 * @time 16:38
 * P502 §16.3 测试驱动开发
 * <p>
 * 在 Inverter2 的基础上，现在添加代码来确保字符串长度不超过 30 个学符：
 */
public class Inverter3 implements StringInverter {
    @Override
    public String invert(String str) {
        if (str.length() > 30) {
            throw new RuntimeException("argument too long!");
        }
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            result += isUpperCase(c) ? toLowerCase(c) : toUpperCase(c);
        }
        return result;
    }
}
