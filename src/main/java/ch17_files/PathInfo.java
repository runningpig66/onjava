package ch17_files;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author runningpig66
 * @date 2025/12/3 周三
 * @time 19:02
 * P.527 §17.1 文件和目录路径
 * {Working directory: E:\IdeaProjects\onjava\src\main\java\ch17_files}
 */
public class PathInfo {
    static void show(String id, Object p) {
        System.out.println(id + p);
    }

    static void info(Path p) {
        show("toString:\n ", p);
        show("Exists: ", Files.exists(p));
        show("RegularFile: ", Files.isRegularFile(p));
        show("Directory: ", Files.isDirectory(p));
        show("Absolute: ", p.isAbsolute());
        show("FileName: ", p.getFileName());
        show("Parent: ", p.getParent());
        show("Root: ", p.getRoot());
        System.out.println("******************");
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("os.name"));
        info(Paths.get("C:", "path", "to", "nowhere", "NoFile.txt"));
        Path p = Paths.get("PathInfo.java");
        info(p);
        Path ap = p.toAbsolutePath();
        info(ap);
        info(ap.getParent());
        try {
            info(p.toRealPath());
        } catch (IOException e) {
            System.out.println(e);
        }
        URI u = p.toUri();
        System.out.println("URI:\n" + u);
        Path puri = Paths.get(u);
        System.out.println(Files.exists(puri));
        File f = ap.toFile(); // Don't be fooled
    }
}
/* Output:
Windows 11
toString:
 C:\path\to\nowhere\NoFile.txt
Exists: false
RegularFile: false
Directory: false
Absolute: true
FileName: NoFile.txt
Parent: C:\path\to\nowhere
Root: C:\
******************
toString:
 PathInfo.java
Exists: true
RegularFile: true
Directory: false
Absolute: false
FileName: PathInfo.java
Parent: null
Root: null
******************
toString:
 E:\IdeaProjects\onjava\src\main\java\ch17_files\PathInfo.java
Exists: true
RegularFile: true
Directory: false
Absolute: true
FileName: PathInfo.java
Parent: E:\IdeaProjects\onjava\src\main\java\ch17_files
Root: E:\
******************
toString:
 E:\IdeaProjects\onjava\src\main\java\ch17_files
Exists: true
RegularFile: false
Directory: true
Absolute: true
FileName: ch17_files
Parent: E:\IdeaProjects\onjava\src\main\java
Root: E:\
******************
toString:
 E:\IdeaProjects\onjava\src\main\java\ch17_files\PathInfo.java
Exists: true
RegularFile: true
Directory: false
Absolute: true
FileName: PathInfo.java
Parent: E:\IdeaProjects\onjava\src\main\java\ch17_files
Root: E:\
******************
URI:
file:///E:/IdeaProjects/onjava/src/main/java/ch17_files/PathInfo.java
true
 */
