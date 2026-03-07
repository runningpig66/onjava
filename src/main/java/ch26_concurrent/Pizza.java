package ch26_concurrent;

import onjava.Nap;

/**
 * @author runningpig66
 * @date 3月7日 周六
 * @time 20:10
 * P.268 §5.13 工作量、复杂性、成本
 * <p>
 * 假设你要制作一个比萨。在制作过程中，从当前步骤到下一个步骤所需的工作量在这里用枚举的一部分来表示：
 * 和 Machina.java 一样，这又是一个简单的状态机。当比萨被放到盒子中时，状态机到达终点。
 */
public class Pizza {
    public enum Step {
        DOUGH(4), ROLLED(1), SAUCED(1), CHEESED(2),
        TOPPED(5), BAKED(2), SLICED(1), BOXED(0);
        int effort;

        Step(int effort) { // Needed to get to the next step
            this.effort = effort;
        }

        Step forward() {
            if (equals(BOXED)) {
                return BOXED;
            }
            new Nap(effort * 0.1);
            return values()[ordinal() + 1];
        }
    }

    private Step step = Step.DOUGH;
    private final int id;

    public Pizza(int id) {
        this.id = id;
    }

    public Pizza next() {
        step = step.forward();
        System.out.println("currentThread: [" + Thread.currentThread().getName() + "], " +
                "Pizza " + id + ": " + step);
        return this;
    }

    public Pizza next(Step previousStep) {
        // 如果当前状态 != 期望的前置状态
        if (!step.equals(previousStep)) {
            throw new IllegalStateException("Expected " + previousStep + " but found " + step);
        }
        return next(); // 状态校验通过，进入下一步
    }

    public Pizza roll() {
        return next(Step.DOUGH);
    }

    public Pizza sauce() {
        return next(Step.ROLLED);
    }

    public Pizza cheese() {
        return next(Step.SAUCED);
    }

    public Pizza toppings() {
        return next(Step.CHEESED);
    }

    public Pizza bake() {
        return next(Step.TOPPED);
    }

    public Pizza slice() {
        return next(Step.BAKED);
    }

    public Pizza box() {
        return next(Step.SLICED);
    }

    public boolean complete() {
        return step.equals(Step.BOXED);
    }

    @Override
    public String toString() {
        return "Pizza" + id + ": " + (step.equals(Step.BOXED) ? "complete" : step);
    }
}
