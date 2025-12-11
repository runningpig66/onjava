package ch17_files;

import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * @author runningpig66
 * @date 2025/12/8 周一
 * @time 17:44
 * P.537 §17.3 文件系统
 * <p>
 * 为了完整起见，我们需要一种方式来找出文件系统的其他信息。
 * 在这里，我们可以使用静态的 FileSystems 工具来获得“默认”的文件系统，
 * 但也可以在一个 Path 对象上调用 getFileSystem() 来获得创建这个路径对象的文件系统。
 * 我们可以通过给定的 URI 获得一个文件系统，也可以构建一个新的文件系统（如果操作系统支持的话）。
 */
public class FileSystemDemo {
    static void show(String id, Object o) {
        System.out.println(id + ": " + o);
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("os.name"));
        FileSystem fsys = FileSystems.getDefault();
        // 遍历并显示所有可用的文件存储（如磁盘分区 C:, D:）
        for (FileStore fs : fsys.getFileStores()) {
            show("File Store", fs);
        }
        // 遍历并显示系统的根目录（如 Windows 的 C:\, Unix 的 /）
        for (Path rd : fsys.getRootDirectories()) {
            show("Root Directory", rd);
        }
        // 获取系统路径分隔符（Windows 为 ‘\’，Unix 为 ‘/’）
        show("Separator", fsys.getSeparator());
        // 获取用于查找用户和组身份的服务（用于权限管理）
        show("UserPrincipalLookupService", fsys.getUserPrincipalLookupService());
        // 检查此文件系统是否处于打开可用状态
        show("isOpen", fsys.isOpen());
        // 检查此文件系统是否为只读
        show("isReadOnly", fsys.isReadOnly());
        // 获取此文件系统底层的提供者（Provider）实现
        show("FileSystemProvider", fsys.provider());
        // 列出此文件系统支持的文件属性视图（如“basic”, “posix”, “owner”等）
        show("File Attribute Views", fsys.supportedFileAttributeViews());
    }
}
/* Output:
Windows 11
File Store: (C:)
File Store: (D:)
File Store: (E:)
Root Directory: C:\
Root Directory: D:\
Root Directory: E:\
Root Directory: F:\
Separator: \
UserPrincipalLookupService: sun.nio.fs.WindowsFileSystem$LookupService$1@3b07d329
isOpen: true
isReadOnly: false
FileSystemProvider: sun.nio.fs.WindowsFileSystemProvider@41629346
File Attribute Views: [basic, user, owner, dos, acl]
 */
