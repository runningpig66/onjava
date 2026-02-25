package ch25_annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author runningpig66
 * @date 2月23日 周一
 * @time 5:43
 * P.164 §4.1 基本语法 §4.1.1 定义注解
 * <p>
 * 下面是一个用于跟踪某项目中用例的简单注解，程序员会给某个特定用例所需的所有方法或方法集都加上注解。
 * 项目经理可以通过计算已实现的用例数来了解项目的进度，维护项目的开发人员可以轻松地找到需要更新的用例，或者在系统内调试业务规则。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UseCase {
    int id();

    // 在注解中：default 关键字仅仅是为了提供一个默认的数据值（Fallback Value）。
    String description() default "no description";
}
