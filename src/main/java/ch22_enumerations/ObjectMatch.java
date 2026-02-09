package ch22_enumerations;

import java.util.List;

/**
 * @author runningpig66
 * @date 2月9日 周一
 * @time 11:02
 * P.052 §1.17 新特性：模式匹配 §1.17.1 违反里氏替换原则
 * {NewFeature} Preview in JDK 17
 * Compile with javac flags:
 * --enable-preview --source 17
 * Run with java flag: --enable-preview
 * <p>
 * 模式匹配不会像继承多态性那样将你约束在单一继承层次结构中——你可以匹配任意类型。想要这样做，就需要将 Object 传入 switch：
 */
record XX() {
}

public class ObjectMatch {
    static String match(Object o) {
        return switch (o) {
            case Dog d -> "Walk the dog";
            case Fish f -> "Change the fish water";
            case Pet sp -> "Not dog or fish";
            case String s -> "String " + s;
            case Integer i -> "Integer " + i;
            case String[] sa -> String.join(", ", sa);
            case XX xx -> "only XX: " + xx;
            case null -> "null only: " + null;
            default -> "Something else";
        };
    }

    public static void main(String[] args) {
        List.of(new Dog(), new Fish(), new Pet(),
                        "Oscar", Integer.valueOf(12), Double.valueOf("47.74"),
                        new String[]{"to", "the", "point"}, new XX())
                .forEach(p -> System.out.println(match(p)));
    }
}
/* Output:
Walk the dog
Change the fish water
Not dog or fish
String Oscar
Integer 12
Something else
to, the, point
only XX: XX[]
 */
