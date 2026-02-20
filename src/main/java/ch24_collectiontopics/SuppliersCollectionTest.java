package ch24_collectiontopics;

import onjava.Suppliers;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author runningpig66
 * @date 2月17日 周二
 * @time 0:55
 * P.108 §3.6 填充集合 §3.6.1 使用 Suppliers 来填充 Collection
 */
class Government implements Supplier<String> {
    static String[] foundation = (
            "strange women lying in ponds " +
                    "distributing swords is no basis " +
                    "for a system of government").split(" ");
    private int index;

    @Override
    public String get() {
        return foundation[index++];
    }
}

public class SuppliersCollectionTest {
    public static void main(String[] args) {
        // Suppliers class from the Generics chapter:
        Set<String> set = Suppliers.create(LinkedHashSet::new, new Government(), 15);
        System.out.println(set);
        List<String> list = Suppliers.create(LinkedList::new, new Government(), 15);
        System.out.println(list);
        list = new ArrayList<>();
        Suppliers.fill(list, new Government(), 15);
        System.out.println(list);

        // Or we can use Streams:
        set = Arrays.stream(Government.foundation).collect(Collectors.toSet());
        System.out.println(set);
        list = Arrays.stream(Government.foundation).collect(Collectors.toList());
        System.out.println(list);
        list = Arrays.stream(Government.foundation).collect(Collectors.toCollection(LinkedList::new));
        System.out.println(list);
        set = Arrays.stream(Government.foundation).collect(Collectors.toCollection(LinkedHashSet::new));
        System.out.println(set);
    }
}
/* Output:
[strange, women, lying, in, ponds, distributing, swords, is, no, basis, for, a, system, of, government]
[strange, women, lying, in, ponds, distributing, swords, is, no, basis, for, a, system, of, government]
[strange, women, lying, in, ponds, distributing, swords, is, no, basis, for, a, system, of, government]
[ponds, no, a, in, swords, for, is, basis, strange, system, government, distributing, of, women, lying]
[strange, women, lying, in, ponds, distributing, swords, is, no, basis, for, a, system, of, government]
[strange, women, lying, in, ponds, distributing, swords, is, no, basis, for, a, system, of, government]
[strange, women, lying, in, ponds, distributing, swords, is, no, basis, for, a, system, of, government]
 */
