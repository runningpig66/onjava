package ch14_streams;

/**
 * @author runningpig66
 * @date 2025/11/2 周日
 * @time 0:30
 * 代码清单 P.395 在应用 map() 期间组合流：
 */
public class FileToWordsTest {
    public static void main(String[] args) throws Exception {
        FileToWords.stream("src/main/java/ch14_streams/Cheese.dat")
                .limit(7)
                .forEach(s -> System.out.format("%s ", s));
        System.out.println();
        FileToWords.stream("src/main/java/ch14_streams/Cheese.dat")
                .skip(7)
                .limit(2)
                .forEach(s -> System.out.format("%s ", s));
    }
}
/* Output:
Not much of a cheese shop really
is it
 */
