package ch25_annotations;

import onjava.atunit.Test;

/**
 * @author runningpig66
 * @date 2月23日 周一
 * @time 4:47
 * P.163 §4.1 基本语法
 * <p>
 * 在下面的示例中，testExecute() 方法添加了 @Test 注解。该注解本身并不会做任何事，
 * 只是编译器会确保在 CLASSPATH 中存在 @Test 注解的定义。本章稍后会创建一个通过反射来运行该方法的工具。
 */
public class Testable {
    public void execute() {
        System.out.println("Executing..");
    }

    @Test
    void testExecute() {
        execute();
    }
}
