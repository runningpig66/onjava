package ch23_references;

/**
 * @author runningpig66
 * @date 2月9日 周一
 * @time 21:05
 * P.061 §2.1 传递引用 §引用别名
 * Aliasing two references to one object
 */
public class Alias1 {
    private int i;

    public Alias1(int ii) {
        i = ii;
    }

    public static void main(String[] args) {
        Alias1 x = new Alias1(7);
        Alias1 y = x; // Assign the reference    // [1]
        System.out.println("x: " + x.i);
        System.out.println("y: " + y.i);
        System.out.println("Incrementing x");
        x.i++;                                   // [2]
        System.out.println("x: " + x.i);
        System.out.println("y: " + y.i);
    }
}
/* Output:
x: 7
y: 7
Incrementing x
x: 8
y: 8
 */
