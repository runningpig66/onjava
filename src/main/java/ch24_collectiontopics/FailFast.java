package ch24_collectiontopics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

/**
 * @author runningpig66
 * @date 2月22日 周日
 * @time 4:21
 * P.151 §3.13 工具函数 §3.13.3 同步 Collection 或 Map
 * Demonstrates the "fail-fast" behavior
 * <p>
 * 下面是一个非常基础的对快速失败机制的演示。我们创建了一个迭代器，并在这个迭代器所指向的集合中添加一个元素，就像这样：
 * <p>
 * 之所以出现这个异常，是因为在获取了迭代器之后，又尝试向其中放入一个元素。尽管这个程序中没有并发，但是它演示了程序的两个部分可能会如何修改同一集合。
 * 这会导致一种不确定的状态，所以这个异常就是通知我们修改自己的代码：在这种情况下，要先把所有的元素都添加到集合中，然后再获取迭代器。
 * ConcurrentHashMap、CopyOnWriteArrayList 和 CopyOnWriteArraySet 都使用了可以避免 ConcurrentModificationException 的技术。
 */
public class FailFast {
    public static void main(String[] args) {
        Collection<String> c = new ArrayList<>();
        Iterator<String> it = c.iterator();
        c.add("An object");
        try {
            String s = it.next();
        } catch (ConcurrentModificationException e) {
            System.out.println(e);
        }
    }
}
/* Output:
java.util.ConcurrentModificationException
 */
