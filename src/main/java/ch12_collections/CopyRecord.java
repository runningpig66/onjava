package ch12_collections;

/**
 * @author runningpig66
 * @date 2025-07-29
 * @time 上午 3:02
 * {NewFeature} Since JDK 16
 */
record R(int a, double b, char c) {
}

public class CopyRecord {
    public static void main(String[] args) {
        var r1 = new R(11, 2.2, 'z');
        // 要复制一个 record, 必须显式地将所有字段都传给其构造器：
        var r2 = new R(r1.a(), r1.b(), r1.c());
        System.out.println(r1.equals(r2));
    }
}
/* Output:
true
*/
