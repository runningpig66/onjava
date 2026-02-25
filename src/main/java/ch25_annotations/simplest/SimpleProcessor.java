package ch25_annotations.simplest;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * @author runningpig66
 * @date 2月24日 周二
 * @time 11:18
 * P.176 §4.3 用 javac 处理注解 §4.3.1 最简单的处理器
 * A bare-bones annotation processor
 * {cd src\main\java}
 * {javac ch25_annotations\simplest\SimpleProcessor.java ch25_annotations\simplest\Simple.java}
 * {javac -processor ch25_annotations.simplest.SimpleProcessor ch25_annotations\simplest\SimpleTest.java}
 * <p>
 * 该处理器演示了如何通过 javac 编译器 API 读取源代码中的注解信息。
 * 必须通过元注解 @SupportedAnnotationTypes 和 @SupportedSourceVersion 进行配置，否则编译器在运行期间可能会忽略该处理器或抛出版本警告。
 */
// 指定该处理器注册处理的注解类型。格式为注解的全限定名（Fully Qualified Name）。
// 如果使用通配符 "*"，则表示该处理器会处理源代码中出现的所有注解（包括 @Override, @Deprecated 等标准注解）。
@SupportedAnnotationTypes("ch25_annotations.simplest.Simple")
// 指定该处理器支持的最新 Java 源代码版本。建议设置为当前开发环境的最新版本，以确保能正确处理新语法特性（如 Record, Switch Pattern 等）。
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class SimpleProcessor extends AbstractProcessor {
    // 注解处理器的核心逻辑入口。编译器会在编译过程中多次调用此方法（每一轮处理称为一个 Round）。
    // 第 1 个参数 annotations：本轮（Round）处理中，编译器在源代码中发现的、且被本处理器注册处理的注解类型集合。
    // 注意：这里包含的是注解接口本身的定义（TypeElement），而不是被注解修饰的类或方法。
    // 例如：如果源代码中使用了 @Simple，这里集合中将包含 ch25_annotations.simplest.Simple 的 TypeElement。
    // 如果配置了 "*"，则包含本轮扫描到的所有注解类型定义。
    // 第 2 个参数 roundEnv：当前的编译轮次环境（RoundEnvironment）。提供了访问本轮编译中抽象语法树（AST）的能力。
    // 主要用于查询被特定注解修饰的程序元素（Element），例如调用 getElementsAnnotatedWith(Simple.class) 来获取所有被 @Simple 修饰的类、方法或字段。
    // 返回值 boolean：true: 表示注解已被处理，编译器后续不会将这些注解传递给其他处理器。false: 表示处理器已完成工作，但这些注解仍可被后续的其他处理器处理。
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // 遍历 annotations 集合，打印本轮触发处理器的注解类型定义
        for (TypeElement t : annotations) {
            System.out.println(t);
        }
        // 通过 roundEnv 查询源代码中所有被 @Simple 注解修饰的元素。这里获取到的 Element 是实际的源代码结构（如 SimpleTest 类定义，foo 方法定义等）
        for (Element el : roundEnv.getElementsAnnotatedWith(Simple.class)) {
            display(el);
        }
        return false;
    }

    private void display(Element el) {
        System.out.println("==== " + el + " ====");
        System.out.println(el.getKind() +
                " : " + el.getModifiers() +
                " : " + el.getSimpleName() +
                " : " + el.asType());
        if (el.getKind().equals(ElementKind.CLASS)) {
            TypeElement te = (TypeElement) el;
            System.out.println(te.getQualifiedName());
            // TypeMirror getSuperclass();
            System.out.println(te.getSuperclass());
            // List<? extends Element> getEnclosedElements();
            System.out.println(te.getEnclosedElements());
        }
        if (el.getKind().equals(ElementKind.METHOD)) {
            ExecutableElement ex = (ExecutableElement) el;
            // TypeMirror getReturnType();
            System.out.print(ex.getReturnType() + " ");
            System.out.print(ex.getSimpleName() + "(");
            // List<? extends VariableElement> getParameters();
            System.out.println(ex.getParameters() + ")");
        }
    }
}
/* Output:
Test.java
ch25_annotations.simplest.Simple
==== ch25_annotations.simplest.SimpleTest ====
CLASS : [public] : SimpleTest : ch25_annotations.simplest.SimpleTest
ch25_annotations.simplest.SimpleTest
java.lang.Object
i,SimpleTest(),foo(),bar(java.lang.String,int,float),main(java.lang.String[])
==== i ====
FIELD : [] : i : int
==== SimpleTest() ====
CONSTRUCTOR : [public] : <init> : ()void
==== foo() ====
METHOD : [public] : foo : ()void
void foo()
==== bar(java.lang.String,int,float) ====
METHOD : [public] : bar : (java.lang.String,int,float)void
void bar(s,i,f)
==== main(java.lang.String[]) ====
METHOD : [public, static] : main : (java.lang.String[])void
void main(args)
 */
