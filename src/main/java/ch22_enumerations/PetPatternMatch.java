package ch22_enumerations;

import java.util.List;

/**
 * @author runningpig66
 * @date 2月9日 周一
 * @time 10:13
 * P.050 §1.17 新特性：模式匹配 §1.17.1 违反里氏替换原则
 * {NewFeature} Preview in JDK 17
 * Compile with javac flags:
 * --enable-preview --source 17
 * <p>
 * 有了模式匹配，就可以通过为每种可能的类型进行检查并编写不同的代码，来处理 Pet 继承层次结构的非里氏替换原则的性质：
 */
public class PetPatternMatch {
    static void careFor(Pet p) {
        switch (p) {
            // 自动判断类型 + 自动强转 + 绑定变量
            case Dog d -> d.walk();
            case Fish f -> f.changeWater();
            case Pet sp -> sp.feed();
        }
    }

    static void petCare() {
        List.of(new Dog(), new Fish())
                .forEach(p -> careFor(p));
    }
}
