package ch12_collections;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author runningpig66
 * @date 2025-07-28
 * @time 下午 17:59
 */
public class UniqueWords {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("E:\\IdeaProjects\\onjava\\src\\main\\java\\ch12_collections\\SetOperations.java"));
        Set<String> words = new TreeSet<>();
        for (String line : lines) {
            for (String word : line.split("\\W+")) {
                if (!word.trim().isEmpty()) {
                    words.add(word);
                }
            }
        }
        System.out.println(words);
    }
}
/* Output:
[07, 16, 2025, 28, 7, A, B, C, Collections, D, E, F, G, H, HashSet, I, J, K, L, M, N, Output, Set, SetOperations, String, System, X, Y, Z, add, addAll, added, args, author, ch12_collections, class, contains, containsAll, date, false, from, import, in, java, main, new, out, package, println, public, remove, removeAll, removed, runningpig66, set1, set2, split, static, time, to, true, util, void]
 */
