package ch18_strings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * @author runningpig66
 * @date 2025/12/16 周二
 * @time 17:24
 * P.584 §18.8 扫描输入
 * <p>
 * 到目前为止，从人类可读的文件或标准输入中读取数据还是比较痛苦的。一般的解决方案是读入一行文本，
 * 对其进行分词解析，然后使用 Integer、Double 等类里的各种方法来解析数据：
 * <p>
 * readLine() 方法将获取的每行输入转为 String 对象，当一行数据只对应一个输入时，处理起来还是很简单的，
 * 但如果两个输入值在一行上，事情就会变得混乱——我们必须拆分该行，才能分别解析每个输入。在这个例子中，拆分发生在创建 numArray 时。
 */
public class SimpleRead {
    public static BufferedReader input =
            new BufferedReader(new StringReader("Sir Robin of Camelot\n22 1.61803"));

    public static void main(String[] args) {
        try {
            System.out.println("What is your name?");
            String name = input.readLine();
            System.out.println(name);
            System.out.println("How old are you? " + "What is your favorite double?");
            System.out.println("(input: <age> <double>)");
            String numbers = input.readLine();
            System.out.println(numbers);
            String[] numArray = numbers.split(" ");
            int age = Integer.parseInt(numArray[0]);
            double favorite = Double.parseDouble(numArray[1]);
            System.out.printf("Hi %s.%n", name);
            System.out.printf("In 5 years you will be %d.%n", age + 5);
            System.out.printf("My favorite double is %f.", favorite / 2);
        } catch (IOException e) {
            System.err.println("I/O exception");
        }
    }
}
/* Output:
What is your name?
Sir Robin of Camelot
How old are you? What is your favorite double?
(input: <age> <double>)
22 1.61803
Hi Sir Robin of Camelot.
In 5 years you will be 27.
My favorite double is 0.809015.
 */
