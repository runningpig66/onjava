package ch19_reflection;

import ch19_reflection.pets.Pet;
import ch19_reflection.pets.PetCreator;
import onjava.Pair;

import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * @author runningpig66
 * @date 2025/12/18 周四
 * @time 20:33
 * P.611 §19.3 转型前检查 §19.3.2 动态的 instanceof
 * <p>
 * Class.isInstance() 方法提供了一种动态验证对象类型的方式。因此，那些乏味的 instanceof 语句就都可以从 PetCounter.java 中删除了。
 * <p>
 * 为了对所有不同类型的 Pet 进行计数，Counter 继承了 HashMap 并预加载了 PetCreator.ALL_TYPES 里的类型。
 * 如果不预加载 Map 里的数据，你最终就只能对随机生成的类型进行计数，而不能包括诸如 Pet 和 Cat 这样的基类型。
 * <p>
 * isInstance() 方法使我们不再需要 instanceof 表达式。此外，这还意味着着，如果想添加新的 Pet 类型，
 * 只需要更改 PetCreator.types 数组就可以，程序的其余部分不需要修改（但在使用 instanceof 表达式时就不可以）。
 * <p>
 * notes: TypeCounter.md
 * Java 中 instanceof、isInstance() 和 isAssignableFrom() 的区别
 */
public class PetCounter3 {
    static class Counter extends HashMap<Class<? extends Pet>, Integer> {
        Counter() {
            super(PetCreator.ALL_TYPES.stream()
                    .map(type -> Pair.make(type, 0))
                    .collect(Collectors.toMap(Pair::key, Pair::value)));
        }

        public void count(Pet pet) {
            // Class.isInstance() eliminates instanceofs:
            entrySet().stream()
                    // 判断 pet 是否是 pair.getKey() 类型的实例（包括其子类）
                    .filter(pair -> pair.getKey().isInstance(pet))
                    .forEach(pair -> put(pair.getKey(), pair.getValue() + 1));
        }

        // 我们重写了 toString() 方法来提供更易于阅读的输出，该输出与打印 Map 时看到的典型输出相似。
        @Override
        public String toString() {
            String result = entrySet().stream()
                    .map(pair -> String.format("%s=%s",
                            pair.getKey().getSimpleName(), pair.getValue()))
                    .collect(Collectors.joining(", "));
            return "{" + result + "}";
        }
    }

    public static void main(String[] args) {
        Counter petCount = new Counter();
        new PetCreator().stream()
                .limit(20)
                .peek(petCount::count)
                .forEach(p -> System.out.print(p.getClass().getSimpleName() + " "));
        System.out.println("\n" + petCount);
    }
}
/* Output:
Rat Manx Cymric Mutt Pug Cymric Pug Manx Cymric Rat EgyptianMau Hamster EgyptianMau Mutt Mutt Cymric Mouse Pug Mouse Cymric
{Pug=3, Pet=20, Cymric=5, Mouse=2, Cat=9, Rodent=5, Dog=6, Mutt=3, Hamster=1, EgyptianMau=2, Manx=7, Rat=2}
 */
