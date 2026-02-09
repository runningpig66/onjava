package ch22_enumerations;

/**
 * @author runningpig66
 * @date 2月9日 周一
 * @time 2:19
 * P.047 §1.16 新特性：智能转型
 * {NewFeature} Since JDK 16
 */
public class SmartCasting {
    static void dumb(Object x) {
        if (x instanceof String) {
            String s = (String) x;
            if (s.length() > 0) {
                System.out.format("%d %s%n", s.length(), s.toUpperCase());
            }
        }
    }

    static void smart(Object x) {
        if (x instanceof String s && s.length() > 0) {
            System.out.format("%d %s%n", s.length(), s.toUpperCase());
        }
    }

    static void wrong(Object x) {
        // "Or" never works:
        //- if (x instanceof String s || s.length() > 0) {}
        // error: cannot find symbol     ^
    }

    public static void main(String[] args) {
        dumb("dumb");
        smart("smart");
    }
}
/* Output:
4 DUMB
5 SMART
 */
