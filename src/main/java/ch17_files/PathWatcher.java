package ch17_files;

import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;

/**
 * @author runningpig66
 * @date 2025/12/9 周二
 * @time 17:12
 * P.538 §17.4 监听 Path
 * {Working directory: E:\IdeaProjects\onjava\src\main\java\ch17_files}
 * <p>
 * WatchService 使我们能够设置一个进程，对某个目录中的变化做出反应。
 * 在下面的示例中，delTxtFiles() 作为一个独立的任务运行，
 * 它会遍历整个目录树，删除所有名字以 .txt 结尾的文件，WatchService 会对文件的删除做出反应。
 * <p>
 * notes: PathWatcher.md
 * 为什么必须在调用会阻塞的 `watchService.take()` 方法之前，通过另一个线程（如 ScheduledExecutorService）来安排触发文件事件的任务？
 */
public class PathWatcher {
    static Path test = Paths.get("test");

    static void delTxtFiles() {
        try (Stream<Path> pathStream = Files.walk(test)) {
            pathStream.filter(path -> path.toString().endsWith(".txt"))
                    .forEach(PathWatcher::deletePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void deletePath(Path path) {
        System.out.println("deleting " + path);
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        Directories.refreshTestDir();
        Directories.populateTestDir();
        Files.createFile(test.resolve("Hello.txt"));

        WatchService watchService = FileSystems.getDefault().newWatchService();
        // 将 WatchService 注册到特定目录，仅监听‘删除’事件。*注意：它只监听此目录的直接子项，不包括子目录中的文件。
        test.register(watchService, ENTRY_DELETE);
//        Executors.newSingleThreadScheduledExecutor()
//                .schedule(PathWatcher::delTxtFiles, 250, TimeUnit.MILLISECONDS);
        try (ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor()) {
            executorService.schedule(PathWatcher::delTxtFiles, 250, TimeUnit.MILLISECONDS);
            // take() 会阻塞主线程，直到被监听目录发生注册过的事件
            // TODO 如果先执行 take, 主线程会被阻塞就什么都干不了了，更别提删除目录了。
            WatchKey watchKey = watchService.take();
            // TODO <?>
            for (WatchEvent<?> event : watchKey.pollEvents()) {
                // context(): 触发事件的文件名（Path 类型）
                // count(): 同一事件的累计次数（通常为1）
                // kind(): 事件类型（此处为 ENTRY_DELETE）
                System.out.println("event.context(): " + event.context() +
                        "\nevent.count(): " + event.count() +
                        "\nevent.kind(): " + event.kind());
            }
            System.exit(0);
        }
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
 */
