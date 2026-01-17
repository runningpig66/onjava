package ch20_generics;

import ch19_reflection.pets.Cat;
import ch19_reflection.pets.Dog;
import ch19_reflection.pets.Pet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author runningpig66
 * @date 2026/1/13 周二
 * @time 16:19
 * P.721 §20.12 动态类型安全
 * Using Collection.checkedList()
 * <p>
 * 由于你可以向 Java 5 之前的版本代码传递泛型集合，因此老式的代码仍然有可能破坏你的集合。
 * 为了解决这种情况下的类型检查问题，Java 5 在 java.util.Collections 中加入了一组实用工具：
 * checkedCollection()、checkedList()、checkedMap()、checkedSet()、checkedSortedMap() 以及 checkedSortedSet() 这几个静态方法。
 * 这些方法都将集合作为第一个参数，以进行动态检查，并将要强制确保的类型作为第二个参数。
 * <p>
 * 如果试图向 checked（经过了检查的）集合插入不匹配的对象，就会抛出 ClassCastException 异常，
 * 而泛型出现之前的（原生）集合则相反，它会在你从中取出对象时通知你出了问题。对于后一种情况，你知道出了问题，
 * 但你并不知道谁是罪魁祸首。而有了 checked 集合，你便可以找出刚才是谁在试图插入那个“坏”对象。
 * <p>
 * 运行该程序的时候，你会看到插入 Cat 的行为并未受到 dogs1 的质疑，
 * 但是 dogs2 则立刻针对插入不正确类型的行为抛出了异常。
 * 你还能看到，也可以将派生类型对象放入被检查为基类类型的 checked 集合。
 */
public class CheckedList {
    @SuppressWarnings({"rawtypes", "unchecked"})
    static void oldStyleMethod(List probablyDogs) {
        probablyDogs.add(new Cat());
    }

    public static void main(String[] args) {
        List<Dog> dogs1 = new ArrayList<>();
        oldStyleMethod(dogs1); // Quietly accepts a Cat
        List<Dog> dogs2 = Collections.checkedList(new ArrayList<>(), Dog.class);
        try {
            oldStyleMethod(dogs2); // Throws an exception
        } catch (Exception e) {
            System.out.println("Excepted: " + e);
        }
        // Derived types work fine:
        List<Pet> pets = Collections.checkedList(new ArrayList<>(), Pet.class);
        pets.add(new Dog());
        pets.add(new Cat());
    }
}
/* Output:
Excepted: java.lang.ClassCastException: Attempt to insert class ch19_reflection.pets.Cat element into collection with element type class ch19_reflection.pets.Dog
 */
