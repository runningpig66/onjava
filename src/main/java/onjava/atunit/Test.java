package onjava.atunit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author runningpig66
 * @date 2月23日 周一
 * @time 4:41
 * P.164 §4.1 基本语法 §4.1.1 定义注解
 * The @Test tag
 * <p>
 * 除了 @ 符号外，@Test 的定义非常像一个空接口。注解的定义也要求必须有元注解 @Target 和 @Retention。
 * @Target 定义了你可以在何处应用该注解（例如方法或字段）。
 * @Retention 定义了该注解在源代码（SOURCE）、类文件（CLASS）或运行时（RUNTIME）中是否可用。
 * 注解通常包含一些可以设定值的元素。程序或工具在处理注解时可以使用这些参数。元素看起来比较像接口方法，只不过你可以为其指定默认值。
 * 没有任何元素的注解（如下面的 @Test）称为标记注解。
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Test {
}
