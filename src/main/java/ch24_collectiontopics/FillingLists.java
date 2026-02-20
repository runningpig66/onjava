package ch24_collectiontopics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author runningpig66
 * @date 2月17日 周二
 * @time 0:30
 * P.107 §3.6 填充集合
 * Collections.fill() & Collections.nCopies()
 */
class StringAddress {
    private String s;

    StringAddress(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return super.toString() + " " + s;
    }
}

public class FillingLists {
    public static void main(String[] args) {
        // Collections.nCopies(int n, T o)：用于集合的快速批量初始化。
        // 该方法会返回一个指定长度为 n、且包含同一对象 o 引用的不可变列表（Immutable List）。
        // 由于返回的列表不支持任何修改操作（add/remove 均会抛出异常），
        // 在实际工程中，通常将其作为数据源（视图）传递给 ArrayList 的构造器，
        // 从而安全、快速地实例化出一个具备初始填充数据的可变集合。
        List<StringAddress> list = new ArrayList<>(
                Collections.nCopies(4, new StringAddress("Hello"))
        );
        System.out.println(list);
        // Collections.fill(List<? super T> list, T obj)：用于现有集合元素的全量覆盖。
        // 该方法会使用指定的 obj 对象引用，替换目标列表中【所有已存在】的元素。
        // 核心限制：fill() 方法严格依赖并受限于列表当前的 size()。
        // 它仅执行覆写操作，绝不会改变列表的物理结构或进行扩容。
        // 若对一个空列表（size == 0）调用此方法，将不会产生任何效果。
        Collections.fill(list, new StringAddress("World!"));
        System.out.println(list);
    }
}
/* Output:
[ch24_collectiontopics.StringAddress@54bedef2 Hello, ch24_collectiontopics.StringAddress@54bedef2 Hello, ch24_collectiontopics.StringAddress@54bedef2 Hello, ch24_collectiontopics.StringAddress@54bedef2 Hello]
[ch24_collectiontopics.StringAddress@6ce253f1 World!, ch24_collectiontopics.StringAddress@6ce253f1 World!, ch24_collectiontopics.StringAddress@6ce253f1 World!, ch24_collectiontopics.StringAddress@6ce253f1 World!]
 */
