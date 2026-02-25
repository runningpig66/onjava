package ch25_annotations.ifx;

/**
 * @author runningpig66
 * @date 2月25日 周三
 * @time 4:29
 * P.179 §4.3 用 javac 处理注解 §4.3.2 更复杂的处理器
 * javac-based annotation processing
 */
@ExtractInterface(interfaceName = "IMultiplier")
public class Multiplier {
    public boolean flag = false;
    private int n = 0;

    public int multiply(int x, int y) {
        int total = 0;
        for (int i = 0; i < x; i++) {
            total = add(total, y);
        }
        return total;
    }

    public int fortySeven() {
        return 47;
    }

    private int add(int x, int y) {
        return x + y;
    }

    public double timesTen(double arg) {
        return arg * 10;
    }

    public static void main(String[] args) {
        Multiplier m = new Multiplier();
        System.out.println("11 * 16 = " + m.multiply(11, 16));
    }
}
/* Output:
11 * 16 = 176
 */
