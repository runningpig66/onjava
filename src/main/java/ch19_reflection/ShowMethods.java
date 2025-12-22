package ch19_reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author runningpig66
 * @date 2025/12/22 周一
 * @time 15:35
 * P.619 §19.6 运行时的类信息 类方法提取器
 * Using reflection to show all the methods of a class,
 * even if the methods are defined in the base class
 * {java ch19_reflection.ShowMethods ch19_reflection.ShowMethods}
 * <p>
 * 请考虑一个类方法提取器。如果我们查看一个类定义的源代码或其 JDK 文档，只能找到在这个类中被定义或被重写的方法。
 * 但对我们来说，可能还有更多继承自其基类的可用方法。要找出这些方法既乏味又费时。
 * 幸运的是，反射提供了一种方式，让我们能够编写简单的工具来自动展示完整的接口：
 * <p>
 * Class 类里的方法 getMethods() 和 getConstructors() 分别返回了 Method 对象的数组和 Constructor 对象的数组。
 * 这两个类都提供了对应的方法，来进一步解析它们所代表的方法，并获取其名称、参数和返回值的相关信息。
 * 但你也可以像下面的示例那样，只使用 toString() 方法来生成一个含有完整的方法签名的字符串。其他部分的代码提取了命令行信息，
 * 判断某个特定的方法签名是否与我们的目标字符串相匹配（使用 contains()），并使用正则表达式去掉了名称限定符。
 * public Method[] getMethods() throws SecurityException
 * public Constructor<?>[] getConstructors() throws SecurityException
 * <p>
 * 在编写程序时，如果你不记得一个类是否有一个特定的方法，并且也不想在 JDK 文档中查找索引或类层次结构，
 * 或者你不知道这个类是否可以对某个对象（比如 Color 对象）做些什么，那么这个工具可以替你节省很多时间。
 */
public class ShowMethods {
    // 输出里包含了一个 public 的无参构造器，即使代码中没有定义任何构造器。
    // 你看到的这个构造器是由编译器自动合成的。如果将 ShowMethods 改为非 public 类（即包访问权限），
    // 那么这个自动合成的无参构造器就不会在输出中显示了。合成的无参构造器会自动获得与类相同的访问权限。
    // ShowMethods() {}

    private static String usage =
            "usage:\n" +
                    "ShowMethods qualified.class.name\n" +
                    "To show all methods in class or:\n" +
                    "ShowMethods qualified.class.name word\n" +
                    "To search for methods involving 'word'";
    private static Pattern p = Pattern.compile("\\w+\\.");

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println(usage);
            System.exit(0);
        }
        int lines = 0;
        try {
            Class<?> c = Class.forName(args[0]);
            Method[] methods = c.getMethods();
            Constructor<?>[] ctors = c.getConstructors();
            if (args.length == 1) {
                for (Method method : methods) {
                    System.out.println(p.matcher(method.toString()).replaceAll(""));
                }
                for (Constructor<?> ctor : ctors) {
                    System.out.println(p.matcher(ctor.toString()).replaceAll(""));
                }
                lines = methods.length + ctors.length;
            } else {
                for (Method method : methods) {
                    if (method.toString().contains(args[1])) {
                        System.out.println(p.matcher(method.toString()).replaceAll(""));
                        lines++;
                    }
                }
                for (Constructor<?> ctor : ctors) {
                    if (ctor.toString().contains(args[1])) {
                        System.out.println(p.matcher(ctor.toString()).replaceAll(""));
                        lines++;
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            System.out.println("No such class: " + e);
        }
    }
}
/* Output:
public static void main(String[])
public boolean equals(Object)
public String toString()
public native int hashCode()
public final native Class getClass()
public final native void notify()
public final native void notifyAll()
public final void wait(long) throws InterruptedException
public final void wait(long,int) throws InterruptedException
public final void wait() throws InterruptedException
public ShowMethods()
 */
