package ch15_exceptions;

/**
 * @author runningpig66
 * @date 2025/11/16 周日
 * @time 4:15
 * 代码清单 P.464 异常匹配
 * Catching exception hierarchies
 * <p>
 * 当一个异常被抛出时，异常处理系统会按照处理程序的编写顺序来寻找“最近的”那个。
 * 当找到一个匹配的处理程序时，它会认为该异常得到了处理，从而不再进行进一步的搜索。
 * 匹配异常时，并不要求这个异常与其处理程序完全匹配。子类的对象可以匹配其基类的处理程序
 */
class Annoyance extends Exception {
}

class Sneeze extends Annoyance {
}

public class Human {
    public static void main(String[] args) {
        // Catch the exact type:
        try {
            // Sneeze 异常被它匹配的第一个 catch 子句捕获了，它是序列中的第一个。
            // 然而，如果移除第一个 catch 子句只留下用于 Annoyance 的 catch 子句，
            // 代码仍然可以工作，因为它要捕捉的是 Sneeze 的基类。
            throw new Sneeze();
        } catch (Sneeze s) {
            System.out.println("Caught Sneeze");
        } catch (Annoyance a) {
            System.out.println("Caught Annoyance");
        }
        // Catch the base type:
        try {
            // 换句话说，catch (Annoyance a) 将捕捉 Annoyance 或者从它派生而来的任何类。这很有用，
            // 因为如果决定向一个方法中加人更多的派生异常，只要客户程序员的代码捕捉的是基类异常，那就不需要修改。
            throw new Sneeze();
        } catch (Annoyance a) {
            System.out.println("Caught Annoyance");
        }
    }
}
/* Output:
Caught Sneeze
Caught Annoyance
 */
