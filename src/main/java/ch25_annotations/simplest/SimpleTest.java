package ch25_annotations.simplest;

/**
 * @author runningpig66
 * @date 2月24日 周二
 * @time 11:05
 * P.175 §4.3 用 javac 处理注解 §4.3.1 最简单的处理器
 * Test the "Simple" annotation
 * <p>
 * SimpleTest.java 只要求 Simple.java 能成功编译，虽然编译的过程中什么都没有发生。
 * javac 允许使用 @Simple 注解（只要它还存在），但是并不会对它做任何事，直到我们创建了一个注解处理器，并将其绑定到编译器中。
 */
@Simple
public class SimpleTest {
    @Simple
    int i;

    @Simple
    public SimpleTest() {
    }

    @Simple
    public void foo() {
        System.out.println("SimpleTest.foo()");
    }

    @Simple
    public void bar(String s, int i, float f) {
        System.out.println("SimpleTest.bar()");
    }

    @Simple
    public static void main(String[] args) {
        @Simple
        SimpleTest st = new SimpleTest();
        st.foo();
    }
}
/* Output:
SimpleTest.foo()
 */
