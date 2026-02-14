package ch24_equalshashcode;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author runningpig66
 * @date 2月12日 周四
 * @time 17:56
 * P.460 §C.2 哈希和哈希码
 * What will the weather be?
 * <p>
 * 本类用于演示在 HashMap 中使用未重写 hashCode() 和 equals() 方法的自定义类作为 Key 时导致的查找失效现象。
 * Object 类的默认 hashCode() 方法通常基于对象的内存地址或随机数生成（即“身份哈希”），而默认的 equals() 方法仅执行引用地址的比较（物理等价性）。
 * 因此，即使两个 Groundhog 实例拥有相同的 number 字段值，它们在 JVM 看来依然是两个完全不相关的对象。当在 Map 中执行查找时，
 * 系统首先根据 Key 的哈希值定位哈希桶。由于新实例与存入实例的内存地址不同，其哈希码通常不同，导致直接定位到错误的桶位而查找失败。
 * 即便发生哈希碰撞定位到了正确的桶，后续的 equals() 比较也会因引用地址不同而返回 false。要实现基于业务逻辑的查找（逻辑等价性），
 * 必须同时重写 hashCode() 以确保相同逻辑对象映射到同一哈希槽，并重写 equals() 以比较关键字段的值。
 */
public class SpringDetector {
    public static <T extends Groundhog> void detectSpring(Class<T> type) {
        try {
            Constructor<T> ghog = type.getConstructor(int.class);
            Map<Groundhog, Prediction> map = IntStream.range(0, 10)
                    .mapToObj(i -> {
                        try {
                            return ghog.newInstance(i);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toMap(Function.identity(), gh -> new Prediction()));
            map.forEach((k, v) -> System.out.println(k + ": " + v));
            Groundhog gh = ghog.newInstance(3);
            System.out.println("Looking up prediction for " + gh);
            if (map.containsKey(gh)) {
                System.out.println(map.get(gh));
            } else {
                System.out.println("Key not found: " + gh);
            }
        } catch (NoSuchMethodException |
                 IllegalAccessException |
                 InvocationTargetException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        detectSpring(Groundhog.class);
    }
}
/* Output:
Groundhog #7: Six more weeks of Winter!
Groundhog #3: Early Spring!
Groundhog #4: Six more weeks of Winter!
Groundhog #2: Early Spring!
Groundhog #1: Early Spring!
Groundhog #8: Six more weeks of Winter!
Groundhog #6: Early Spring!
Groundhog #0: Early Spring!
Groundhog #5: Six more weeks of Winter!
Groundhog #9: Six more weeks of Winter!
Looking up prediction for Groundhog #3
Key not found: Groundhog #3
 */
