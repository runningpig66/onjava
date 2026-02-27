package onjava.atunit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author runningpig66
 * @date 2月27日 周五
 * @time 3:06
 * P.192 §4.4 基于注解的单元测试 §4.4.2 实现 @Unit
 * The @Unit @TestObjectCreate tag
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestObjectCreate {
}
