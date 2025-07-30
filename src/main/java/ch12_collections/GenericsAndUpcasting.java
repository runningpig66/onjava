package ch12_collections;

import java.util.ArrayList;

/**
 * @author runningpig66
 * @date 25/07/24/周四
 * @time 上午 0:20
 */
class GrannySmith extends Apple {
}

class Gala extends Apple {
}

class Fuji extends Apple {
}

class Braeburn extends Apple {
}

public class GenericsAndUpcasting {
    public static void main(String[] args) {
        ArrayList<Apple> apples = new ArrayList<>();
        apples.add(new GrannySmith());
        apples.add(new Gala());
        apples.add(new Fuji());
        apples.add(new Braeburn());
        for (Apple apple : apples) {
            System.out.println(apple);
        }
    }
}
/* Output:
ch12_collections.GrannySmith@8efb846
ch12_collections.Gala@2a84aee7
ch12_collections.Fuji@a09ee92
ch12_collections.Braeburn@30f39991
 */
