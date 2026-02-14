package ch24_equalshashcode;

/**
 * @author runningpig66
 * @date 2月15日 周日
 * @time 0:59
 * P.469 §C.2 哈希和哈希码 §C.2.3 重写 hashCode()
 */
public class StringHashCode {
    public static void main(String[] args) {
        String[] hellos = "Hello Hello".split(" ");
        System.out.println(hellos[0].hashCode());
        System.out.println(hellos[1].hashCode());
    }
}
/* Output:
69609650
69609650
 */
