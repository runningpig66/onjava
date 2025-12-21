package onjava;

import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * @author runningpig66
 * @date 2025/12/19 周五
 * @time 15:30
 * P.612 §19.3 转型前检查 §19.3.3 递归计数
 * Counts instances of a type family
 * <p>
 * PetCounter3.Counter 中的 Map 预先加载了所有不同的 Pet 类。
 * 我们还可以使用 Class.isAssignableFrom() 方法代替 Map 的预加载，来创建一个并不仅限于对 Pet 进行计数的通用工具：
 * <p>
 * count() 方法获取其参数的 Class，并使用 isAssignableFrom() 在运行时验证传递的对象实际上在不在我们希望的层次结构里。
 * countClass() 首先对这个确切的类型进行计数，然后，如果其基类可以赋值给 baseType，则对基类进行递归调用 countClass()。
 * <p>
 * notes: TypeCounter.md
 * Java 中 instanceof、isInstance() 和 isAssignableFrom() 的区别
 */
public class TypeCounter extends HashMap<Class<?>, Integer> {
    private Class<?> baseType;

    public TypeCounter(Class<?> baseType) {
        this.baseType = baseType;
    }

    public void count(Object obj) {
        Class<?> type = obj.getClass();
        if (!baseType.isAssignableFrom(type)) {
            throw new RuntimeException(obj + " incorrect type: " + type +
                    ", should be type or subtype of " + baseType);
        }
        countClass(type);
    }

    private void countClass(Class<?> type) {
        // TODO Same thing.
        //  compute(type, (k, quantity) -> quantity == null ? 1 : quantity + 1);
        //  merge(type, 1, Integer::sum);
        Integer quantity = get(type);
        put(type, quantity == null ? 1 : quantity + 1);
        Class<?> superClass = type.getSuperclass();
        if (superClass != null && baseType.isAssignableFrom(superClass)) {
            countClass(superClass);
        }
    }

    @Override
    public String toString() {
        String result = entrySet().stream()
                .map(pair ->
                        String.format("%s=%s", pair.getKey().getSimpleName(), pair.getValue()))
                .collect(Collectors.joining(", "));
        return "{" + result + "}";
    }
}
