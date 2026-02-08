package ch22_enums;

import static ch22_enums.Outcome.*;

/**
 * @author runningpig66
 * @date 2月6日 周五
 * @time 16:01
 * P.033 §1.11 多路分发
 */
public class Rock implements Item {
    @Override
    public Outcome compete(Item it) {
        return it.eval(this);
    }

    @Override
    public Outcome eval(Paper p) {
        return WIN;
    }

    @Override
    public Outcome eval(Scissors s) {
        return LOSE;
    }

    @Override
    public Outcome eval(Rock r) {
        return DRAW;
    }

    @Override
    public String toString() {
        return "Rock";
    }
}
