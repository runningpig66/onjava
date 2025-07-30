package ch12_collections;

import java.util.ArrayList;

/**
 * P.294
 *
 * @author runningpig66
 * @date 25/07/24/周四
 * @time 上午 0:08
 */
class Apple {
    private static long counter;
    private final long id = counter++;

    public long id() {
        return id;
    }
}

class Orange {
}

public class ApplesAndOrangesWithoutGenerics {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        ArrayList apples = new ArrayList();
        for (int i = 0; i < 3; i++) {
            apples.add(new Apple());
        }
        // No problem adding an Orange to apples:
        apples.add(new Orange());
        for (Object apple : apples) {
            ((Apple) apple).id();
            // Orange is detected only at runtime
        }
    }
}
/* Output:
Exception in thread "main" java.lang.ClassCastException: class ch12_collections.Orange cannot be cast to class ch12_collections.Apple (ch12_collections.Orange and ch12_collections.Apple are in unnamed module of loader 'app')
	at ch12_collections.ApplesAndOrangesWithoutGenerics.main(ApplesAndOrangesWithoutGenerics.java:34)
 */
