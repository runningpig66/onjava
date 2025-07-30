package ch12_collections;

import java.util.*;

/**
 * @author runningpig66
 * @date 25/07/24/周四
 * @time 上午 1:13
 */
public class AddingGroups {
    public static void main(String[] args) {
        Collection<Integer> collection =
                new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        Integer[] moreInts = {6, 7, 8, 9, 10};
        collection.addAll(Arrays.asList(moreInts));
        // 运行快很多，但是我们无法以这种方式构建 Collection:
        Collections.addAll(collection, 11, 12, 13, 14, 15);
        Collections.addAll(collection, moreInts);
        // 生成一个底层为数组的列表：
        List<Integer> list = Arrays.asList(16, 17, 18, 19, 20);
        list.set(1, 99); // 修改元素 OK
        list.add(21); // 运行时错误；底层的数据不能调节大小
    }
}
