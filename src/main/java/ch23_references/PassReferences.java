package ch23_references;

/**
 * @author runningpig66
 * @date 2月9日 周一
 * @time 20:58
 * P.061 §2.1 传递引用
 * <p>
 * 当你将一个引用传给方法后，该引用指向的仍然是原来的对象。可以通过下面这个简单的实验看出来这一点：
 */
public class PassReferences {
    public static void f(PassReferences h) {
        System.out.println("h inside f(): " + h);
    }

    public static void main(String[] args) {
        PassReferences p = new PassReferences();
        System.out.println("p inside main(): " + p);
        f(p);
    }
}
/* Output:
p inside main(): ch23_references.PassReferences@2ff4acd0
h inside f(): ch23_references.PassReferences@2ff4acd0
 */
