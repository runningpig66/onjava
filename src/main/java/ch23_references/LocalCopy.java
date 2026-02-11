package ch23_references;

/**
 * @author runningpig66
 * @date 2月10日 周二
 * @time 8:35
 * P.067 §2.2 创建本地副本 §2.2.4 成功的克隆
 * Creating local copies with clone()
 * <p>
 * 一旦你理解了实现 clone() 方法的细节，就能够创建出可轻松复制的类，以提供生成本地副本的能力了：
 */
class Duplo implements Cloneable {
    private int n;

    Duplo(int n) {
        this.n = n;
    }

    @Override
    public Duplo clone() { // [1]
        try {
            return (Duplo) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public int getValue() {
        return n;
    }

    public void setValue(int n) {
        this.n = n;
    }

    public void increment() {
        n++;
    }

    @Override
    public String toString() {
        return Integer.toString(n);
    }
}

public class LocalCopy {
    public static Duplo g(Duplo v) {
        // Passing a reference, modifies outside object:
        v.increment();
        return v;
    }

    public static Duplo f(Duplo v) {
        v = v.clone(); // Local copy // [2]
        v.increment();
        return v;
    }

    public static void main(String[] args) {
        Duplo a = new Duplo(11);
        Duplo b = g(a);
        // Reference equivalence, not object equivalence:
        System.out.println("a == b: " + (a == b) + "\na = " + a + "\nb = " + b);

        Duplo c = new Duplo(47);
        Duplo d = f(c);
        System.out.println("c == d: " + (c == d) + "\nc = " + c + "\nd = " + d);
    }
}
/* Output:
a == b: true
a = 12
b = 12
c == d: false
c = 47
d = 48
 */
