package ch24_equalshashcode;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author runningpig66
 * @date 2月12日 周四
 * @time 16:13
 * P.457 §C.1 经典的 equals() §子类型之间的相等性
 * <p>
 * 在本例中，Dog 和 Pig 都哈希到了 HashSet 中的同一个桶里。此时，HashSet 会回退到使用 equals() 方法来区分对象，
 * 但是本例的 equals() 方法也认为两者相等。HashSet 不会添加 Pig 对象，因为它已经有了一个相同的对象。
 * <p>
 * 在自己实现的 hashCode() 方法中，如果只涉及单个字段，请使用 Objects.hashCode() 方法。如果涉及多个字段，请使用 Objects.hash() 方法。
 */
enum Size {SMALL, MEDIUM, LARGE}

class Animal {
    private static int counter = 0;
    private final int id = counter++;
    private final String name;
    private final Size size;

    Animal(String name, Size size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public boolean equals(Object rval) {
        return rval instanceof Animal animal &&
                // Objects.equals(id, animal.id) && // [1]
                Objects.equals(name, animal.name) &&
                Objects.equals(size, animal.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, size);
        // return Objects.hash(name, size, id); // [2]
    }

    @Override
    public String toString() {
        return String.format("%s[%d]: %s %s %x", getClass().getSimpleName(), id, name, size, hashCode());
    }
}

class Dog extends Animal {
    Dog(String name, Size size) {
        super(name, size);
    }
}

class Pig extends Animal {
    Pig(String name, Size size) {
        super(name, size);
    }
}

public class SubtypeEquality {
    public static void main(String[] args) {
        Set<Animal> pets = new HashSet<>();
        pets.add(new Dog("Ralph", Size.MEDIUM));
        pets.add(new Pig("Ralph", Size.MEDIUM));
        pets.forEach(System.out::println);
    }
}
/* Output:
Dog[0]: Ralph MEDIUM 93ee2ea0
 */
