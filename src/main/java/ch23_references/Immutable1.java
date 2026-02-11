package ch23_references;

/**
 * @author runningpig66
 * @date 2月11日 周三
 * @time 22:24
 * P.088 §2.4 不可变类 §2.4.1 创建不可变类
 * Immutable objects are immune to aliasing
 * <p>
 * 下面是一种创建自有的不可变类的方法：
 */
public class Immutable1 {
    private int data;

    public Immutable1(int initVal) {
        data = initVal;
    }

    public int read() {
        return data;
    }

    public boolean nonzero() {
        return data != 0;
    }

    public Immutable1 multiply(int multiplier) {
        return new Immutable1(data * multiplier);
    }

    public static void f(Immutable1 i1) {
        Immutable1 quad = i1.multiply(4);
        System.out.println("i1 = " + i1.read());
        System.out.println("quad = " + quad.read());
    }

    public static void main(String[] args) {
        Immutable1 x = new Immutable1(47);
        System.out.println("x = " + x.read());
        f(x);
        System.out.println("x = " + x.read());
    }
}
/* Output:
x = 47
i1 = 47
quad = 188
x = 47
 */
