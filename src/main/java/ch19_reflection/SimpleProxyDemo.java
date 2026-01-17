package ch19_reflection;

/**
 * @author runningpig66
 * @date 2025/12/22 周一
 * @time 16:56
 * P.621 §19.7 动态代理
 * <p>
 * 代理（proxy）是基本的设计模式之一。它是为了代替“实际”对象而插入的一个对象，从而提供额外的或不同的操作。
 * 这些操作通常涉及与“实际”对象的通信，因此代理通常充当中间人的角色。下面是一个用来展示代理结构的简单示例：
 * <p>
 * 在任何时候，如果你想要将额外的操作从“实际”对象中分离出来，特别是当你没有使用这些额外操作，但希望很轻松地就能改成使用，
 * 或反过来，这时代理就很有用了（设计模式的关注点就是封装修改————因此你需要做对应的修改来适应模式）。
 * 例如，如果你希望跟踪对 RealObject 中方法的调用，或者测量此类调用的开销，该怎么办？
 * 你肯定不希望在应用程序中包含这些代码，而代理可以让你很容易地添加或删除它们。
 * <p>
 * notes: Java动态代理：从原理到实战.md
 */
interface Interface {
    void doSomething();

    void somethingElse(String arg);
}

class RealObject implements Interface {
    @Override
    public void doSomething() {
        System.out.println("doSomething");
    }

    @Override
    public void somethingElse(String arg) {
        System.out.println("somethingElse " + arg);
    }
}

class SimpleProxy implements Interface {
    private Interface proxied;

    SimpleProxy(Interface proxied) {
        this.proxied = proxied;
    }

    @Override
    public void doSomething() {
        System.out.println("SimpleProxy doSomething");
        proxied.doSomething();
    }

    @Override
    public void somethingElse(String arg) {
        System.out.println("SimpleProxy somethingElse " + arg);
        proxied.somethingElse(arg);
    }
}

class SimpleProxyDemo {
    public static void consumer(Interface iface) {
        iface.doSomething();
        iface.somethingElse("bonobo");
    }

    public static void main(String[] args) {
        consumer(new RealObject());
        consumer(new SimpleProxy(new RealObject()));
    }
}
/* Output:
doSomething
somethingElse bonobo
SimpleProxy doSomething
doSomething
SimpleProxy somethingElse bonobo
somethingElse bonobo
 */
