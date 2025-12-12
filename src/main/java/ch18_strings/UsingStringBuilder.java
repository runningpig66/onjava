package ch18_strings;

import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author runningpig66
 * @date 2025/12/11 周四
 * @time 16:34
 * P.552 §18.2 重载 + 与 StringBuilder
 * <p>
 * 因此，当创建 toString() 方法时，如果操作很简单，通常可以依赖编译器，让它以合理的方式自行构建结果。
 * 但是如果涉及循环，并且对性能也有一定要求，那就需要在 toString() 中显式使用 StringBuilder 了，如下所示：
 */
public class UsingStringBuilder {
    public static String string1() {
        Random rand = new Random(47);
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < 25; i++) {
            result.append(rand.nextInt(100));
            result.append(", ");
        }
        // 注意，在添加右方括号之前，可以调用 delete() 来删除最后一个逗号和空格。
        result.delete(result.length() - 2, result.length());
        result.append("]");
        return result.toString();
    }

    public static String string2() {
        String result = new Random(47)
                .ints(25, 0, 100)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(", "));
        return "[" + result + "]";
    }

    public static void main(String[] args) {
        System.out.println(string1());
        System.out.println(string2());
    }
}
/* Output:
[58, 55, 93, 61, 61, 29, 68, 0, 22, 7, 88, 28, 51, 89, 9, 78, 98, 61, 20, 58, 16, 40, 11, 22, 4]
[58, 55, 93, 61, 61, 29, 68, 0, 22, 7, 88, 28, 51, 89, 9, 78, 98, 61, 20, 58, 16, 40, 11, 22, 4]
 */
