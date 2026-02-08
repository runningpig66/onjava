package ch22_enums;

import static ch22_enums.Outcome.*;

/**
 * @author runningpig66
 * @date 2月6日 周五
 * @time 15:59
 * P.032 §1.11 多路分发
 */
public class Paper implements Item {
    @Override
    public Outcome compete(Item it) {
        return it.eval(this);
    }

    @Override
    public Outcome eval(Paper p) {
        return DRAW;
    }

    @Override
    public Outcome eval(Scissors s) {
        return WIN;
    }

    @Override
    public Outcome eval(Rock r) {
        return LOSE;
    }

    @Override
    public String toString() {
        return "Paper";
    }
}
