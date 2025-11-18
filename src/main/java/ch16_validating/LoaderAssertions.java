package ch16_validating;

/**
 * @author runningpig66
 * @date 2025/11/18 周二
 * @time 23:39
 * 代码清单 P.483 前置条件：断言：1. Java断言语法
 * Using the class loader to enable assertions
 * {ThrowsException}
 * <p>
 * 还有另一种方法可以控制断言：以编程的方式操作 ClassLoader 对象。ClassLoader 中有几种方法允许动态启用和禁用断言，
 * 包括 setDefaultAssertionStatus(). 它为之后加载的所有类设置了断言状态。所以你可以像这样静默地开启断言：
 */
public class LoaderAssertions {
    public static void main(String[] args) {
        ClassLoader.getSystemClassLoader().setDefaultAssertionStatus(true);
        new Loaded().go();
    }
}

class Loaded {
    public void go() {
        assert false : "Loaded.go()";
    }
}
/* Output:
Exception in thread "main" java.lang.AssertionError: Loaded.go()
	at ch16_validating.Loaded.go(LoaderAssertions.java:23)
	at ch16_validating.LoaderAssertions.main(LoaderAssertions.java:17)
 */
