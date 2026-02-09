package ch22_enumerations.sealedpet;

import java.util.List;

/**
 * @author runningpig66
 * @date 2月9日 周一
 * @time 10:39
 * P.051 §1.17 新特性：模式匹配 §1.17.1 违反里氏替换原则
 * {NewFeature} Preview in JDK 17
 * Compile with javac flags:
 * --enable-preview --source 17
 */
// 使用 sealed 开启密封，使用 permits 指定“白名单”：只允许 Dog 和 Fish。
sealed interface Pet permits Dog, Fish {
    void feed();
}

// 必须声明为 final (到此为止)、sealed (继续密封) 或 non-sealed (放弃密封)
final class Dog implements Pet {
    @Override
    public void feed() {
    }

    void walk() {
    }
}

final class Fish implements Pet {
    @Override
    public void feed() {
    }

    void changeWater() {
    }
}

public class PetPatternMatch2 {
    static void careFor(Pet p) {
        switch (p) {
            case Dog d -> d.walk();
            case Fish f -> f.changeWater();
        }
    }

    static void petCare() {
        List.of(new Dog(), new Fish())
                .forEach(p -> careFor(p));
    }
}
