package ch22_enums;

import onjava.OSExecute;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author runningpig66
 * @date 2月3日 周二
 * @time 16:22
 * P.006 §1.4 values()方法的神秘之处
 * Analyzing enums using reflection
 */
enum Explore {HERE, THERE}

public class Reflection {
    public static Set<String> analyze(Class<?> enumClass) {
        System.out.println("_____ Analyzing " + enumClass + " _____");
        System.out.println("Interfaces:");
        for (Type t : enumClass.getGenericInterfaces()) {
            System.out.println(t);
        }
        System.out.println("Base: " + enumClass.getSuperclass());
        System.out.println("Methods: ");
        Set<String> methods = new TreeSet<>();
        for (Method m : enumClass.getMethods()) {
            methods.add(m.getName());
        }
        System.out.println(methods);
        return methods;
    }

    public static void main(String[] args) {
        Set<String> exploreMethods = analyze(Explore.class);
        Set<String> enumMethods = analyze(Enum.class);
        System.out.println("Explore.containsAll(Enum)? " + exploreMethods.containsAll(enumMethods));
        System.out.print("Explore.removeAll(Enum): ");
        exploreMethods.removeAll(enumMethods);
        System.out.println(exploreMethods);
        // Decompile the code for the enum:
        OSExecute.command("javap -cp E:/IdeaProjects/onjava/build/classes/java/main ch22_enums.Explore");
    }
}
/* Output:
_____ Analyzing class ch22_enums.Explore _____
Interfaces:
Base: class java.lang.Enum
Methods:
[compareTo, describeConstable, equals, getClass, getDeclaringClass, hashCode, name, notify, notifyAll, ordinal, toString, valueOf, values, wait]
_____ Analyzing class java.lang.Enum _____
Interfaces:
interface java.lang.constant.Constable
java.lang.Comparable<E>
interface java.io.Serializable
Base: class java.lang.Object
Methods:
[compareTo, describeConstable, equals, getClass, getDeclaringClass, hashCode, name, notify, notifyAll, ordinal, toString, valueOf, wait]
Explore.containsAll(Enum)? true
Explore.removeAll(Enum): [values]
Compiled from "Reflection.java"
final class ch22_enums.Explore extends java.lang.Enum<ch22_enums.Explore> {
  public static final ch22_enums.Explore HERE;
  public static final ch22_enums.Explore THERE;
  public static ch22_enums.Explore[] values();
  public static ch22_enums.Explore valueOf(java.lang.String);
  static {};
}
 */
