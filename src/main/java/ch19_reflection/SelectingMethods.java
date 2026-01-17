package ch19_reflection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author runningpig66
 * @date 2025/12/22 周一
 * @time 19:53
 * P.624 §19.7 动态代理
 * Looking for particular methods in a dynamic proxy
 * <p>
 * 通常，你会执行被代理的操作，然后使用 Method.invoke() 方法将请求转发给被代理的对象，并传入必要的参数。
 * 乍一看这可能有些受限，就好像你只能执行通用的操作一样。但是，你可以过滤某些方法调用、同时又放行其他的方法调用；
 * <p>
 * notes: Java动态代理：从原理到实战.md
 */
class MethodSelector implements InvocationHandler {
    private Object proxied;

    MethodSelector(Object proxied) {
        this.proxied = proxied;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 在这里，我们只是查看了方法名称，但你还可以查看方法签名的其他方面，甚至可以搜索特定的参数值。
        if (method.getName().equals("interesting")) {
            System.out.println("Proxy detected the interesting method");
        }
        return method.invoke(proxied, args);
    }
}

interface SomeMethods {
    void boring1();

    void boring2();

    void interesting(String arg);

    void boring3();
}

class Implementation implements SomeMethods {
    @Override
    public void boring1() {
        System.out.println("boring1");
    }

    @Override
    public void boring2() {
        System.out.println("boring2");
    }

    @Override
    public void interesting(String arg) {
        System.out.println("interesting " + arg);
    }

    @Override
    public void boring3() {
        System.out.println("boring3");
    }
}

public class SelectingMethods {
    public static void main(String[] args) {
        SomeMethods proxy = (SomeMethods) Proxy.newProxyInstance(
                SomeMethods.class.getClassLoader(),
                new Class[]{SomeMethods.class},
                new MethodSelector(new Implementation())
        );
        proxy.boring1();
        proxy.boring2();
        proxy.interesting("bonobo");
        proxy.boring3();
    }
}
/* Output:
boring1
boring2
Proxy detected the interesting method
interesting bonobo
boring3
 */
