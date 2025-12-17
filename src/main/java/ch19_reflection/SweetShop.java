package ch19_reflection;

/**
 * @author runningpig66
 * @date 2025/12/17 周三
 * @time 10:57
 * P.593 §19.2 Class 对象
 * Examination of the way the class loader works
 * {Working directory: E:\IdeaProjects\onjava\src\main\java\ch18_strings}
 * <p>
 * 所有的 Class 对象都属于 Class 类。Class 对象和其他对象一样，因此你可以获取并操作它的引用（这也是加载器所做的）。
 * 静态的 forName() 方法可以获得 Class 对象的引用，该方法接收了一个包含所需类的文本名称的字符串，并返回了一个 Class 引用；
 * 我们对 forName() 的调用只是为了它的副作用：如果类 Gum 尚未加载，则加载它。在加载过程中，会执行 Gum 的静态代码块。
 * public static Class<?> forName(String className)
 */
class Cookie {
    static {
        System.out.println("Loading Cookie");
    }
}

class Gum {
    static {
        System.out.println("Loading Gum");
    }
}

class Candy {
    static {
        System.out.println("Loading Candy");
    }
}

public class SweetShop {
    public static void main(String[] args) {
        System.out.println("inside main");
        new Candy();
        System.out.println("After creating Candy");
        try {
            // 必须使用完全限定类名（含包名）
            Class.forName("ch19_reflection.Gum");
        } catch (ClassNotFoundException e) {
            System.out.println("Couldn't find Gum");
        }
        System.out.println("After Class.forName(\"Gum\")");
        new Cookie();
        System.out.println("After creating Cookie");
    }
}
/* Output:
inside main
Loading Candy
After creating Candy
Loading Gum
After Class.forName("Gum")
Loading Cookie
After creating Cookie
 */
