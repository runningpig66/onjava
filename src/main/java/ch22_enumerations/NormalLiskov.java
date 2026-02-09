package ch22_enumerations;

import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2月9日 周一
 * @time 9:30
 * P.049 §1.17 新特性：模式匹配 §1.17.1 违反里氏替换原则
 * <p>
 * 如果感兴趣的类型全都具有共同的基类，并且只会使用公共基类中定义的方法，那么就遵循了里氏替换原则（Liskov Substitution Principle, LSP）。
 * 在这种情况下，模式匹配就不是必需的了——只需使用普通的继承多态性即可。
 */
interface LifeForm {
    String move();

    String react();
}

class Worm implements LifeForm {
    @Override
    public String move() {
        return "Worm::move()";
    }

    @Override
    public String react() {
        return "Worm::react()";
    }
}

class Giraffe implements LifeForm {
    @Override
    public String move() {
        return "Giraffe::move()";
    }

    @Override
    public String react() {
        return "Giraffe::react()";
    }
}

public class NormalLiskov {
    public static void main(String[] args) {
        Stream.of(new Worm(), new Giraffe())
                .forEach(lf -> System.out.println(lf.move() + " " + lf.react()));
    }
}
/* Output:
Worm::move() Worm::react()
Giraffe::move() Giraffe::react()
 */
