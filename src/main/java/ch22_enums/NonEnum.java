package ch22_enums;

/**
 * @author runningpig66
 * @date 2月3日 周二
 * @time 17:28
 * P.008 §1.4 values()方法的神秘之处
 */
public class NonEnum {
    public static void main(String[] args) {
        Class<Integer> intClass = Integer.class;
        try {
            for (Object en : intClass.getEnumConstants()) {
                System.out.println(en);
            }
        } catch (Exception e) {
            System.out.println("Expected: " + e);
        }
    }
}
/* Output:
Expected: java.lang.NullPointerException: Cannot read the array length because "<local2>" is null
 */
