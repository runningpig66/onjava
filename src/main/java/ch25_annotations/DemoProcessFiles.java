package ch25_annotations;

import onjava.ProcessFiles;

/**
 * @author runningpig66
 * @date 2月27日 周五
 * @time 4:47
 * P.197 §4.4 基于注解的单元测试 §4.4.2 实现 @Unit
 * {Working directory: E:\IdeaProjects\onjava\src\main\java\ch25_annotations}
 * <p>
 * AtUnit 类实现了 ProcessFiles.Strategy，其中包含 process() 方法。由此，AtUnit 的实例可以传递给 ProcessFiles 构造器。
 * 构造器的第二个参数告诉 ProcessFiles 去查找所有文件名后缀为 .class 的文件。以下是简单的用法示例：
 * 在没有命令行参数的情况下，程序会遍历当前的目录树。你还可以提供多个参数，可以是类文件（不论文件名是否带有 .class 后缀）或目录。
 */
public class DemoProcessFiles {
    public static void main(String[] args) {
        // new ProcessFiles(file -> System.out.println(file), "java").start(args);
        new ProcessFiles(System.out::println, "java").start(args);
    }
}
/* Output:
.\AtUnitExample1.java
.\AtUnitExample2.java
.\AtUnitExample3.java
.\AtUnitExample4.java
.\AtUnitExample5.java
.\AUComposition.java
.\AUExternalTest.java
.\database\Constraints.java
.\database\DBTable.java
.\database\Member.java
.\database\SQLInteger.java
.\database\SQLString.java
.\database\TableCreator.java
.\database\Uniqueness.java
.\DemoProcessFiles.java
.\HashSetTest.java
.\ifx\ExtractInterface.java
.\ifx\IfaceExtractorProcessor.java
.\ifx\IMultiplier.java
.\ifx\Multiplier.java
.\PasswordUtils.java
.\simplest\Simple.java
.\simplest\SimpleProcessor.java
.\simplest\SimpleTest.java
.\SimulatingNull.java
.\StackL.java
.\StackLStringTst.java
.\Testable.java
.\UseCase.java
.\UseCaseTracker.java
 */
