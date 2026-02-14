package ch24_equalshashcode;

/**
 * @author runningpig66
 * @date 2月12日 周四
 * @time 17:50
 * P.460 §C.2 哈希和哈希码
 * Looks plausible, but doesn't work as a HashMap key
 */
public class Groundhog {
    protected int number;

    public Groundhog(int n) {
        number = n;
    }

    @Override
    public String toString() {
        return "Groundhog #" + number;
    }
}
