package ch22_enums;

/**
 * @author runningpig66
 * @date 2月6日 周五
 * @time 15:57
 * P.032 §1.11 多路分发
 */
public interface Item {
    Outcome compete(Item it);

    Outcome eval(Paper p);

    Outcome eval(Scissors s);

    Outcome eval(Rock r);
}
