package ch25_annotations;

import java.util.List;

/**
 * @author runningpig66
 * @date 2月23日 周一
 * @time 5:50
 * P.165 §4.1 基本语法 §4.1.1 定义注解
 */
public class PasswordUtils {
    @UseCase(id = 47, description = "Passwords must contain at least one numeric")
    public boolean validatePassword(String passwd) {
        return passwd.matches("\\w*\\d\\w*");
    }

    @UseCase(id = 48)
    public String encryptPassword(String passwd) {
        return new StringBuilder(passwd).reverse().toString();
    }

    @UseCase(id = 49, description = "New passwords can't equal previously used ones")
    public boolean checkForNewPassword(List<String> prevPasswords, String passwd) {
        return !prevPasswords.contains(passwd);
    }
}
