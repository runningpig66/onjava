package ch17_files;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.Executors;

import static ch17_files.PathWatcher.test;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;

/**
 * @author runningpig66
 * @date 2025/12/10 周三
 * @time 14:34
 * P.540 §17.4 监听 Path
 * {Working directory: E:\IdeaProjects\onjava\src\main\java\ch17_files}
 * <p>
 * 你可能会认为，如果我们说“监听这个目录”，它自然会包括整个子树，但这里就要按字面意思理解：
 * 它只监听这个目录，而不是它下面的一切。如果想监听整个目录树，则必须在整个树的每个子目录上设置一个 WatchService。
 * <p>
 * watchDir() 方法在其参数上放了一个关注 ENTRY_DELETE 事件的 WatchService，同时启动了一个独立的进程来监控这个 WatchService。
 * 这里没有通过 schedule() 方法让任务推迟到以后再运行，而是通过 submit() 让它现在就运行。我们遍历整个目录树，
 * 并在每个子目录上应用 watchDir()。现在当我们运行 delTxtFiles() 时，各个 WatchService 检测到了删除操作。
 * <p>
 * notes: TreeWatcher.md
 * 为什么监听线程（子线程）和事件触发线程（主线程）的角色与 PathWatcher.java 示例完全相反？这种设计如何解决了单层目录监听的限制？
 */
public class TreeWatcher {
    static void watchDir(Path dir) {
        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            dir.register(watcher, ENTRY_DELETE);
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    WatchKey watchKey = watcher.take();
                    for (WatchEvent event : watchKey.pollEvents()) {
                        System.out.println("event.context(): " + event.context() +
                                "\nevent.count(): " + event.count() +
                                "\nevent.kind(): " + event.kind());
                        System.exit(0);
                    }
                } catch (InterruptedException e) {
                    return;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        Directories.refreshTestDir();
        Directories.populateTestDir();
        Files.createFile(test.resolve("Hello.txt"));

        Files.walk(test)
                .filter(Files::isDirectory)
                .forEach(TreeWatcher::watchDir);
        PathWatcher.delTxtFiles();
    }
}
/* Output:
deleting test\bag\foo\bar\baz\File.txt
deleting test\bar\baz\bag\foo\File.txt
deleting test\baz\bag\foo\bar\File.txt
deleting test\foo\bar\baz\bag\File.txt
deleting test\Hello.txt
event.context(): Hello.txt
event.count(): 1
event.kind(): ENTRY_DELETE
event.context(): File.txt
event.count(): 1
event.kind(): ENTRY_DELETE
event.context(): File.txt
event.count(): 1
event.kind(): ENTRY_DELETE
event.context(): File.txt
event.count(): 1
event.kind(): ENTRY_DELETE
event.context(): File.txt
event.count(): 1
event.kind(): ENTRY_DELETE
 */
