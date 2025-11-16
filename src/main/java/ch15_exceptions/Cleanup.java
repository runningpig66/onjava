package ch15_exceptions;

/**
 * @author runningpig66
 * @date 2025/11/15 周六
 * @time 22:30
 * 代码清单 P.454 构造器
 * Guaranteeing proper cleanup of a resource
 * <p>
 * 对于可能在构造过程中抛出异常而且需要清理的类，最安全的用法是使用嵌套的 try 块。
 * <p>
 * 仔细看这里的逻辑：InputFile 对象的构造实际上是在自已的 try 块中。
 * 如果构造失败，就会进人外部的 catch 子句，不会调用 dispose()。
 * 然而，如果构造成功，一定要确保这个对象的清理，所以紧跟在构造操作之后，我们创建了一个新的 try 块。
 * finally 会执行与内部的 try 块关联的清理。
 * 这样，如果构造失败，这个 finally 子句就不会执行了，所以它总是会在构造成功的前提下执行。
 */
public class Cleanup {
    public static void main(String[] args) {
        try {
            InputFile in = new InputFile("src/main/java/ch15_exceptions/Cleanup.java");
            try {
                String s;
                int i = 1;
                while ((s = in.getLine()) != null) {
                    // Perform line-by-line processing here...
                }
            } catch (Exception e) {
                System.out.println("Caught Exception in main");
                e.printStackTrace(System.out);
            } finally {
                in.dispose();
            }
        } catch (Exception e) {
            System.out.println("InputFile construction failed");
        }
    }
}
/* Output:
dispose() successful
 */
