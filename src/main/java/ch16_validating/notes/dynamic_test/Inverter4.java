package ch16_validating.notes.dynamic_test;

import static java.lang.Character.*;

/**
 * @author runningpig66
 * @date 2025/11/20 周四
 * @time 22:31
 * 第四版实现：用 Java 8 流来改写（只是换个写法）。
 */
public class Inverter4 implements StringInverter {
    @Override
    public String invert(String str) {
        StringBuilder result = new StringBuilder();
        str.chars().forEach(codePoint -> {
            char c = (char) codePoint;
            // 大写 → 小写，小写 → 大写，其它字符原样
            if (isUpperCase(c)) {
                result.append(toLowerCase(c));
            } else if (isLowerCase(c)) {
                result.append(toUpperCase(c));
            } else {
                result.append(c);
            }
        });
        return result.toString();
    }
}
