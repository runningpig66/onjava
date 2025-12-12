package ch18_strings;

/**
 * @author runningpig66
 * @date 2025/12/11 周四
 * @time 16:10
 * P.550 §18.2 重载 + 与 StringBuilder
 */
public class WhitherStringBuilder {
    public String implicit(String[] fields) {
        String result = "";
        for (String field : fields) {
            result += field;
        }
        return result;
    }

    public String explicit(String[] fields) {
        StringBuilder result = new StringBuilder();
        for (String field : fields) {
            result.append(field);
        }
        return result.toString();
    }
}
