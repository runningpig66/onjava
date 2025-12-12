package ch18_strings;

/**
 * @author runningpig66
 * @date 2025/12/11 周四
 * @time 15:34
 * P.549 §18.2 重载 + 与 StringBuilder
 */
public class Concatenation {
    public static void main(String[] args) {
        String mango = "mango";
        String s = "abc" + mango + "def" + 47;
        System.out.println(s);
    }
}
/* Output:
abcmangodef47
 */
