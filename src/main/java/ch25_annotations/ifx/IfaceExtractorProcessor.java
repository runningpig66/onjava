package ch25_annotations.ifx;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author runningpig66
 * @date 2月25日 周三
 * @time 4:36
 * P.180 §4.3 用 javac 处理注解 §4.3.2 更复杂的处理器
 * javac-based annotation processing
 * {cd src\main\java}
 * {javac ch25_annotations\ifx\IfaceExtractorProcessor.java}
 * {javac -processor ch25_annotations.ifx.IfaceExtractorProcessor ch25_annotations\ifx\Multiplier.java}
 * <p>
 * 下面的示例是一个编译期处理器，它会提取出感兴趣的方法，并创建新接口的源代码文件（该源文件之后会作为“编译阶段”的一部分，被自动编译）：
 * <p>
 * 在实际的工业级开源框架（如 ButterKnife, Dagger, Room）中，为了提高代码生成的健壮性与可维护性，通常避免手动拼接字符串，而是采用 Square 公司开发的 JavaPoet 库。
 * JavaPoet 提供了一套面向对象的 API（如 TypeSpec, MethodSpec），能够以结构化的方式生成 `.java` 源文件，自动处理导包、缩进及语法校验等细节。
 */
@SupportedAnnotationTypes("ch25_annotations.ifx.ExtractInterface")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class IfaceExtractorProcessor extends AbstractProcessor {
    private ArrayList<Element> interfaceMethods = new ArrayList<>();
    // Elements 对象 elementUtils 是个 static 工具的集合，我们通过它来在 writeInterfaceFile() 中找到包名。
    Elements elementUtils;
    private ProcessingEnvironment processingEnv;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        for (Element elem : env.getElementsAnnotatedWith(ExtractInterface.class)) {
            // 在处理每个新类之前，必须清空上一次累积的方法列表，防止方法混入到错误的接口中。
            interfaceMethods.clear();
            String simpleInterfaceName = elem.getAnnotation(ExtractInterface.class).interfaceName();
            String packageName = elementUtils.getPackageOf(elem).getQualifiedName().toString();
            // getEnclosedElements() 方法生成被某个特定元素“围住”的所有元素。
            for (Element enclosed : elem.getEnclosedElements()) {
                if (enclosed.getKind().equals(ElementKind.METHOD) &&
                        enclosed.getModifiers().contains(Modifier.PUBLIC) &&
                        !enclosed.getModifiers().contains(Modifier.STATIC)) {
                    interfaceMethods.add(enclosed);
                }
            }
            if (!interfaceMethods.isEmpty()) {
                writeInterfaceFile(packageName, simpleInterfaceName);
            }
        }
        return false;
    }

    private void writeInterfaceFile(String packageName, String simpleInterfaceName) {
        // 构造全限定名（如 "ch25_annotations.ifx.IMultiplier"），以确保 Filer 能在正确的包路径下创建文件。
        String qualifiedName = packageName.isEmpty() ? simpleInterfaceName : packageName + "." + simpleInterfaceName;
        // Filer ( 由 getFiler() 生成 ) 是一种创建新文件的 PrintWriter。之所以使用 Filer 对象而非某个普通的 PrintWriter，
        // 是因为 Filer 对象允许 javac 持续跟踪你创建的所有新文件，从而可以检查它们的注解，并在额外的“编译阶段”中编译它们。
        try (Writer writer = processingEnv.getFiler().createSourceFile(qualifiedName).openWriter()) {
            //- String packageName = elementUtils.getPackageOf(interfaceMethods.get(0)).toString();
            if (!packageName.isEmpty()) {
                writer.write("package " + packageName + ";\n");
            }
            writer.write("public interface " + simpleInterfaceName + " {\n");
            for (Element elem : interfaceMethods) {
                ExecutableElement method = (ExecutableElement) elem;
                String signature = "  public ";
                signature += method.getReturnType() + " ";
                signature += method.getSimpleName();
                signature += createArgList(method.getParameters());
                System.out.println(signature);
                writer.write(signature + ";\n");
            }
            writer.write("}");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String createArgList(List<? extends VariableElement> parameters) {
        String args = parameters.stream()
                .map(p -> p.asType() + " " + p.getSimpleName())
                .collect(Collectors.joining(", "));
        return "(" + args + ")";
    }
}
/* Output:
  public int multiply(int x, int y)
  public int fortySeven()
  public double timesTen(double arg)
 */
