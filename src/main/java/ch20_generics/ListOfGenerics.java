package ch20_generics;

import java.util.ArrayList;
import java.util.List;

/**
 * @author runningpig66
 * @date 2025/12/29 周一
 * @time 23:48
 * P.680 §20.7 对类型擦除的补偿 §20.7.2 泛型数组
 * <p>
 * 如你在 Erased.java 中所见，你是无法创建泛型数组的。通用的解决方案是不管在何处，你都用 ArrayList 来创建泛型数组：
 * 这样你就获得了数组的行为，此外还得到了泛型提供的编译时类型安全性。
 */
public class ListOfGenerics<T> {
    private List<T> array = new ArrayList<>();

    public void add(T item) {
        array.add(item);
    }

    public T get(int index) {
        return array.get(index);
    }
}
