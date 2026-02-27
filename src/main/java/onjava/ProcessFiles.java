package onjava;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.PathMatcher;

/**
 * @author runningpig66
 * @date 2月27日 周五
 * @time 4:02
 * P.196 §4.4 基于注解的单元测试 §4.4.2 实现 @Unit
 * <p>
 * AtUnit.java 使用了另一个叫作 ProcessFiles 的工具来单步遍历命令行中的每个参数，以及确定它是目录还是文件，并进行相应的处理。
 * 其中包含了一个可定制的 Strategy（策略）接口，因此可应用于多种方案实现。
 * <p>
 * AtUnit 类实现了 ProcessFiles.Strategy，其中包含 process() 方法。由此，AtUnit 的实例可以传递给 ProcessFiles 构造器。
 * 构造器的第二个参数告诉 ProcessFiles 去查找所有文件名后缀为 .class 的文件。
 */
public class ProcessFiles {
    public interface Strategy {
        void process(File file);
    }

    private Strategy strategy;
    private String ext;

    public ProcessFiles(Strategy strategy, String ext) {
        this.strategy = strategy;
        this.ext = ext;
    }

    // 在没有命令行参数的情况下，程序会遍历当前的目录树。你还可以提供多个参数，可以是类文件（不论文件名是否带有 .class 后缀）或目录。
    public void start(String[] args) {
        try {
            if (args.length == 0) {
                processDirectoryTree(new File("."));
            } else {
                for (String arg : args) {
                    File fileArg = new File(arg);
                    if (fileArg.isDirectory()) {
                        processDirectoryTree(fileArg);
                    } else {
                        // Allow user to leave off extension:
                        if (!arg.endsWith("." + ext)) {
                            arg += "." + ext;
                        }
                        strategy.process(new File(arg).getCanonicalFile());
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void processDirectoryTree(File root) throws IOException {
        PathMatcher matcher = FileSystems.getDefault()
                .getPathMatcher("glob:**/*.{" + ext + "}");
        Files.walk(root.toPath())
                .filter(matcher::matches)
                .forEach(p -> strategy.process(p.toFile()));
    }
}
