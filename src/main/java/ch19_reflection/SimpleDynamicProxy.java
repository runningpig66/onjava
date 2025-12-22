package ch19_reflection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author runningpig66
 * @date 2025/12/22 周一
 * @time 17:20
 * P.623 §19.7 动态代理
 * <p>
 * Java 的动态代理 (dynamic proxy) 比代理更进一步，它可以动态地创建代理，并动态地处理对所代理方法的调用。
 * 在动态代理上进行的所有调用都会被重定向到一个调用处理器 (invocation handler) 上，
 * 这个调用处理器的工作就是发现这是什么调用，然后决定如何处理它。下面是用动态代理重写的 SimpleProxyDemo.java:
 * <p>
 * 我们通过调用静态方法 Proxy.newProxyInstance() 来创建动态代理，它需要三个参数：
 * 一个类加载器（通常可以从一个已经加载的对象里获取其类加载器，然后传递给它就可以了），
 * 一个希望代理实现的接口列表（不是类或抽象类），以及 InvocationHandler 接口的一个实现。
 * 动态代理会将所有调用重定向到调用处理器，因此调用处理器的构造器通常会获得“实际”对象的引用，以便在执行完自己的中间任务后可以转发请求。
 * <p>
 * 代理对象传递给了 invoke() 方法来处理，以防你需要区分请求的来源，但是在许多情况下，你并不关心这一点。
 * 不过，在 invoke() 内部调用代理的方法时需要小心，因为对接口的调用是通过代理进行重定向的。
 * <p>
 * (Method.java)
 * public Object invoke(Object obj, Object... args) throws IllegalAccessException, InvocationTargetException
 * (Proxy.java)
 * public static Object newProxyInstance(ClassLoader loader, Class<?>[] interfaces, InvocationHandler h)
 * <p>
 * notes: SimpleDynamicProxy.md
 * 理解Java动态代理
 */
class DynamicProxyHandler implements InvocationHandler {
    private Object proxied;

    DynamicProxyHandler(Object proxied) {
        this.proxied = proxied;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("**** proxy: " + proxy.getClass() +
                ", method: " + method + ", args: " + args);
        if (args != null) {
            for (Object arg : args) {
                System.out.println("  " + arg);
            }
        }
        return method.invoke(proxied, args);
    }
}

class SimpleDynamicProxy {
    public static void consumer(Interface iface) {
        iface.doSomething();
        iface.somethingElse("bonobo");
    }

    public static void main(String[] args) {
        RealObject real = new RealObject();
        consumer(real);
        // Insert a proxy and call again:
        Interface proxy = (Interface) Proxy.newProxyInstance(
                Interface.class.getClassLoader(),
                new Class[]{Interface.class},
                new DynamicProxyHandler(real)
        );
        consumer(proxy);
    }
}
/* Output:
doSomething
somethingElse bonobo
**** proxy: class ch19_reflection.$Proxy0, method: public abstract void ch19_reflection.Interface.doSomething(), args: null
doSomething
**** proxy: class ch19_reflection.$Proxy0, method: public abstract void ch19_reflection.Interface.somethingElse(java.lang.String), args: [Ljava.lang.Object;@4fca772d
  bonobo
somethingElse bonobo
 */
