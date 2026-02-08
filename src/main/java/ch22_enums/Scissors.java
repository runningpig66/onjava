package ch22_enums;

import static ch22_enums.Outcome.*;

/**
 * @author runningpig66
 * @date 2月6日 周五
 * @time 16:02
 * P.033 §1.11 多路分发
 */
public class Scissors implements Item {
    @Override
    public Outcome compete(Item it) {
        return it.eval(this);
    }

    @Override
    public Outcome eval(Paper p) {
        return LOSE;
    }

    @Override
    public Outcome eval(Scissors s) {
        return DRAW;
    }

    @Override
    public Outcome eval(Rock r) {
        return WIN;
    }

    @Override
    public String toString() {
        return "Scissors";
    }
}
