package ch24_collectiontopics;

import onjava.Countries;

import java.util.*;

/**
 * @author runningpig66
 * @date 2月22日 周日
 * @time 3:21
 * P.148 §3.13 工具函数 §3.13.2 创建不可修改的 Collection 或 Map
 * Using the Collections.unmodifiable methods
 * <p>
 * 不管是哪一种情况，在将集合变为只读之前，都必须用有意义的数据填充这个集合。一旦它被加载，
 * 最好的方法就是用“不可修改”调用所产生的引用来替换现有的引用。一旦使其成为只读的，
 * 我们就不用担心有不小心修改集合内容的风险了。此外，这个工具也允许我们把一个可修改的集合当成某个类中的 private 成员，
 * 然后从方法调用返回一个指向该集合的只读引用。因此，我们可以在这个类内修改它，但是其他人只能读取。
 */
public class ReadOnly {
    static Collection<String> data = new ArrayList<>(Countries.names(6));

    public static void main(String[] args) {
        // <T> Collection<T> unmodifiableCollection(Collection<? extends T> c)
        Collection<String> c = Collections.unmodifiableCollection(new ArrayList<>(data));
        System.out.println(c); // Reading is OK
        //- c.add("one"); // Can't change it

        // <T> List<T> unmodifiableList(List<? extends T> list)
        List<String> a = Collections.unmodifiableList(new ArrayList<>(data));
        ListIterator<String> lit = a.listIterator();
        System.out.println(lit.next()); // Reading is OK
        //- lit.add("one"); // Can't change it

        // <T> Set<T> unmodifiableSet(Set<? extends T> s)
        Set<String> s = Collections.unmodifiableSet(new HashSet<>(data));
        System.out.println(s); // Reading is OK
        //- s.add("one"); // Can't change it

        // For a SortedSet:
        Set<String> ss = Collections.unmodifiableSortedSet(new TreeSet<>(data));

        // <K,V> Map<K,V> unmodifiableMap(Map<? extends K, ? extends V> m)
        Map<String, String> m = Collections.unmodifiableMap(new HashMap<>(Countries.capitals(6)));
        System.out.println(m); // Reading is OK
        //- m.put("Ralph", "Howdy!");

        // For a SortedMap:
        Map<String, String> sm = Collections.unmodifiableSortedMap(new TreeMap<>(Countries.capitals(6)));
    }
}
/* Output:
[ALGERIA, ANGOLA, BENIN, BOTSWANA, BURKINA FASO, BURUNDI]
ALGERIA
[BENIN, BOTSWANA, ANGOLA, BURKINA FASO, ALGERIA, BURUNDI]
{BENIN=Porto-Novo, BOTSWANA=Gaberone, ANGOLA=Luanda, BURKINA FASO=Ouagadougou, ALGERIA=Algiers, BURUNDI=Bujumbura}
 */
