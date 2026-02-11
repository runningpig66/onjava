package ch23_references;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author runningpig66
 * @date 2月9日 周一
 * @time 21:28
 * P.064 §2.2 创建本地副本 §2.2.2 克隆对象
 * The clone() operation works for only a few items in the standard Java library
 * <p>
 * clone() 方法生成了一个 Object，然后该 Object 必须被转换为合适的类型。
 * 本例演示了 ArrayList 的 clone() 方法如何不去自动尝试克隆 ArrayList 中的每个对象——
 * 原有的 ArrayList 和克隆的 ArrayList 是两个独立的对象，但它们内部包含的元素是指向同一个对象的不同引用别名。
 */
class Int {
    private int i;

    Int(int ii) {
        i = ii;
    }

    public void increment() {
        i++;
    }

    @Override
    public String toString() {
        return Integer.toString(i);
    }
}

public class CloneArrayList {
    public static void main(String[] args) {
        ArrayList<Int> v = IntStream.range(0, 10)
                .mapToObj(Int::new)
                .collect(Collectors.toCollection(ArrayList::new));
        System.out.println("v: " + v);
        @SuppressWarnings("unchecked")
        // 这里是浅拷贝 (Shallow Copy)，clone() 操作会创建一个新的 ArrayList 实例，v 和 v2 是堆内存中两个独立的 ArrayList 对象。
        // 但是，clone() 不会递归地复制列表中的元素。因此，两个列表内部存储的是指向同一组对象的引用。
        ArrayList<Int> v2 = (ArrayList<Int>) v.clone();
        // 因为元素是共享的（别名现象），此操作产生的副作用对 v 也是可见的。
        // 修改克隆列表的结构（如添加或删除元素）不会影响原列表，但修改列表中共享元素的内部状态会同时影响两个列表。
        // Increment all v2's elements:
        v2.forEach(Int::increment);
        // See if it changed v's elements:
        System.out.println("v: " + v);

        // 验证容器的独立性：清空 v2 列表是一个结构性修改。因为 v 和 v2 是两个独立的容器对象，所以 v 的结构（元素数量）不受影响。
        v2.clear();
        System.out.println("v2 size: " + v2.size());
        System.out.println("v  size: " + v.size());
    }
}
/* Output:
v: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
v: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
v2 size: 0
v  size: 10
 */
