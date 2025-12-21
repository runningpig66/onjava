package ch19_reflection.pets;

import org.jspecify.annotations.NonNull;

import java.util.Objects;

/**
 * @author runningpig66
 * @date 2025/12/18 周四
 * @time 14:27
 * P.604? §19.3 转型前检查
 * <p>
 * 每个 Individual 都有一个 id 和一个可选的名称。
 * 我们必须在每种情况下都显式地编写无参构造器，因为每个类都有一个带参数的构造器，这阻止了编译器自动生成无参构造器。
 */
public class Individual implements Comparable<Individual> {
    private static long counter = 0;
    private final long id = counter++;
    private String name;

    public Individual(String name) {
        this.name = name;
    }

    // 'name' is optional:
    public Individual() {
    }

    @Override
    public String toString() {
        // return getClass().getSimpleName() + " " + Objects.toString(name, ""); // Same thing
        return getClass().getSimpleName() + (name == null ? "" : " " + name);
    }

    public long id() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Individual &&
                Objects.equals(id, ((Individual) obj).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }

    @Override
    public int compareTo(@NonNull Individual o) {
        // Compare by class name first:
        String first = getClass().getSimpleName();
        String argFirst = o.getClass().getSimpleName();
        int firstCompare = first.compareTo(argFirst);
        if (firstCompare != 0) {
            return firstCompare;
        }
        if (name != null && o.name != null) {
            int secondCompare = name.compareTo(o.name);
            if (secondCompare != 0) {
                return secondCompare;
            }
        }
//         return (Long.compare(o.id, id)); // Same thing
        return (o.id < id ? -1 : (o.id == id ? 0 : 1));
    }
}
