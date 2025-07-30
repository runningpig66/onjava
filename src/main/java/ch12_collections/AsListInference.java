package ch12_collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author runningpig66
 * @date 25/07/24/周四
 * @time 上午 1:46
 */
class Snow {
}

class Powder extends Snow {
}

class Light extends Powder {
}

class Heavy extends Powder {
}

class Crusty extends Snow {
}

class Slush extends Snow {
}

public class AsListInference {
    public static void main(String[] args) {
        List<Snow> snow1 = Arrays.asList(
                new Crusty(), new Slush(), new Powder());
        //- snow1.add(new Heavy()); // 运行时异常
        //- snow1.remove(0); // 运行时异常

        List<Snow> snow2 = Arrays.asList(
                new Light(), new Heavy());
        //- snow2.add(new Slush()); // 运行时异常
        //- snow2.remove(0); // 运行时异常

        List<Snow> snow3 = new ArrayList<>();
        Collections.addAll(snow3, new Light(), new Heavy(), new Powder());
        snow3.add(new Crusty());
        snow3.remove(0);

        // 以显式类型参数说明作为提示
        List<Snow> snow4 = Arrays.asList(
                new Light(), new Heavy(), new Slush());
        //- snow4.add(new Powder()); // 运行时异常
        //- snow4.remove(0); // 运行时异常
    }
}
