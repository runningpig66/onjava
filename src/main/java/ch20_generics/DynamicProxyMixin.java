package ch20_generics;

import onjava.Tuple2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import static onjava.Tuple.tuple;

/**
 * @author runningpig66
 * @date 2026/1/17 周六
 * @time 18:22
 * P.729 §20.14 混型 §20.14.4 与动态代理混合
 * <p>
 * 可以用动态代理来创建一种比装饰器更接近于现代混型的机制（见第 19 章关于 Java 动态代理运行机制的解释）。
 * 如果使用了动态代理，结果类的动态类型就是被混合后的合并类型。由于动态代理的限制，每个被混入的类都必须是某个接口的实现：
 * <p>
 * 因为只有动态类型包含了所有的混入类型，而静态类型并没有，所以这种方式仍然不如 C++ 的优秀，
 * 因为在调用方法之前，要强制向下转型为合适的类型。不过，这明显更接近于真正的混型了。
 */
class MixinProxy implements InvocationHandler {
    Map<String, Object> delegatesByMethod;

    @SafeVarargs
    MixinProxy(Tuple2<Object, Class<?>>... pairs) {
        delegatesByMethod = new HashMap<>();
        for (Tuple2<Object, Class<?>> pair : pairs) {
            for (Method method : pair.a2.getMethods()) {
                String methodName = method.getName();
                // The first interface in the map implements the method.
                if (!delegatesByMethod.containsKey(methodName)) {
                    delegatesByMethod.put(methodName, pair.a1);
                }
            }
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        Object delegate = delegatesByMethod.get(methodName);
        return method.invoke(delegate, args);
    }

//    @SuppressWarnings("unchecked")
//    public static Object newInstance(Tuple2... pairs) {
//        Class[] interfaces = new Class[pairs.length];
//        for (int i = 0; i < pairs.length; i++) {
//            interfaces[i] = (Class) pairs[i].a2;
//        }
//        ClassLoader cl = pairs[0].a1.getClass().getClassLoader();
//        return Proxy.newProxyInstance(cl, interfaces, new MixinProxy(pairs));
//    }

    @SafeVarargs
    public static Object newInstance(Tuple2<Object, Class<?>>... pairs) {
        Class<?>[] interfaces = new Class<?>[pairs.length];
        for (int i = 0; i < pairs.length; i++) {
            interfaces[i] = pairs[i].a2;
        }
        ClassLoader cl = pairs[0].a1.getClass().getClassLoader();
        return Proxy.newProxyInstance(cl, interfaces, new MixinProxy(pairs));
    }
}

public class DynamicProxyMixin {
    public static void main(String[] args) {
        Object mixin = MixinProxy.newInstance(
                tuple(new BasicImp(), Basic.class),
                tuple(new TimeStampedImp(), TimeStamped.class),
                tuple(new SerialNumberedImp(), SerialNumbered.class)
        );
        // Bruce Eckel 在这一节花费这么多笔墨，其实是为了解决 Java 语言的一个“先天遗憾”————缺乏真正的多重继承。
        // 虽然这是 Java 静态类型系统下最接近混型的方式，但也存在局限性：由于动态代理导致静态类型缺失，编译器仅将其视为 Object，
        // 调用前必须显式向下转型。这比 C++ 那种天然的混型要笨拙，但在 Java 的规则枷锁下，这已经是能做到的极限操作了。
        Basic b = (Basic) mixin;
        TimeStamped t = (TimeStamped) mixin;
        SerialNumbered s = (SerialNumbered) mixin;
        b.set("Hello");
        System.out.println(b.get());
        System.out.println(t.getStamp());
        System.out.println(s.getSerialNumber());
    }
}
/* Output:
Hello
1768661802155
1
 */
