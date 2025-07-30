package ch12_collections;

import java.util.ArrayList;

/**
 * @author runningpig66
 * @date 25/07/24/周四
 * @time 上午 0:41
 */
public class GenericTypeInference {
    void old() {
        ArrayList<Apple> apples = new ArrayList<>();
    }

    void modern() {
        var apples = new ArrayList<Apple>();
    }

    void pitFall() {
        var apples = new ArrayList<>(); // apples 的类型是 ArrayList<Object>
        apples.add(new Apple());
        apples.get(0); // 作为普通的 Object 类型返回
    }
}
