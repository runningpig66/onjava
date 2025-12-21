package ch19_reflection.pets;

import java.util.ArrayList;
import java.util.List;

/**
 * @author runningpig66
 * @date 2025/12/18 周四
 * @time 16:40
 * P.607 §19.3 转型前检查
 * <p>
 * 在实现 Creator 的子类时，必须提供一个 Pet（包括 Pet 子类）的 Class 对象的 List，这样才可以调用 get() 方法来获取 Pet 对象。
 * types() 方法一般来说只需要返回一个静态 List 的引用就可以了。下面是一个使用 forName() 实现的示例：
 */
public class ForNamePetCreator extends Creator {
    private static final List<Class<? extends Pet>> types = new ArrayList<>();
    // Types you want randomly created:
    private static final String[] typeNames = {
            "ch19_reflection.pets.Mutt",
            "ch19_reflection.pets.Pug",
            "ch19_reflection.pets.EgyptianMau",
            "ch19_reflection.pets.Manx",
            "ch19_reflection.pets.Cymric",
            "ch19_reflection.pets.Rat",
            "ch19_reflection.pets.Mouse",
            "ch19_reflection.pets.Hamster"
    };

    // 要生成具有实际类型的 Class 对象的列表，就需要进行强制类型转换，这会产生编译时警告。我们单独定义了 loader() 方法，
    // 然后在静态初始化块中调用了它，这是因为 @SuppressWarnings("unchecked") 注解不能直接用于静态初始化块。
    @SuppressWarnings("unchecked")
    private static void loader() {
        try {
            for (String name : typeNames) {
                types.add((Class<? extends Pet>) Class.forName(name));
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static {
        loader();
    }

    @Override
    public List<Class<? extends Pet>> types() {
        return types;
    }
}
