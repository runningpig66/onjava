package ch24_collectiontopics;

import onjava.Countries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

/**
 * @author runningpig66
 * @date 2月23日 周一
 * @time 1:38
 * P.156 §3.15 Java 1.0/1.1 的集合类 §3.15.1 Vector 和 Enumeration
 * Java 1.0/1.1 Vector and Enumeration
 */
public class Enumerations {
    public static void main(String[] args) {
        Vector<String> v = new Vector<>(Countries.names(10));
        Enumeration<String> e = v.elements();
        while (e.hasMoreElements()) {
            System.out.print(e.nextElement() + ", ");
        }
        // Produce an Enumeration from a Collection:
        e = Collections.enumeration(new ArrayList<>());
    }
}
/* Output:
ALGERIA, ANGOLA, BENIN, BOTSWANA, BURKINA FASO, BURUNDI, CAMEROON, CAPE VERDE, CENTRAL AFRICAN REPUBLIC, CHAD,
 */
