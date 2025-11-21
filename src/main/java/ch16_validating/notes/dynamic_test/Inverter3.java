package ch16_validating.notes.dynamic_test;

import static java.lang.Character.isLowerCase;
import static java.lang.Character.toUpperCase;

/**
 * @author runningpig66
 * @date 2025/11/20 周四
 * @time 22:29
 * 第三版实现：故意留个 bug —— 只处理小写 → 大写，大写保持不变（错误实现，用来验证测试是否能抓出来）。
 */
public class Inverter3 implements StringInverter {
    @Override
    public String invert(String str) {
        StringBuilder result = new StringBuilder(str.length());
        for (char c : str.toCharArray()) {
            // 只处理小写 → 大写
            if (isLowerCase(c)) {
                result.append(toUpperCase(c));
            } else {
                // BUG: 大写不反转
                result.append(c);
            }
        }
        return result.toString();
    }
}
