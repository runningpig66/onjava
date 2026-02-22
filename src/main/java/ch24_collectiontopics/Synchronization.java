package ch24_collectiontopics;

import java.util.*;

/**
 * @author runningpig66
 * @date 2月22日 周日
 * @time 4:04
 * P.150 §3.13 工具函数 §3.13.3 同步 Collection 或 Map
 * Using the Collections.synchronized methods
 * <p>
 * synchronized 关键字是多线程主题的一个重要部分。多线程主题更为复杂，本书第 5 章会介绍。
 * 这里只是要指出，Collections 包含一种自动同步整个集合的方式，其语法与不可修改的方法类似：
 * <p>
 * 最好是直接通过恰当的“同步”（synchronized）方法来传递新创建的集合，如以下代码所示。
 * 这样一来，我们就不会意外地将非同步的版本暴露出去了。
 */
public class Synchronization {
    public static void main(String[] args) {
        Collection<String> c = Collections.synchronizedCollection(new ArrayList<>());

        List<String> list = Collections.synchronizedList(new ArrayList<>());

        Set<String> s = Collections.synchronizedSet(new HashSet<>());

        Set<String> ss = Collections.synchronizedSortedSet(new TreeSet<>());

        Map<String, String> m = Collections.synchronizedMap(new HashMap<>());

        Map<String, String> sm = Collections.synchronizedSortedMap(new TreeMap<>());
    }
}
