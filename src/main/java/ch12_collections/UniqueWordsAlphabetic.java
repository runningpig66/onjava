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
 * @time 下午 19:59
 */
public class UniqueWordsAlphabetic {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("E:\\IdeaProjects\\onjava\\src\\main\\java\\ch12_collections\\SetOperations.java"));
        Set<String> words = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
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
[07, 16, 2025, 28, 7, A, add, addAll, added, args, author, B, C, ch12_collections, class, Collections, contains, containsAll, D, date, E, F, false, from, G, H, HashSet, I, import, in, J, java, K, L, M, main, N, new, out, Output, package, println, public, remove, removeAll, removed, runningpig66, Set, set1, set2, SetOperations, split, static, String, System, time, to, true, util, void, X, Y, Z]
 */
