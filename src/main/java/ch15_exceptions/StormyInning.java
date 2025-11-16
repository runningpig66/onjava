package ch15_exceptions;

/**
 * @author runningpig66
 * @date 2025/11/15 周六
 * @time 17:52
 * 代码清单 P.449 异常的约束
 * Overridden methods can throw only the exceptions specified in their base-class versions,
 * or exceptions derived from the base-class exceptions
 * <p>
 * 在重写一个方法时，只能抛出该方法的基类版本中说明的异常。
 * 不能因为方法的基类版本中存在一个异常的说明，就认为它一定会存在于这个方法的子类版本中。
 * 这与继承规则不同————基类中的方法也必须存在于子类中：换句话说，
 * 在继承和重写的过程中，“异常说明”可以缩小，但是不能扩大————这与类在继承过程中的规则恰恰相反。
 * <p>
 * notes: StormyInning.md
 * 这是我根据 OnJava 的练习代码和书中的摘录段落写的带注释的代码。
 * 我有一点不清楚： event() 方法同时存在于基类 Inning 和接口 Storm 中，
 * 但是在基类和接口中声明的两个 event() 抛出的异常不同，导致我这里是不是无法让重写的 event() 方法抛出任何异常了？
 * 我似乎只能这样收窄异常，收窄到无法写任何异常，才能正常的重写 event() 方法。
 * 另外，假设基类 Inning 和接口 Storm 中的 event() 方法的抛出异常是相同的，
 * 那么我如果没有重写需求的话，就可以不用强制在 StormyInning 中手动重写 event() 方法，
 * 因为基类 Inning 已经自动帮我实现了接口 Storm 中的 event() 方法。
 * 但是如今，由于基类和接口中声明的两个 event() 抛出的异常不同，
 * 导致我必须强制在 StormyInning 中手动重写 event() 方法，且重写的 event() 方法不能抛出任何异常（异常收窄），
 * 我的理解对吗？
 * ————————————————
 * 当一个方法同时继承自基类和接口时，它在子类中的 throws 列表必须是：
 * 「基类声明的受检异常」∩「所有接口声明的受检异常」的子集。
 * 如果交集为空，就只能把受检异常收窄到“一个也不声明”。
 */
class BaseballException extends Exception {
}

class Foul extends BaseballException {
}

class Strike extends BaseballException {
}

abstract class Inning {
    Inning() throws BaseballException {
    }

    public void event() throws BaseballException {
        // Doesn't actually have to throw anything
    }

    public abstract void atBat() throws Strike, Foul;

    public void walk() {
        // Throws no checked exceptions
    }
}

class StormException extends Exception {
}

class RainedOut extends StormException {
}

class PopFoul extends Foul {
}

interface Storm {
    void event() throws RainedOut;

    void rainHard() throws RainedOut;
}

public class StormyInning extends Inning implements Storm {
    // OK to add new exceptions for constructors, but you
    // must deal with the base constructor exceptions:
    // 对异常的这些约束并不适用于构造器。StormyInning 表明，不管基类构造器抛出什么，
    // 其构造器可以抛出任何它想抛出的东西。因为基类构造器必须总是以这样或那样的方式调用
    // （这里，无参构造器会被自动调用），所以子类构造器必须在其异常说明中声明基类构造器提到的异常。
    // TODO 子类构造器不能捕捉基类构造器抛出的异常。
    public StormyInning() throws RainedOut, BaseballException {
    }

    public StormyInning(String s) throws BaseballException {
    }

    // Regular methods must conform to base class:
    // StormyInning.walk() 之所以无法编译，是因为它抛出了一个 Inning.walk() 没有抛出的异常。
    // 如果允许这种情况，我们就可以编写出调用 Inning.walk() 而不处理任何异常的代码。
    // 然而当我们将其中的对象替换为从 Inning 派生而来的类的对象时，这个方法可能会抛出异常，
    // 我们的代码就无法工作了。通过强制子类的方法遵守基类方法的异常说明，对象的可替换性得以保持。
    //- public void walk() throws PopFoul {} // Compile error

    // Interface CANNOT add exceptions to existing methods from the base class:
    // 当 StormyInning 继承了 Inning, 同时实现了 Storm 接口时，
    // Storm 中的 event() 方法不能改变 Inning 中的 event() 方法的异常说明。
    // 否则在使用基类的时候，我们就无法判断是否捕获了正确的异常，所以这种约束是合理的。
    //- public void event() throws RainedOut {}

    // If the method doesn't already exist in the base class, the exception is OK:
    // 然而，如果接口中描述的方法不在基类之中，比如 rainHard(), 它抛出什么异常都是没有问题的。
    @Override
    public void rainHard() throws RainedOut {
    }

    // You can choose to not throw any exceptions, even if the base version does:
    // 重写的 event() 方法表明，即使基类方法抛出了异常，其子类版本可以选择不抛出任何异常。
    // 同样，这是合理的，因为不会破坏针对基类版本会抛出异常这种情况编写的代码。
    @Override
    public void event() {
    }

    // Overridden methods can throw inherited exceptions:
    // 重写的方法，可以抛出其基类版本所说明的异常的子类：
    // atBat() 抛出的是 PopFoul, 这个异常是从 atBat() 的基类版本所抛出的 Foul 派生而来的。
    // 这样，如果我们编写的代码是处理 Inning 的，而且会调用 atBat(), 我们就必须捕捉 Foul 异常。
    // 而 PopFoul 是从 Foul 派生而来的，所以异常处理程序也能捕捉 PopFoul。
    @Override
    public void atBat() throws PopFoul {
    }

    public static void main(String[] args) {
        try {
            StormyInning si = new StormyInning();
            si.atBat();
        } catch (PopFoul e) {
            System.out.println("Pop foul");
        } catch (RainedOut e) {
            System.out.println("Rained out");
        } catch (BaseballException e) {
            System.out.println("Generic baseball exception");
        }

        // Strike not thrown in derived version.
        try {
            // What happens if you upcast?
            Inning i = new StormyInning();
            i.atBat();
            // You must catch the exceptions from the base-class version of the method:
        } catch (Strike e) {
            System.out.println("Strike");
        } catch (Foul e) {
            System.out.println("Foul");
        } catch (RainedOut e) {
            System.out.println("Rained out");
        } catch (BaseballException e) {
            System.out.println("Generic baseball exception");
        }
    }
}
