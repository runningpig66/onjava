package ch25_annotations;

import onjava.atunit.Test;
import onjava.atunit.TestObjectCleanup;
import onjava.atunit.TestObjectCreate;
import onjava.atunit.TestProperty;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * @author runningpig66
 * @date 2月27日 周五
 * @time 1:06
 * P.189 §4.4 基于注解的单元测试
 * {java -cp build/classes/java/main onjava.atunit.AtUnit
 * build/classes/java/main/ch25_annotations/AtUnitExample5.class}
 * <p>
 * 如果测试对象的创建过程需要执行初始化，而且需要在稍后清理对象，你可以选择添加一个静态的 @TestObjectCleanup 方法，以在使用完测试对象后执行清理工作。
 * 在下一个示例中，@TestObjectCreate 通过打开一个文件来创建各个测试对象，因此必须在丢弃测试对象前关闭该文件。
 */
public class AtUnitExample5 {
    private String text;

    public AtUnitExample5(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    @TestProperty
    static PrintWriter output;
    @TestProperty
    static int counter;

    @TestObjectCreate
    static AtUnitExample5 create() {
        String id = Integer.toString(counter++);
        try {
            output = new PrintWriter("src/main/java/ch25_annotations/Test" + id + ".txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new AtUnitExample5(id);
    }

    // 从输出可以看出，在每项测试之后，清理方法都被自动执行了。
    @TestObjectCleanup
    static void cleanup(AtUnitExample5 tobj) {
        System.out.println("Running cleanup");
        output.close();

        // 验证文件清除功能的逻辑。测试确认能够正常删除后，将其注释掉即可保留生成的文件用于查看。
        /*String filePath = "src/main/java/ch25_annotations/Test" + tobj.toString() + ".txt";
        File file = new File(filePath);
        if (file.exists()) {
            boolean isDeleted = file.delete();
            System.out.println("File deleted: " + isDeleted);
        }*/
    }

    @Test
    boolean test1() {
        output.print("test1");
        return true;
    }

    @Test
    boolean test2() {
        output.print("test2");
        return true;
    }

    @Test
    boolean test3() {
        output.print("test3");
        return true;
    }
}
/* Output:
ch25_annotations.AtUnitExample5
  . test1
Running cleanup
  . test3
Running cleanup
  . test2
Running cleanup
OK (3 tests)
 */
/* Output 2:
ch25_annotations.AtUnitExample5
  . test1
Running cleanup
File deleted: true
  . test3
Running cleanup
File deleted: true
  . test2
Running cleanup
File deleted: true
OK (3 tests)
 */
