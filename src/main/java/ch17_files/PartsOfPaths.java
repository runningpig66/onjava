package ch17_files;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author runningpig66
 * @date 2025/12/3 周三
 * @time 20:15
 * P.530 §17.1 文件和目录路径 §17.1.1 选择 Path 的片段
 * {Working directory: E:\IdeaProjects\onjava\src\main\java\ch17_files}
 * <p>
 * 我们可以轻松获得 Path 对象路径的各个部分。注意 [1] 和 [2]。
 */
public class PartsOfPaths {
    public static void main(String[] args) {
        System.out.println(System.getProperty("os.name"));
        Path p = Paths.get("PartsOfPaths.java").toAbsolutePath();
        for (int i = 0; i < p.getNameCount(); i++) {
            System.out.println(p.getName(i));
        }
        // [1] 请注意，尽管这里的路径确实是以 .java 结尾的，但 endsWith() 的结果是 false。
        // 这是因为 endsWith() 比较的是整个路径组件，而不是名字中的一个子串。
        // 即：p.endsWith("PartsOfPaths.java") 才是 true
        System.out.println("ends with '.java': " + p.endsWith(".java"));
        for (Path pp : p) {
            System.out.print(pp + ": ");
            System.out.print(p.startsWith(pp) + " : ");
            System.out.println(p.endsWith(pp));
        }
        // [2] 然而．我们看到在对 Path 进行遍历时，并没有包含根目录，
        // 只有当我们用根目录来检查 startsWith() 时，才会得到 true。
        System.out.println("Starts with " + p.getRoot() + " " + p.startsWith(p.getRoot()));
    }
}
/* Output:
Windows 11
IdeaProjects    // 我们看到在对 Path 进行遍历时，并没有包含根目录
onjava
src
main
java
ch17_files
PartsOfPaths.java
ends with '.java': false
IdeaProjects: false : false    // 我们看到在对 Path 进行遍历时，并没有包含根目录
onjava: false : false
src: false : false
main: false : false
java: false : false
ch17_files: false : false
PartsOfPaths.java: false : true
Starts with E:\ true    // 只有当我们用根目录来检查 startsWith() 时，才会得到 true。
 */
