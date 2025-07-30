package ch12_collections;

import java.util.Map;

/**
 * @author runningpig66
 * @date 2025-07-28
 * @time 下午 21:49
 */
record Employee(String name, int id) {
}

public class BasicRecord {
    public static void main(String[] args) {
        Employee bob = new Employee("Bob Dobbs", 11);
        Employee dot = new Employee("Dorothy Gale", 9);
        // bob.id = 12; // Error: id has private access in Employee
        System.out.println(bob.name()); // Accessor
        System.out.println(bob.id()); // Accessor
        System.out.println(bob); // toString()
        // Employee 可以用作 Map 中的键：
        var map = Map.of(bob, "A", dot, "B");
        System.out.println(map);
    }
}
/* Output:
Bob Dobbs
11
Employee[name=Bob Dobbs, id=11]
{Employee[name=Bob Dobbs, id=11]=A, Employee[name=Dorothy Gale, id=9]=B}
*/
