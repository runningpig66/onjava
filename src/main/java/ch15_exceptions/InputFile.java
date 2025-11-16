package ch15_exceptions;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author runningpig66
 * @date 2025/11/15 周六
 * @time 21:19
 * 代码清单 P.452 构造器
 * Paying attention to exceptions in constructors
 */
public class InputFile {
    private BufferedReader in;

    public InputFile(String fname) throws Exception {
        try {
            in = new BufferedReader(new FileReader(fname));
            // Other code that might throw exceptions
        } catch (FileNotFoundException e) {
            System.out.println("Could not open " + fname);
            // Wasn't open, so don't close it
            throw e;
        } catch (Exception e) {
            // All other exceptions must close it
            try {
                in.close();
            } catch (IOException e2) {
                System.out.println("in.close() unsuccessful");
            }
            throw e; // Rethrow
        }
    }

    public String getLine() {
        String s;
        try {
            s = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException("readLine() failed");
        }
        return s;
    }

    // 当不再需要 InputFile 对象时，用户必须调用 dispose() 方法。
    // 这将释放 BufferedReader 和 / 或 FileReader 对象所使用的系统资源（比如文件句柄）。
    // 我们不会在使用完 InputFile 对象之前调用它。你可能会考虑把这样的功能放入一个 finalize() 方法中，
    // 但是正如第 6 章所提到的，我们并不是总能确定 finalize() 会被调用（即使能确定它会被调用，也不知道什么时候会被调用）。
    // 这是 Java 的缺点之一：除了内存的清理之外，其他清理都不会自动发生，所以必须告知客户程序员，这应由他们自已处理。
    public void dispose() {
        try {
            in.close();
            System.out.println("dispose() successful");
        } catch (IOException e2) {
            throw new RuntimeException("in.close() failed");
        }
    }
}
