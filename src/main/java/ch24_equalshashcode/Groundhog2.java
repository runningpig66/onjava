package ch24_equalshashcode;

import java.util.Objects;

/**
 * @author runningpig66
 * @date 2月12日 周四
 * @time 20:07
 * P.462 §C.2 哈希和哈希码
 * A class that's used as a key in a HashMap must override hashCode() and equals()
 * <p>
 * hashCode() 方法并没有被要求一定要返回唯一的标识，但是 equals() 方法必须严格判断两个对象是否相等。
 */
public class Groundhog2 extends Groundhog {
    public Groundhog2(int n) {
        super(n);
    }

    @Override
    public int hashCode() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Groundhog2 groundhog2 &&
                Objects.equals(number, groundhog2.number);
    }
}
