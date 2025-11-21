package ch16_validating.notes.dynamic_test;

/**
 * @author runningpig66
 * @date 2025/11/20 周四
 * @time 21:06
 * 第一版实现：逐个字符判断、修改大小写。
 */
public class Inverter1 implements StringInverter {
    @Override
    public String invert(String str) {
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            // 大写 → 小写，小写 → 大写，其它字符原样
            if (Character.isUpperCase(c)) {
                result += Character.toLowerCase(c);
            } else if (Character.isLowerCase(c)) {
                result += Character.toUpperCase(c);
            } else {
                result += c;
            }
        }
        return result;
    }
}
