package ch25_annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author runningpig66
 * @date 2月24日 周二
 * @time 4:49
 * P.168 §4.2 编写注解处理器 §4.2.2 默认值的限制
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SimulatingNull {
    int id() default -1;

    String description() default "";
}
