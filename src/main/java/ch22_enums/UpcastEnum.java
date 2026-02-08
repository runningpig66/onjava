package ch22_enums;

/**
 * @author runningpig66
 * @date 2月3日 周二
 * @time 17:15
 * P.008 §1.4 values()方法的神秘之处
 * No values() method if you upcast an enum
 */
enum Search {HITHER, YON}

public class UpcastEnum {
    public static void main(String[] args) {
        Search[] vals = Search.values();
        Enum e = Search.HITHER;
        // e.values(); // No values() in Enum
        for (Enum en : e.getClass().getEnumConstants()) {
            System.out.println(en);
        }
    }
}
/* Output:
HITHER
YON
 */
