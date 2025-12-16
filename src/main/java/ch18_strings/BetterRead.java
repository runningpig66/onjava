package ch18_strings;

import java.util.Scanner;

/**
 * @author runningpig66
 * @date 2025/12/16 周二
 * @time 17:44
 * P.585 §18.8 扫描输入
 * <p>
 * Java 5 中添加的 Scanner 类大大减轻了扫描输入的负担：
 * Scanner 的构造器可以接受任何类型的输入对象，包括 File 对象、InputStream、String，或者此例里的 Readable
 * （Java 5 引入的一个接口，用于描述“具有 read() 方法的东西”）。示例中 input（类型是 BufferedReader）就归于这一类。
 * public Scanner(Readable source)
 * <p>
 * 在 Scanner 中，输入、分词和解析这些操作都被包含在各种不同类型的 “next” 方法中。一个普通的 next() 返回下一个 String，
 * 所有的基本类型（char 除外）以及 BigDecimal 和 BigInteger 都有对应的 “next” 方法。
 * 所有的 “next” 方法都是阻塞的，这意味着它们只有在输入流能提供一个完整可用的数据分词时才会返回。
 * 你也可以根据相应的 “hasNext” 方法是否返回 true，来判断下一个输入分词的类型是否正确。
 */
public class BetterRead {
    public static void main(String[] args) {
        Scanner stdin = new Scanner(SimpleRead.input);
        System.out.println("What is your name?");
        String name = stdin.nextLine();
        System.out.println(name);
        System.out.println("How old are you? What is your favorite double?");
        System.out.println("(input: <age> <double>)");
        int age = stdin.nextInt();
        double favorite = stdin.nextDouble();
        System.out.println(age);
        System.out.println(favorite);
        System.out.printf("Hi %s.%n", name);
        System.out.printf("In 5 years you will be %d.%n", age + 5);
        System.out.printf("My favorite double is %f.", favorite / 2);
    }
}
/* Output:
What is your name?
Sir Robin of Camelot
How old are you? What is your favorite double?
(input: <age> <double>)
22
1.61803
Hi Sir Robin of Camelot.
In 5 years you will be 27.
My favorite double is 0.809015.
 */
