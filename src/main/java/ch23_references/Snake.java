package ch23_references;

/**
 * @author runningpig66
 * @date 2月10日 周二
 * @time 11:56
 * P.069 §2.2 创建本地副本 §2.2.5 Object.clone() 的效果
 * Tests cloning to see if reference destinations are also cloned
 */
public class Snake implements Cloneable {
    private Snake next;
    private char c;

    // Value of i == number of segments
    public Snake(int i, char x) {
        c = x;
        if (--i > 0) {
            next = new Snake(i, (char) (x + 1));
        }
    }

    public void increment() {
        c++;
        if (next != null) {
            next.increment();
        }
    }

    @Override
    public String toString() {
        String s = ":" + c;
        if (next != null) {
            s += next.toString();
        }
        return s;
    }

    @Override
    public Snake clone() {
        try {
            return (Snake) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Snake s = new Snake(5, 'a');
        System.out.println("s = " + s);
        Snake s2 = s.clone();
        System.out.println("s2 = " + s2);
        s.increment();
        System.out.println("after s.increment, s2 = " + s2);
    }
}
/* Output:
s = :a:b:c:d:e
s2 = :a:b:c:d:e
after s.increment, s2 = :a:c:d:e:f
 */
