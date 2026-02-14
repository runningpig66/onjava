package ch24_equalshashcode;

import java.util.HashSet;
import java.util.Set;

/**
 * @author runningpig66
 * @date 2月12日 周四
 * @time 17:18
 * P.459 §C.1 经典的 equals() §子类型之间的相等性
 */
class Dog2 extends Animal {
    Dog2(String name, Size size) {
        super(name, size);
    }

    @Override
    public boolean equals(Object rval) {
        return rval instanceof Dog2 &&
                super.equals(rval);
    }
}

class Pig2 extends Animal {
    Pig2(String name, Size size) {
        super(name, size);
    }

    @Override
    public boolean equals(Object rval) {
        return rval instanceof Pig2 &&
                super.equals(rval);
    }
}

public class SubtypeEquality2 {
    public static void main(String[] args) {
        Set<Animal> pets = new HashSet<>();
        pets.add(new Dog2("Ralph", Size.MEDIUM));
        pets.add(new Pig2("Ralph", Size.MEDIUM));
        pets.forEach(System.out::println);
    }
}
/* Output:
Dog2[0]: Ralph MEDIUM 93ee2ea0
Pig2[1]: Ralph MEDIUM 93ee2ea0
 */
