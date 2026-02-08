package ch22_enums;

import java.text.DateFormat;
import java.util.Date;

/**
 * @author runningpig66
 * @date 2月4日 周三
 * @time 13:11
 * P.019 §1.10 常量特定方法
 * <p>
 * Java 的枚举机制可以通过为每个枚举实例编写不同的方法，来赋予它们不同的行为。
 * 要实现这一点，你可以在枚举类型中定义一个或多个抽象方法，然后为每个枚举实例编写不同的实现。例如：
 */
public enum ConstantSpecificMethod {
    DATE_TIME {
        @Override
        String getInfo() {
            return DateFormat.getDateInstance().format(new Date());
        }
    },
    CLASSPATH {
        @Override
        String getInfo() {
            return System.getenv("CLASSPATH");
        }
    },
    VERSION {
        @Override
        String getInfo() {
            return System.getProperty("java.version");
        }
    };

    abstract String getInfo();

    public static void main(String[] args) {
        for (ConstantSpecificMethod csm : values()) {
            System.out.println(csm.getInfo());
        }
    }
}
/* Output:
Feb 4, 2026
.;%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\lib\tools.jar;
21.0.9
 */
