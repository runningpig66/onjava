package ch16_validating;

import static java.lang.Character.*;

/**
 * @author runningpig66
 * @date 2025/11/20 周四
 * @time 16:38
 * P503 §16.3 测试驱动开发
 * <p>
 * 在 Inverter3 的基础上，最后实现排除不允许的字符：
 */
public class Inverter4 implements StringInverter {
    static final String ALLOWED = "abcdefghijklmnopqrstuvwxyz ,." + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Override
    public String invert(String str) {
        if (str.length() > 30) {
            throw new RuntimeException("argument too long!");
        }
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (ALLOWED.indexOf(c) == -1) {
                throw new RuntimeException(c + " Not allowed");
            }
            result += isUpperCase(c) ? toLowerCase(c) : toUpperCase(c);
        }
        return result;
    }
}
