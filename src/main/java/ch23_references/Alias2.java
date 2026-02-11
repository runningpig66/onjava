package ch23_references;

/**
 * @author runningpig66
 * @date 2月9日 周一
 * @time 21:10
 * P.062 §2.1 传递引用 §引用别名
 * Method calls implicitly alias their arguments
 */
public class Alias2 {
    private int i;

    public Alias2(int i) {
        this.i = i;
    }

    public static void f(Alias2 reference) {
        reference.i++;
    }

    public static void main(String[] args) {
        Alias2 x = new Alias2(7);
        System.out.println("x: " + x.i);
        System.out.println("Calling f(x)");
        f(x);
        System.out.println("x: " + x.i);
    }
}
/* Output:
x: 7
Calling f(x)
x: 8
 */
