package ch14_streams;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/10/28 周二
 * @time 22:50
 * 代码清单 P.394 在应用 map() 期间组合流：创建一个不需要中间存储的单词流。
 * 注意这里的正则表达式模式使用的是 \\W+。\\W 意味着一个“非单词字符”，
 * 而 + 意味着“一个或多个”。小写形式 \\w 指的是“单词字符”。
 */
public class FileToWords {
    public static Stream<String> stream(String filePath) throws Exception {
        return Files.lines(Paths.get(filePath))
                .skip(1) // First (comment) line
                .flatMap(line ->
                        Pattern.compile("\\W+").splitAsStream(line));
    }
}
