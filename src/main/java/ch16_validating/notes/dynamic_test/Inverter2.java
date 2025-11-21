package ch16_validating.notes.dynamic_test;

import static java.lang.Character.*;

/**
 * @author runningpig66
 * @date 2025/11/20 周四
 * @time 22:26
 * 第二版实现：用 StringBuilder，但逻辑跟 Inverter1 一样。这里我们假设以后要在这里扩展更多功能。
 */
public class Inverter2 implements StringInverter {
    @Override
    public String invert(String str) {
        StringBuilder result = new StringBuilder(str.length());
        for (char c : str.toCharArray()) {
            // 大写 → 小写，小写 → 大写，其它字符原样
            if (isUpperCase(c)) {
                result.append(toLowerCase(c));
            } else if (isLowerCase(c)) {
                result.append(toUpperCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
