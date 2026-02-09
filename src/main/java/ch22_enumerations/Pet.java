package ch22_enumerations;

/**
 * @author runningpig66
 * @date 2月9日 周一
 * @time 10:09
 * P.050 §1.17 新特性：模式匹配 §1.17.1 违反里氏替换原则
 */
public class Pet {
    void feed() {
    }
}

class Dog extends Pet {
    void walk() {
    }
}

class Fish extends Pet {
    void changeWater() {
    }
}
