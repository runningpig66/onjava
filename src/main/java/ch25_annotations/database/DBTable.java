package ch25_annotations.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author runningpig66
 * @date 2月24日 周二
 * @time 4:57
 * P.168 §4.2 编写注解处理器 §4.2.3 生成外部文件
 * <p>
 * 以下示例是一个注解，它会让注解处理器创建一个数据库表：
 */
@Target(ElementType.TYPE) // Applies to classes only
@Retention(RetentionPolicy.RUNTIME)
public @interface DBTable {
    String name() default "";
}
