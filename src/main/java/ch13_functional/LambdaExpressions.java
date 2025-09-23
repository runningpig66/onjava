package ch13_functional;

/**
 * @author runningpig66
 * @date 2025/8/5 周二
 * @time 3:33
 * 代码清单 P.346
 */
interface Description {
    String brief();
}

interface Body {
    String detailed(String head);
}

interface Multi {
    String twoArg(String head, Double d);
}

public class LambdaExpressions {
    static Body bod = h -> h + " No Parens!"; // [1]
    static Body bod2 = (h) -> h + " More details"; // [2]
    static Description desc = () -> "Short info"; // [3]
    static Multi mult = (h, n) -> h + n; // [4]
    static Description moreLines = () -> { // [5]
        System.out.println("moreLines()");
        return "from moreLines()";
    };

    public static void main(String[] args) {
        System.out.println(bod.detailed("Oh!"));
        System.out.println(bod2.detailed("Hi!"));
        System.out.println(desc.brief());
        System.out.println(mult.twoArg("Pi! ", 3.14159));
        System.out.println(moreLines.brief());
    }
}
/* Output:
Oh! No Parens!
Hi! More details
Short info
Pi! 3.14159
moreLines()
from moreLines()
 */
