package ch25_annotations;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author runningpig66
 * @date 2月23日 周一
 * @time 6:24
 * P.166 §4.2 编写注解处理器
 * <p>
 * 以下示例是一个非常简单的注解处理器，它读取被注解的 PasswordUtils（密码工具）类，然后利用反射来查找 @UseCase 标签。
 * 通过给定的 id 值列表，该注解列出了它找到的所有用例，并报告所有丢失的用例。
 */
public class UseCaseTracker {
    public static void trackUseCases(List<Integer> useCases, Class<?> cl) {
        // Method[] getDeclaredMethods()
        // 返回当前类中声明的所有方法，包含 public, protected, default (package), private。不包含从父类或接口继承的方法。仅关注当前类本身的定义。
        for (Method m : cl.getDeclaredMethods()) {
            // <T extends Annotation> T getAnnotation(Class<T> annotationClass)
            // 获取指定类型的注解对象。如果存在该注解，则返回由 JVM 动态代理生成的注解代理对象；如果不存在，返回 null。
            // 如果注解标记了 @Inherited，且当前元素是类（Class），它会向上递归查找父类的注解。但对于方法（Method），它通常只看当前方法。
            UseCase uc = m.getAnnotation(UseCase.class);
            if (uc != null) {
                System.out.println("Found Use Case " + uc.id() + "\n " + uc.description());
                useCases.remove(Integer.valueOf(uc.id()));
            }
        }
        useCases.forEach(i -> System.out.println("Missing use case " + i));
    }

    public static void main(String[] args) {
        List<Integer> useCases = IntStream.range(47, 51).boxed().collect(Collectors.toList());
        trackUseCases(useCases, PasswordUtils.class);
    }
}
/* Output:
Found Use Case 47
 Passwords must contain at least one numeric
Found Use Case 48
 no description
Found Use Case 49
 New passwords can't equal previously used ones
Missing use case 50
 */
