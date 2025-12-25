package ch20_generics;

import ch20_generics.watercolors.Watercolors;

import java.util.EnumSet;
import java.util.Set;

import static ch20_generics.watercolors.Watercolors.*;
import static onjava.Sets.*;

/**
 * @author runningpig66
 * @date 2025/12/26 周五
 * @time 0:47
 * P.660 §20.4 泛型方法 §20.4.4 Set 实用工具
 * <p>
 * 该示例可以通过 EnumSet 轻松地根据枚举生成 Set (你将在进阶卷第 1 章中了解到 EnumSet 的相关知识)。
 * 此处为静态方法 EnumSet.range() 指定了选取范围的头尾边界元素，用于创建结果 Set。
 * public static <E extends Enum<E>> EnumSet<E> range(E from, E to)
 */
public class WatercolorSets {
    public static void main(String[] args) {
        EnumSet<Watercolors> set1 = EnumSet.range(BRILLIANT_RED, VIRIDIAN_HUE);
        EnumSet<Watercolors> set2 = EnumSet.range(CERULEAN_BLUE_HUE, BURNT_UMBER);
        System.out.println("set1: " + set1);
        System.out.println("set2: " + set2);
        System.out.println("union(set1, set2): " + union(set1, set2));
        Set<Watercolors> subset = intersection(set1, set2);
        System.out.println("intersection(set1, set2): " + subset);
        System.out.println("difference(set1, subset): " + difference(set1, subset));
        System.out.println("difference(set2, subset): " + difference(set2, subset));
        System.out.println("complement(set1, set2): " + complement(set1, set2));
    }
}
/* Output:
set1: [BRILLIANT_RED, CRIMSON, MAGENTA, ROSE_MADDER, VIOLET, CERULEAN_BLUE_HUE, PHTHALO_BLUE, ULTRAMARINE, COBALT_BLUE_HUE, PERMANENT_GREEN, VIRIDIAN_HUE]
set2: [CERULEAN_BLUE_HUE, PHTHALO_BLUE, ULTRAMARINE, COBALT_BLUE_HUE, PERMANENT_GREEN, VIRIDIAN_HUE, SAP_GREEN, YELLOW_OCHRE, BURNT_SIENNA, RAW_UMBER, BURNT_UMBER]
union(set1, set2): [PHTHALO_BLUE, ROSE_MADDER, ULTRAMARINE, VIRIDIAN_HUE, MAGENTA, SAP_GREEN, CRIMSON, BURNT_SIENNA, BURNT_UMBER, RAW_UMBER, CERULEAN_BLUE_HUE, VIOLET, COBALT_BLUE_HUE, PERMANENT_GREEN, BRILLIANT_RED, YELLOW_OCHRE]
intersection(set1, set2): [PHTHALO_BLUE, ULTRAMARINE, VIRIDIAN_HUE, CERULEAN_BLUE_HUE, COBALT_BLUE_HUE, PERMANENT_GREEN]
difference(set1, subset): [ROSE_MADDER, VIOLET, MAGENTA, BRILLIANT_RED, CRIMSON]
difference(set2, subset): [BURNT_SIENNA, BURNT_UMBER, RAW_UMBER, SAP_GREEN, YELLOW_OCHRE]
complement(set1, set2): [ROSE_MADDER, MAGENTA, SAP_GREEN, CRIMSON, BURNT_SIENNA, BURNT_UMBER, RAW_UMBER, VIOLET, BRILLIANT_RED, YELLOW_OCHRE]
 */
