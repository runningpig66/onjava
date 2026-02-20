package ch24_collectiontopics;

import onjava.CountMap;

import java.util.LinkedHashMap;

/**
 * @author runningpig66
 * @date 2月21日 周六
 * @time 2:15
 * P.143 §3.12 理解 Map §3.12.3 LinkedHashMap
 * What you can do with a LinkedHashMap
 * <p>
 * notes: LRUCacheDemo.java
 */
public class LinkedHashMapDemo {
    public static void main(String[] args) {
        LinkedHashMap<Integer, String> linkedMap = new LinkedHashMap<>(new CountMap(9));
        System.out.println(linkedMap);
        // Least-recently-used order:
        linkedMap = new LinkedHashMap<>(16, 0.75f, true);
        linkedMap.putAll(new CountMap(9));
        System.out.println(linkedMap);
        for (int i = 0; i < 6; i++) {
            linkedMap.get(i);
        }
        System.out.println(linkedMap);
        linkedMap.get(0);
        System.out.println(linkedMap);
    }
}
/* Output:
{0=A0, 1=B0, 2=C0, 3=D0, 4=E0, 5=F0, 6=G0, 7=H0, 8=I0}
{0=A0, 1=B0, 2=C0, 3=D0, 4=E0, 5=F0, 6=G0, 7=H0, 8=I0}
{6=G0, 7=H0, 8=I0, 0=A0, 1=B0, 2=C0, 3=D0, 4=E0, 5=F0}
{6=G0, 7=H0, 8=I0, 1=B0, 2=C0, 3=D0, 4=E0, 5=F0, 0=A0}
 */
