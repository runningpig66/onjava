package ch20_generics;

/**
 * @author runningpig66
 * @date 2025/12/25 周四
 * @time 21:51
 * P.656 §20.4 泛型方法 §20.4.2 通用 Supplier
 * <p>
 * 例如，下面是一个简单的具有无参构造器的类：CountedObject 类一直记录着已创建出的自身实例的数量，并通过 toString() 方法报告出来。
 */
public class CountedObject {
    private static long counter = 0;
    private final long id = counter++;

    public long id() {
        return id;
    }

    @Override
    public String toString() {
        return "CountedObject " + id;
    }
}
