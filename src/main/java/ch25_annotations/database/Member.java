package ch25_annotations.database;

/**
 * @author runningpig66
 * @date 2月24日 周二
 * @time 5:15
 * P.170 §4.2 编写注解处理器 §4.2.3 生成外部文件
 */
@DBTable(name = "MEMBER")
public class Member {
    // 语法糖：当注解元素名为 value，且它是唯一被赋值的元素时，可以省略 "value=" 直接写值。
    @SQLString(30)
    String firstName;
    @SQLString(50)
    String lastName;
    @SQLInteger
    Integer age;
    // 语法限制：一旦需要给除了 value 以外的其他元素（如 constraints）赋值，快捷格式即失效，必须显式写出 "value="。
    @SQLString(value = 30, constraints = @Constraints(primaryKey = true))
    String reference;
    static int memberCount;

    public String getReference() {
        return reference;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public String toString() {
        return reference;
    }
}
