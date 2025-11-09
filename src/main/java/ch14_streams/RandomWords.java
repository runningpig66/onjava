package ch14_streams;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 2025/10/20 周一
 * @time 0:41
 * 代码清单 P.381 我们可以使用 Random 来创建一个用以提供任何一组对象的 Supplier。
 * 下面是一个代码示例，读取 Cheese.dat 文本文件，生成 String 对象。
 */
public class RandomWords implements Supplier<String> {
    List<String> words = new ArrayList<>();
    Random rand = new Random(47);

    RandomWords(String fname) throws IOException {
        // 我们使用 Files 类把个文件中的所有文本行都读人到一个List<string>中:
        List<String> lines = Files.readAllLines(Paths.get(fname));
        // Skip the first line:
        for (String line : lines.subList(1, lines.size())) {
            for (String word : line.split("[ .?,]+")) {
                words.add(word.toLowerCase());
            }
        }
    }

    @Override
    public String get() {
        return words.get(rand.nextInt(words.size()));
    }

    @Override
    public String toString() {
        return words.stream().collect(Collectors.joining(" "));
    }
    public static void main(String[] args) throws IOException {
        System.out.println(Stream.generate(new RandomWords("src/main/java/ch14_streams/Cheese.dat"))
                .limit(10)
                .collect(Collectors.joining(" ")));
    }
}
/* Output:
it shop sir the much cheese by conclusion district is
 */