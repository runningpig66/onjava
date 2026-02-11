package ch23_references;

/**
 * @author runningpig66
 * @date 2月11日 周三
 * @time 1:23
 * P.076 §2.2 创建本地副本 §2.2.9 在继承层次结构中增加可克隆性并向下覆盖
 * <p>
 * 如果你创建了一个新的类，则其基类默认是 Object，因此默认是不具备可克隆性的。只要你不显式地增加可克隆性，它就不会具备该能力。
 * 但你可以在继承层次结构中的任意一层增加该能力，而且从该层开始向下的所有层次都会具备该能力，就像这样：
 */
class Person {
}

class Hero extends Person {
}

class Scientist extends Person implements Cloneable {
    @Override
    public Scientist clone() {
        try {
            return (Scientist) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}

class MadScientist extends Scientist {
}

public class HorrorFlick {
    public static void main(String[] args) {
        Person p = new Person();
        Hero h = new Hero();
        Scientist s = new Scientist();
        MadScientist m = new MadScientist();
        //- p = (Person) p.clone(); // Compile error
        //- h = (Hero) h.clone(); // Compile error
        s = s.clone();
        m = (MadScientist) m.clone();
    }
}
