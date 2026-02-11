package ch23_references;

/**
 * @author runningpig66
 * @date 2月11日 周三
 * @time 1:50
 * P.079 §2.3 控制可克隆性
 * Check to see if a reference can be cloned
 * <p>
 * 下面这个示例演示了实现克隆，然后在继承层次结构中将其“关闭”的多种方法：
 */
// Can't clone this -- doesn't override clone():
class Ordinary {
}

// Overrides clone, doesn't implement Cloneable:
class WrongClone extends Ordinary {
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); // Throws exception
    }
}

// 1. 开启克隆：IsCloneable 显式实现了 Cloneable 接口并将 clone() 方法公开。
// 它建立了标准的克隆契约，作为该继承体系中具备克隆能力的基类。
// Does all the right things for cloning:
class IsCloneable extends Ordinary implements Cloneable {
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

// 2. 逻辑关闭：由于接口具有传递性，NoMore 无法移除已继承的 Cloneable 标记。
// 因此，它通过重写 clone() 并强制抛出异常，在逻辑层面上阻断了克隆链路。
// Turn off cloning by throwing the exception:
class NoMore extends IsCloneable {
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
}

// 3. 阻断验证：TryMore 试图通过调用 super.clone() 恢复克隆行为。
// 验证结果表明，由于上游 (NoMore) 已破坏了调用链，下游子类将无法获得有效的克隆支持。
class TryMore extends NoMore {
    @Override
    public Object clone() throws CloneNotSupportedException {
        // Calls NoMore.clone(), throws exception:
        return super.clone();
    }
}

// 4. 绕过限制：单纯抛出异常无法彻底阻止子类的克隆行为。“抛出异常”只是一种软限制，它依赖于子类“自觉”地调用 super.clone()。
// 只要子类不调用 super.clone()，而是使用构造函数 (new BackOn()) 创建副本，父类 (NoMore) 的异常逻辑就会被完全旁路，从而恢复克隆能力。
class BackOn extends NoMore {
    private BackOn duplicate(BackOn b) {
        // Somehow make a copy of b and return that copy. A dummy copy, just to make a point:
        return new BackOn();
    }

    @Override
    public Object clone() {
        // Doesn't call NoMore.clone():
        return duplicate(this);
    }
}

// 5. 彻底封死：唯一能物理阻断克隆传递的手段是使用 final 修饰类。这禁止了类的继承，从而从根源上杜绝了子类通过重写方法来绕过限制的可能性。
// 任何试图调用 ReallyNoMore 实例的 clone() 的行为，都会命中 NoMore 中定义的抛异常逻辑（或者 ReallyNoMore 自己重写的抛异常逻辑）。
// You can't inherit from this, so you can't override clone() as you can in BackOn:
final class ReallyNoMore extends NoMore {
}

public class CheckCloneable {
    public static Ordinary tryToClone(Ordinary ord) {
        String id = ord.getClass().getSimpleName();
        System.out.println("Attempting " + id);
        Ordinary x = null;
        if (ord instanceof Cloneable) {
            try {
                x = (Ordinary) ((IsCloneable) ord).clone();
                System.out.println("Cloned " + id);
            } catch (CloneNotSupportedException e) {
                System.out.println("Could not clone " + id);
            }
        } else {
            System.out.println("Doesn't implement Cloneable");
        }
        return x;
    }

    public static void main(String[] args) {
        // Upcasting:
        Ordinary[] ord = {
                new IsCloneable(),
                new WrongClone(),
                new NoMore(),
                new TryMore(),
                new BackOn(),
                new ReallyNoMore(),
        };
        Ordinary x = new Ordinary();
        // This won't compile because clone() is protected in Object:
        //- x = (Ordinary) x.clone();
        // Checks first to see if the class implements Cloneable:
        for (Ordinary ord1 : ord) {
            tryToClone(ord1);
        }
    }
}
/* Output:
Attempting IsCloneable
Cloned IsCloneable
Attempting WrongClone
Doesn't implement Cloneable
Attempting NoMore
Could not clone NoMore
Attempting TryMore
Could not clone TryMore
Attempting BackOn
Cloned BackOn
Attempting ReallyNoMore
Could not clone ReallyNoMore
 */
