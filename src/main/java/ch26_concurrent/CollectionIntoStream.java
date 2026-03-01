package ch26_concurrent;

import onjava.Rand;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author runningpig66
 * @date 3月1日 周日
 * @time 2:37
 * P.224 §5.7 并行流 §5.7.2 parallel() 和 limit() 的作用
 */
public class CollectionIntoStream {
    public static void main(String[] args) {
        List<String> strings = Stream.generate(new Rand.String(5))
                .limit(10)
                // .collect(Collectors.toList());
                .toList();
        strings.forEach(System.out::println);
        // Convert to a Stream for many more options:
        String result = strings.stream()
                .map(String::toUpperCase)
                .map(s -> s.substring(2))
                .reduce(":", (s1, s2) -> s1 + s2);
        System.out.println(result);
    }
}
/* Output:
btpen
pccux
szgvg
meinn
eeloz
tdvew
cippc
ygpoa
lkljl
bynxt
:PENCUXGVGINNLOZVEWPPCPOALJLNXT
 */
