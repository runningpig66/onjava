package ch24_equalshashcode;

/**
 * @author runningpig66
 * @date 2月12日 周四
 * @time 12:03
 * P.452 §C.1 经典的 equals()
 */
public interface EqualityFactory {
    Equality make(int i, String s, double d);
}
