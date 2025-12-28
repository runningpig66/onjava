package ch20_generics;

import java.util.ArrayList;
import java.util.List;

/**
 * @author runningpig66
 * @date 2025/12/28 周日
 * @time 17:25
 * P.673 §20.6 类型擦除的奥秘 §20.6.4 边界的行为
 * <p>
 * 如果我们改为创建集合而不是数组，情况便会有所不同：
 * <p>
 * 编译器并未产生警告，尽管我们知道（由于类型擦除）在 create() 内部的 new ArrayList<T>() 方法中，<T> 被擦除了————
 * 在运行时，类中并没有<T>，因此它看起来似乎并没有什么实际用处。但是如果顺着这个思路将表达式修改为 new ArrayList()，编译器会产生警告。
 * <p>
 * notes: 泛型边界行为：数组的运行时依赖与集合的编译期一致性分析.md
 */
public class ListMaker<T> {
    List<T> create() {
        return new ArrayList<>();
        // Unchecked assignment: 'java.util.ArrayList' to 'java.util.List<T>'
        // return new ArrayList();
    }

    public static void main(String[] args) {
        ListMaker<String> stringMaker = new ListMaker<>();
        List<String> stringList = stringMaker.create();
    }
}
