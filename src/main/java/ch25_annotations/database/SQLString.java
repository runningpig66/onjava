package ch25_annotations.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author runningpig66
 * @date 2月24日 周二
 * @time 5:05
 * P.169 §4.2 编写注解处理器 §4.2.3 生成外部文件
 * <p>
 * 以下示例是表字段的注解：
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SQLString {
    int value() default 0;

    String name() default "";

    // 这里并没有把 primaryKey 等属性再写一遍，而是直接引用了 @Constraints
    Constraints constraints() default @Constraints;
}
